package com.ua.cm.project.unews;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ua.cm.project.unews.firebase.Firebase;

public class SettingsActivity extends AppCompatActivity {
    private Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebase = new Firebase();

        Fragment frag;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            frag = new SettingsScreen();
            fragmentTransaction.add(R.id.activity_settings, frag, "settings_fragment");
            fragmentTransaction.commit();
        } else {
            frag = getFragmentManager().findFragmentByTag("settings_fragment");
            fragmentTransaction.add(R.id.activity_settings, frag, "settings_fragment");
            fragmentTransaction.commit();
        }

    }

    public static class SettingsScreen extends PreferenceFragment {
        private EditTextPreference name;
        private SwitchPreference notifications;
        private Firebase firebase;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_screen);
            firebase = new Firebase();

            name = (EditTextPreference) findPreference("settingsName");
            notifications = (SwitchPreference) findPreference("settingsNotifications");
            setDefaultName();

            name.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    firebase.getDatabaseReference().child("users").child(firebase.getUserID()).child("profile").child("name").setValue(newValue);
                    return true;
                }
            });
            //MultiSelectListPreference listPreferenceCategories = (MultiSelectListPreference) findPreference("multiSelectCategories");
            //listPreferenceCategories.setEntries(new CharSequence[]{"um", "um", "um", "um", "um"});
        }

        private void setDefaultName() {

            Query query = firebase.getDatabaseReference().child("users").child(firebase.getUserID()).child("profile").child("name");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        String a = (String) dataSnapshot.getValue();
                        name.setText(a);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}

