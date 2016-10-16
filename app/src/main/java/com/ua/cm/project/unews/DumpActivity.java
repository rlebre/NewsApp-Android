package com.ua.cm.project.unews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DumpActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;

    private ToggleButton techToggle;
    private ToggleButton sportsToggle;
    private ToggleButton worldToggle;
    private ToggleButton scienceToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_dump);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        final TextView textView = (TextView) findViewById(R.id.textView);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            textView.setText(user.getEmail());
            Log.d("USSER", user.getEmail() + ".");
        } else {
            textView.setText("USER NULL");
        }

        Button logout = (Button) findViewById(R.id.logout_button);
        logout.setOnClickListener(this);

        techToggle = (ToggleButton) findViewById(R.id.tech_toggle);
        sportsToggle = (ToggleButton) findViewById(R.id.sports_toggle);
        worldToggle = (ToggleButton) findViewById(R.id.world_toggle);
        scienceToggle = (ToggleButton) findViewById(R.id.science_toggle);

        techToggle.setOnClickListener(this);
        sportsToggle.setOnClickListener(this);
        worldToggle.setOnClickListener(this);
        scienceToggle.setOnClickListener(this);
        //------------------------------------------------------------------------------

        techToggle.setChecked(true);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

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

            case R.id.tech_toggle:
                checkValuesToggled();
                break;

            case R.id.sports_toggle:
                checkValuesToggled();
                break;

            case R.id.world_toggle:
                checkValuesToggled();
                break;

            case R.id.science_toggle:
                checkValuesToggled();
                break;

        }
    }

    /*  private void checkValuesToggled() {
          String uid = mAuth.getCurrentUser().getUid();
          Log.d("AAAAAA", uid);
          mDatabaseReference.child("users").child(uid).setValue(null);
          String key = mDatabaseReference.child("users").child(uid).child("categories").getKey();
          Log.d("AAAAAA", key);
          List<String> map = new LinkedList<>();
          map.add("aaaa");
          map.add("aaaa");
          map.add("aaaa");
          Map<String, Object> childUpdates = new HashMap<>();
          childUpdates.put("users/" + uid + "/categories/", map);
          mDatabaseReference.s(childUpdates);

      }*/
    private void checkValuesToggled() {
        String uid = mAuth.getCurrentUser().getUid();

        List<String> map = new LinkedList<>();

        if (techToggle.isChecked())
            map.add("tech");
        if (sportsToggle.isChecked())
            map.add("sports");
        if (worldToggle.isChecked())
            map.add("world");
        if (scienceToggle.isChecked())
            map.add("science");

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("users/" + uid + "/categories/", map);
        mDatabaseReference.updateChildren(childUpdates);

    }
}
