package com.journaldev.mvpdagger2.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v14.preference.SwitchPreference;
import com.journaldev.mvpdagger2.application.App;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.utils.GlideUtils;

import static android.content.Context.MODE_PRIVATE;

public class PreferenceScreenFragment extends PreferenceFragmentCompat {

    public static final String APP_PREFERENCES = "mysettings";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        final SwitchPreference preferenceDarkTheme = (SwitchPreference) findPreference("isDarkTheme");

        preferenceDarkTheme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                App.getAppPreference().setIsDarkTheme((Boolean) newValue);
                savePreferences(preferenceDarkTheme.getKey(), preferenceDarkTheme.isChecked());
                App.getAppPreference().changeTheme(getActivity(), R.style.DarkTheme2, R.style.LightTheme2);
                changeToTheme(getActivity());
                preferenceDarkTheme.setChecked((Boolean) newValue);
                return false;
            }
        });
    }

    public static void changeToTheme(Activity activity) {
        activity.finish();
        Intent intent = new Intent(activity, activity.getClass());
        intent.putExtra("isOptionsChange", true);
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
        saveIsAnim();
        saveIsCache();
    }

    private void saveIsAnim() {
        SwitchPreference preferenceAnim = (SwitchPreference) findPreference("isAnim");
        App.getAppPreference().setIsAnim(preferenceAnim.isChecked());
        savePreferences(preferenceAnim.getKey(), preferenceAnim.isChecked());
    }

    private void saveIsCache() {
        SwitchPreference preferenceCache = (SwitchPreference) findPreference("isCache");
        App.getAppPreference().setIsCache(preferenceCache.isChecked());
        savePreferences(preferenceCache.getKey(), preferenceCache.isChecked());

        cleanCache(preferenceCache.isChecked());
    }

    private void cleanCache(Boolean isCache) {
        if (!isCache)
            GlideUtils.cleanCache(getActivity().getApplicationContext());
    }


    private void savePreferences(String key, boolean value) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

}

