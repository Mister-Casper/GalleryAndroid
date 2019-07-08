package com.journaldev.mvpdagger2.utils;

import android.app.Application;

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MeasurementLaunchTime.startTime = System.currentTimeMillis();
    }
}
