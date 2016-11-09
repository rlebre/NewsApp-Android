package com.ua.cm.project.unews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ua.cm.project.unews.firebase.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CategoriesActivity extends AppCompatActivity implements View.OnClickListener {
    private Firebase firebase;
    private ArrayList<ToggleButton> toggleButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_categories);
        firebase = new Firebase();

        final TextView textView = (TextView) findViewById(R.id.categoriesTextView);

        if (firebase.isUserLoggedIn()) {
            String username = firebase.getUsername() != null ? firebase.getUsername() : firebase.getUserEmail();

            textView.setText(getString(R.string.hi) + ", " + username);
            textView.append(getString(R.string.choose_fav_cat));
            Log.d("USER", firebase.getUserEmail() + ".");
        } else {
            textView.setText(getString(R.string.hi) + "," + getString(R.string.choose_fav_cat));
        }

        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);

        toggleButtons = new ArrayList<>();

        toggleButtons.add((ToggleButton) findViewById(R.id.tech_toggle));
        toggleButtons.add((ToggleButton) findViewById(R.id.sports_toggle));
        toggleButtons.add((ToggleButton) findViewById(R.id.world_toggle));
        toggleButtons.add((ToggleButton) findViewById(R.id.science_toggle));
        toggleButtons.add((ToggleButton) findViewById(R.id.finance_toggle));
        toggleButtons.add((ToggleButton) findViewById(R.id.society_toggle));
        toggleButtons.add((ToggleButton) findViewById(R.id.politics_toggle));
        toggleButtons.add((ToggleButton) findViewById(R.id.entertainment_toggle));
        toggleButtons.add((ToggleButton) findViewById(R.id.health_toggle));
        toggleButtons.add((ToggleButton) findViewById(R.id.travel_toggle));
        toggleButtons.add((ToggleButton) findViewById(R.id.weather_toggle));
        toggleButtons.add((ToggleButton) findViewById(R.id.food_toggle));


        for (ToggleButton t : toggleButtons) {
            t.setOnClickListener(this);
        }

        Query query = firebase.getDatabaseReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("categories").orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    for (ToggleButton t : toggleButtons) {
                        if (t.getText().toString().equalsIgnoreCase((String) s.getValue())) {
                            t.setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ERROR", databaseError.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        firebase.logout();
        LoginManager.getInstance().logOut(); //facebook logout if logged in
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_button:
                firebase.logout();
                LoginManager.getInstance().logOut(); //facebook logout if logged in
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
            case R.id.categories_save_button:
                checkValuesToggled();
                startActivity(new Intent(getApplicationContext(), TopicsActivity.class));
                finish();
                break;
        }
    }

    private void checkValuesToggled() {
        List<String> map = new LinkedList<>();

        for (ToggleButton t : toggleButtons) {
            if (t.isChecked()) {
                map.add(t.getText().toString().toLowerCase());
            }
        }
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("users/" + firebase.getUserID() + "/categories/", map);
        firebase.getDatabaseReference().updateChildren(childUpdates);
    }
}
