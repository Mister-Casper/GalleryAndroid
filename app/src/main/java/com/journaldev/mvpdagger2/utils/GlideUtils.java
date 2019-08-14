package com.journaldev.mvpdagger2.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.journaldev.mvpdagger2.activity.MainActivity;

public class GlideUtils {

    public static void cleanCache(final Context context)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }).start();
    }

    public static void optionsCleanCache(RequestOptions options) {
        options.diskCacheStrategy(DiskCacheStrategy.NONE);
        options.skipMemoryCache(true);
    }
}
