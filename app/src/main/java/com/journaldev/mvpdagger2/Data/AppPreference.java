package com.journaldev.mvpdagger2.Data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.preference.PreferenceManager;

public class AppPreference {

    public static Boolean getIsAnim() {
        if (isAnim == null)
            throw new IllegalArgumentException();

        return isAnim;
    }

    public static void setIsAnim(Boolean isAnim) {
        AppPreference.isAnim = isAnim;
    }

    public static Boolean getIsDarkTheme() {
        if (isDarkTheme == null)
            throw new IllegalArgumentException();

        return isDarkTheme;
    }

    public static void setIsDarkTheme(Boolean isDarkTheme) {
        AppPreference.isDarkTheme = isDarkTheme;
    }

    private static Boolean isDarkTheme = null;
    private static Boolean isAnim = null;

    public static void load(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        loadIsAnim(prefs);
        loadIsDarkTheme(prefs);
    }

    private static void loadIsAnim(SharedPreferences prefs) {
        isAnim = prefs.getBoolean("isAnim",
                true);
    }

    private static void loadIsDarkTheme(SharedPreferences prefs) {
        isDarkTheme = prefs.getBoolean("isDarkTheme",
                false);
    }

    public static int chandgeTheme(Activity activity, int darkTheme, int lightTheme) {
        if (isDarkTheme == null)
            activity.setTheme(lightTheme);

        if (isDarkTheme) {
            activity.setTheme(darkTheme);
            return darkTheme;
        }
        else
            activity.setTheme(lightTheme);

        return lightTheme;
    }
}
