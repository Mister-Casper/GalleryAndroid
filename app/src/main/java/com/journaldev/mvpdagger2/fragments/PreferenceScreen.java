package com.journaldev.mvpdagger2.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.annotation.Nullable;
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
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.gray));
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        SwitchPreference preference = (SwitchPreference) findPreference("isAnim");
        AppPreference.setIsAnim(preference.isChecked());
        savePreferences(preference.getKey(), preference.isChecked());
    }

    private void savePreferences(String key, boolean value) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

}

