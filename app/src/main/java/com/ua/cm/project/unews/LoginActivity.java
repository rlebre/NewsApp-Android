package com.ua.cm.project.unews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.ua.cm.project.unews.firebase.Firebase;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    protected FirebaseAuth.AuthStateListener mAuthListener;
    static final String TAG = "LOGIN_ACTIVITY";

    private FirebaseAnalytics mFirebaseAnalytics;

    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;
    private LoginButton loginButton;
    private static final int RC_SIGN_IN = 9001;

    private Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.firebase = new Firebase();

        this.findViewById(R.id.login_google_button).setOnClickListener(this);
        this.findViewById(R.id.login_register_button).setOnClickListener(this);
        this.findViewById(R.id.login_facebook_button).setOnClickListener(this);
        this.findViewById(R.id.login_email_button).setOnClickListener(this);
        this.findViewById(R.id.not_now_hyperlink).setOnClickListener(this);

        mCallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_facebook_button);
        loginButton.setReadPermissions(Arrays.asList("email"));


        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:SUCCESS " + loginButton.toString());
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                Toast.makeText(getApplicationContext(), getString(R.string.cancelled), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                Toast.makeText(getApplicationContext(), getString(R.string.error) + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                /*if (firebase.isUserLoggedIn()) {
                    startNextActivity();
                } else {
                    //startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                }*/
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebase.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Log.d("LOG", result.getStatus().getStatusMessage());
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebase.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getApplicationContext(), getString(R.string.auth_failed_short), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.logged_in), Toast.LENGTH_SHORT).show();
                            Query query = firebase.getDatabaseReference().child("users").child(firebase.getUserID()).child("profile").child("reg_timestamp");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot reg_timestamp) {
                                    if (!reg_timestamp.exists()) {
                                        saveInfo(firebase.getUserID(), firebase.getUsername(), firebase.getUserEmail(), "google", true);
                                    } else {
                                        saveInfo(firebase.getUserID(), firebase.getUsername(), firebase.getUserEmail(), "google", false);
                                    }

                                    startNextActivity();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.d("ERROR", databaseError.getMessage());
                                }
                            });
                        }
                    }
                });
    }


    public void startNextActivity() {
        // caso seja pedida a proxima activity sem o utilizador estar registado, entao e porque este nao se deseja logar
        if (!firebase.isUserLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), TopicsActivity.class));
            finish();
        } else {
            Query query = firebase.getDatabaseReference().child("users").child(firebase.getUserID()).child("categories").orderByKey();
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        startActivity(new Intent(getApplicationContext(), CategoriesActivity.class));
                    } else {
                        startActivity(new Intent(getApplicationContext(), TopicsActivity.class));
                    }
                    finish();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("ERROR", databaseError.getMessage());
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_email_button:
                startActivity(new Intent(LoginActivity.this, LoginEmailActivity.class));
                //Intent intent = new Intent(this, LoginEmailActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(intent);
                //finish();
                break;

            case R.id.login_register_button:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;

            case R.id.not_now_hyperlink:
                //signInAnonymous();
                startNextActivity();
                break;

            case R.id.login_google_button:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;

            default:
                break;

        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "signInWithCredential:ENTREIII");
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebase.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed_short) + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            LoginManager.getInstance().logOut();
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.logged_in), Toast.LENGTH_SHORT).show();

                            Query query = firebase.getDatabaseReference().child("users").child(firebase.getUserID()).child("profile").child("reg_timestamp");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot reg_timestamp) {
                                    if (!reg_timestamp.exists()) {
                                        saveInfo(firebase.getUserID(), firebase.getUsername(), firebase.getUserEmail(), "facebook", true);
                                    } else {
                                        saveInfo(firebase.getUserID(), firebase.getUsername(), firebase.getUserEmail(), "facebook", false);
                                    }

                                    startNextActivity();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.d("ERROR", databaseError.getMessage());
                                }
                            });
                        }
                    }
                });
    }

    private void signInAnonymous() {
        firebase.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInAnonymously", task.getException());
                            Toast.makeText(getApplicationContext(), getString(R.string.auth_failed_short), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveInfo(String uid, String name, String email, String accType, boolean firstLogin) {
        if (firstLogin) {
            firebase.getDatabaseReference().child("users").child(uid).child("profile").child("reg_timestamp").setValue(ServerValue.TIMESTAMP);
        }

        /*
        Date date = new Date(new Long("1478694919674"));
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sfd.setTimeZone(TimeZone.getTimeZone("GMT"));
        Log.d("DATE", sfd.format(date));
        */

        firebase.getDatabaseReference().child("users").child(uid).child("profile").child("name").setValue(name);
        firebase.getDatabaseReference().child("users").child(uid).child("profile").child("email").setValue(email);
        firebase.getDatabaseReference().child("users").child(uid).child("profile").child("last_login").setValue(ServerValue.TIMESTAMP);
        firebase.getDatabaseReference().child("users").child(uid).child("profile").child("account_type").setValue(accType);
    }
}
