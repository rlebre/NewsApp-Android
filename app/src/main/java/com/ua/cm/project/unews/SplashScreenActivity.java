package com.ua.cm.project.unews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

        if(!isNetworkAvailable()){
            new AlertDialog.Builder(SplashScreenActivity.this)
                    .setTitle("Info")

                    .setMessage("Internet not available, Cross check your internet connectivity and try again")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }else {

            firebase = new Firebase();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (!firebase.isUserLoggedIn()) {
                        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                        finish();
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
