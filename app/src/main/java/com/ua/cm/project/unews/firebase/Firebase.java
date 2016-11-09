package com.ua.cm.project.unews.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

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

    public DatabaseReference getDatabaseReference() {
        return mDatabaseReference;
    }

    public void logout() {
        mAuth.signOut();
    }

    public void addAuthStateListener(AuthStateListener authListener) {
        mAuth.addAuthStateListener(authListener);
    }

    public void removeAuthStateListener(AuthStateListener authListener) {
        mAuth.removeAuthStateListener(authListener);
    }

    public Task<AuthResult> signInWithCredential(AuthCredential credential) {
        return mAuth.signInWithCredential(credential);
    }

    public Task<AuthResult> signInWithEmailAndPassword(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> signInAnonymously() {
        return mAuth.signInAnonymously();
    }

    public List<String> getSubscribedFeeds() {
        String[] urls = {"http://www.jornaldenegocios.pt/rss", "http://feeds.feedburner.com/PublicoRSS", "http://www.rtp.pt/noticias/rss", "http://feeds.feedburner.com/expresso-geral"};
        return Arrays.asList(urls);
    }
}
