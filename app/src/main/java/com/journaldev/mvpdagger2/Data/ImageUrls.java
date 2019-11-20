package com.journaldev.mvpdagger2.Data;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;

public class ImageUrls {
    private static LinkedList<ItemPhotoData> mUrls;

    public static LinkedList<ItemPhotoData> getUrls(Context context) {
        if (mUrls == null) {
            getImageUrl(context);
        }
        return mUrls;
    }

    private static Cursor cc = null;

    private static void getImageUrl(Context context) {

        Handler handler = new Handler(Looper.getMainLooper());
        context.getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true, new ImageObserver(handler));

        cc = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);

        loadUrl();
    }

    private static void loadUrl() {
        if (cc != null) {
            cc.moveToFirst();
            mUrls = new LinkedList<>();
            for (int i = cc.getCount() - 1; i >= 0; i--) {
                cc.moveToPosition(i);
                Uri temp = Uri.parse(cc.getString(1));
                File file = new File(temp.toString());
                if (file.exists())
                    mUrls.add(new ItemPhotoData(temp, Boolean.parseBoolean(isLikeImage(temp, ExifInterface.TAG_USER_COMMENT))));
            }
        }
    }

    private static String isLikeImage(Uri uri, String tag) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String attribute = exif.getAttribute(tag);
        return attribute;
    }

    static class ImageObserver extends ContentObserver {

        public ImageObserver(Handler handler) {
            super(handler);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return false;
        }

        @Override
        public void onChange(boolean arg0) {
            super.onChange(arg0);
            loadUrl();
        }
    }
}
