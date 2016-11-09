package com.ua.cm.project.unews;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ua.cm.project.unews.firebase.Firebase;

public class SplashScreenActivity extends AppCompatActivity {
    private Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_splash_screen);
        firebase = new Firebase();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!firebase.isUserLoggedIn()) {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                } else {
                    Query query = firebase.getDatabaseReference().child("users").child(firebase.getUserID()).child("categories").orderByKey();
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot userCategories) {
                            if (!userCategories.exists()) {
                                startActivity(new Intent(SplashScreenActivity.this, CategoriesActivity.class));
                            } else {
                                startActivity(new Intent(SplashScreenActivity.this, TopicsActivity.class));
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
        }, 1000);
    }
}
