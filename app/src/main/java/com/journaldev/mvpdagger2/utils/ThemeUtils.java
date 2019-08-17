package com.journaldev.mvpdagger2.utils;

import android.app.Activity;

public class ThemeUtils {
    public static Boolean isDarkTheme = null;

    public static int chandgeTheme(Activity activity, int darkTheme, int lightTheme) {
        if (isDarkTheme == null) {
            activity.setTheme(lightTheme);
            return lightTheme;
        }

        if (isDarkTheme) {
            activity.setTheme(darkTheme);
            return darkTheme;
        } else
            activity.setTheme(lightTheme);

        return lightTheme;
    }
}
