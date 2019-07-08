package com.journaldev.mvpdagger2.utils;

import android.os.Build;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

public class FabricEvents {

    public static void sendLaunchTime(long milisecunds)
    {
        Answers.getInstance().logCustom ( new CustomEvent("AppLaunchTime")
                .putCustomAttribute("launchTimeMilisecunds",milisecunds));
    }

}
