package com.journaldev.mvpdagger2.application;

import android.app.Application;

public class App extends Application {
    private static App app;
    private static AppPreference appPreference;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        appPreference = new AppPreference(app);
    }

    public static App getApp() {
        return app;
    }

    public static AppPreference getAppPreference() {
        return appPreference;
    }
}
