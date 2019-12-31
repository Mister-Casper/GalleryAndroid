package com.journaldev.mvpdagger2.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.journaldev.mvpdagger2.application.App;

public abstract class BaseThemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppPreference().changeTheme(this, getDarkTheme(), getLightTheme());
    }

    abstract int getDarkTheme();

    abstract int getLightTheme();
}
