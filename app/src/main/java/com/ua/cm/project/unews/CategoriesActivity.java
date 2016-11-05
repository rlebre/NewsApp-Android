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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CategoriesActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;

    private ArrayList<ToggleButton> toggleButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_categories);

        mAuth = FirebaseAuth.getInstance();
        final TextView textView = (TextView) findViewById(R.id.textView);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String username = user.getDisplayName() != null ? user.getDisplayName() : user.getEmail();

            textView.setText("Hi, " + username);
            textView.append("\nPlease choose favorite categories.");
            Log.d("USSER", user.getEmail() + ".");
        } else {
            textView.setText("Hi, please choose favorite categories.");
        }

        Button logout = (Button) findViewById(R.id.logout_button);
        logout.setOnClickListener(this);

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

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        Query query = mDatabaseReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("categories").orderByKey();
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
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout_button:
                mAuth.signOut();
                LoginManager.getInstance().logOut();
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
        String uid = mAuth.getCurrentUser().getUid();

        List<String> map = new LinkedList<>();

        for (ToggleButton t : toggleButtons) {
            if (t.isChecked()) {
                map.add(t.getText().toString().toLowerCase());
            }
        }
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("users/" + uid + "/categories/", map);
        mDatabaseReference.updateChildren(childUpdates);

    }
}
