package com.journaldev.mvpdagger2.utils;

public class MeasurementLaunchTime {

    public static long startTime;
    public static long loadTime;


    public static long getLaunchTime(){
        return loadTime - startTime;
    }

}
