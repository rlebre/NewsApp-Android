package com.ua.cm.project.unews;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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

    private static class SettingsScreen extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_screen);

            //MultiSelectListPreference listPreferenceCategories = (MultiSelectListPreference) findPreference("multiSelectCategories");
            //listPreferenceCategories.setEntries(new CharSequence[]{"um", "um", "um", "um", "um"});
        }
    }
}
