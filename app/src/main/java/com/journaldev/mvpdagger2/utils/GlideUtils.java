package com.journaldev.mvpdagger2.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class GlideUtils {

    public static void cleanCache(final Context context) {
        new Thread(() -> Glide.get(context).clearDiskCache()).start();
    }

    public static RequestOptions optionsCleanCache(RequestOptions options) {
        RequestOptions resultOptions = options;
        resultOptions = resultOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        resultOptions = resultOptions.skipMemoryCache(true);
        return resultOptions;
    }
}
