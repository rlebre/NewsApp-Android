package com.ua.cm.project.unews;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    protected FirebaseAuth mAuth;
    protected FirebaseAuth.AuthStateListener mAuthListener;
    static final String TAG_FIREBASE = "FIREBASE";

    private Button login_email_button;
    private Button register_button;

    private FirebaseAnalytics mFirebaseAnalytics;

// ...


// Obtain the FirebaseAnalytics instance.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_email_button = (Button) findViewById(R.id.login_login_email_button);
        register_button = (Button) findViewById(R.id.login_register_button);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG_FIREBASE, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG_FIREBASE, "onAuthStateChanged:signed_out");
                }
            }
        };

        final Activity a = this;
        login_email_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(a, LoginEmailActivity.class));

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // Name, email address, and profile photo Url
                    String name = user.getDisplayName();
                    String email = user.getEmail();
                    Uri photoUrl = user.getPhotoUrl();

                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                    // authenticate with your backend server, if you have one. Use
                    // FirebaseUser.getToken() instead.
                    String uid = user.getUid();

                    Log.d(TAG_FIREBASE, name + email + photoUrl + uid + "<=========");
                }
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(a, RegisterActivity.class));
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
