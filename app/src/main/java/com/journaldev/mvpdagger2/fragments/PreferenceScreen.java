package com.journaldev.mvpdagger2.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v14.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.journaldev.mvpdagger2.Data.AppPreference;
import com.journaldev.mvpdagger2.R;

import static android.content.Context.MODE_PRIVATE;

public class PreferenceScreen extends PreferenceFragmentCompat {

    public static final String APP_PREFERENCES = "mysettings";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        AppPreference.load(getActivity());
        final SwitchPreference preferenceDarkTheme = (SwitchPreference) findPreference("isDarkTheme");

        preferenceDarkTheme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                AppPreference.setIsDarkTheme((Boolean) newValue);
                savePreferences(preferenceDarkTheme.getKey(), preferenceDarkTheme.isChecked());
                int currentTheme = AppPreference.chandgeTheme(getActivity(),R.style.DarkTheme2,R.style.LightTheme2);
                changeToTheme(getActivity());
                preferenceDarkTheme.setChecked((Boolean) newValue);
                return false;
            }
        });
    }

    public static void changeToTheme(Activity activity) {
        activity.finish();
        Intent intent = new Intent(activity, activity.getClass());
        intent.putExtra("isOptionsChandge",true);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public void onStop() {
        super.onStop();
        SwitchPreference preferenceAnim = (SwitchPreference) findPreference("isAnim");

        AppPreference.setIsAnim(preferenceAnim.isChecked());
        savePreferences(preferenceAnim.getKey(), preferenceAnim.isChecked());
    }

    private void savePreferences(String key, boolean value) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

}

