package com.journaldev.mvpdagger2.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

public class AppPreference {

    public static Boolean getIsAnim() {
        if(isAnim == null)
            throw new IllegalArgumentException();

        return isAnim;
    }

    public static void setIsAnim(Boolean isAnim) {
        AppPreference.isAnim = isAnim;
    }

    private static Boolean isAnim = null;

    public static void load(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        isAnim = prefs.getBoolean("isAnim",
                true);
    }


}
