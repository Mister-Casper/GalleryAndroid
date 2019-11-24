package com.journaldev.mvpdagger2.utils;

import android.app.Activity;

public class ThemeUtils {
    public static Boolean isDarkTheme = null;

    public static void changeTheme(Activity activity, int darkTheme, int lightTheme) {
        if (isDarkTheme == null) {
            activity.setTheme(lightTheme);
        }

        if (isDarkTheme) {
            activity.setTheme(darkTheme);
        } else
            activity.setTheme(lightTheme);
    }
}
