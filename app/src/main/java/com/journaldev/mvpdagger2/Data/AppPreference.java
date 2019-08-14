package com.journaldev.mvpdagger2.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.journaldev.mvpdagger2.utils.GlideUtils;
import com.journaldev.mvpdagger2.utils.ThemeUtils;

public class AppPreference {

    public static Boolean getIsAnim() {
        if (isAnim == null)
            throw new IllegalArgumentException();

        return isAnim;
    }

    public static Boolean getIsCache() {
        if (isCache == null)
            throw new IllegalArgumentException();

        return isCache;
    }

    public static void setIsAnim(Boolean isAnim) {
        AppPreference.isAnim = isAnim;
    }

    public static void setIsDarkTheme(Boolean isDarkTheme) {
        ThemeUtils.isDarkTheme = isDarkTheme;
    }

    public static void setIsCache(Boolean isCache) {
        AppPreference.isCache = isCache;
    }


    private static Boolean isAnim = null;
    private static Boolean isCache = null;

    public static void load(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        loadIsAnim(prefs);
        loadIsDarkTheme(prefs);
        loadIsCache(prefs);
    }

    private static void loadIsAnim(SharedPreferences prefs) {
        isAnim = prefs.getBoolean("isAnim",
                true);
    }

    private static void loadIsDarkTheme(SharedPreferences prefs) {
        ThemeUtils.isDarkTheme = prefs.getBoolean("isDarkTheme",
                false);
    }

    private static void loadIsCache(SharedPreferences prefs) {
        AppPreference.setIsCache(prefs.getBoolean("isCache",
                true));
    }

}
