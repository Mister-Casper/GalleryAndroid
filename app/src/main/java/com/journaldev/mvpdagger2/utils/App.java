package com.journaldev.mvpdagger2.utils;

import android.app.Application;

public class App extends Application {
    private static App app;
    private static ImageHelper imageHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        imageHelper = new ImageHelper(getApplicationContext());
    }

    public static ImageHelper getImageHelper() {
        return imageHelper;
    }

    public static App getApp() {
        return app;
    }
}
