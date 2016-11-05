package com.ua.cm.project.unews;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;

import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SplashScreenActivity extends AppCompatActivity {

    ProgressBar progressBar;
    boolean isFavChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_splash_screen);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                } else {
                    DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
                    Query query = mFirebaseDatabaseReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("categories").orderByKey();
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            isFavChosen = dataSnapshot.exists();

                            if (!isFavChosen) {
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
        }, 2000);
    }
}
