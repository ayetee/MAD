package com.mad.assignment11453798.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.mad.assignment11453798.R;

/**
 * SettingsActivity Class
 */
public class SettingsActivity extends PreferenceActivity {

    /**
     * Overrides onCreate()
     * Initiates settings page
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    /**
     * SettingsFragment Class
     */
    public static class SettingsFragment extends PreferenceFragment
    {
        /**
         * Overrides onCreate()
         * Initiates fragment with preferences.xml
         */
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}