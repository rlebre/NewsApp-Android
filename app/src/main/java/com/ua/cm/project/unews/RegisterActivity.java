package com.ua.cm.project.unews;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.ServerValue;
import com.ua.cm.project.unews.firebase.Firebase;

/**
 * A login screen that offers register
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Firebase firebase;

    private EditText mNameView;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebase = new Firebase();
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mNameView = (EditText) findViewById(R.id.register_name);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });


        progressDialog = new ProgressDialog(this);

        findViewById(R.id.register_button).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_button:
                attemptRegister();
                break;
        }
    }

    private void attemptRegister() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            progressDialog.setMessage(getString(R.string.registering_user));
            progressDialog.show();
            firebase.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, getString(R.string.registered_successfully), Toast.LENGTH_SHORT).show();
                        firebase.sendEmailVerification();
                        saveInfo(mNameView.getText().toString(), firebase.getUserEmail());
                        Intent intent = new Intent(getApplicationContext(), CategoriesActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, R.string.could_not_regist, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    private void saveInfo(String name, String email) {
        String uid = firebase.getUserID();
        firebase.getDatabaseReference().child("users").child(uid).child("profile").child("name").setValue(name);
        firebase.getDatabaseReference().child("users").child(uid).child("profile").child("email").setValue(email);
        firebase.getDatabaseReference().child("users").child(uid).child("profile").child("reg_timestamp").setValue(ServerValue.TIMESTAMP);
    }

}

