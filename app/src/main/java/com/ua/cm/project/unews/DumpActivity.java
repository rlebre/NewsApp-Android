package com.ua.cm.project.unews;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DumpActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private ToggleButton techToggle;
    private ToggleButton sportsToggle;
    private ToggleButton worldToggle;
    private ToggleButton scienceToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dump);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView textView = (TextView) findViewById(R.id.textView);


        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    textView.setText(user.getEmail());
                    Log.d("USSER", user.getEmail() + ".");
                } else {
                    textView.setText("USER NULL");
                }
            }
        };

        Button logout = (Button) findViewById(R.id.logout_button);


        techToggle = (ToggleButton) findViewById(R.id.tech_toggle);
        sportsToggle = (ToggleButton) findViewById(R.id.sports_toggle);
        worldToggle = (ToggleButton) findViewById(R.id.world_toggle);
        scienceToggle = (ToggleButton) findViewById(R.id.science_toggle);

        //------------------------------------------------------------------------------

        databaseReference = FirebaseDatabase.getInstance().getReference();

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
                break;

            case R.id.sports_toggle:
                break;

            case R.id.world_toggle:
                break;

            case R.id.science_toggle:
                break;

        }
    }
}
