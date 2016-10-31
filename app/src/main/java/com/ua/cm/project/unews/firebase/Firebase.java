package com.ua.cm.project.unews.firebase;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by rui on 10/25/16.
 */

public class Firebase {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseAnalytics mFirebaseAnalytics;

    public Firebase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public String getUserID() {
        if (this.isUserLoggedIn()) {
            return mAuth.getCurrentUser().getUid();
        }

        return "";
    }

    public String getUsername() {
        if (this.isUserLoggedIn()) {
            return mAuth.getCurrentUser().getDisplayName();
        }
        return "";
    }

    public String getUserEmail() {
        if (this.isUserLoggedIn()) {
            return mAuth.getCurrentUser().getEmail();
        }
        return "";
    }

    public void logout() {
        mAuth.signOut();
    }
}
