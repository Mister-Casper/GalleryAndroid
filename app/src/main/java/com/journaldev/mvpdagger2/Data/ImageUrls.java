package com.journaldev.mvpdagger2.Data;

import android.content.Context;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
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
    private static LinkedList<Boolean> like = new LinkedList<>();

    public static boolean isUpdate = false;



    public static LinkedList<ItemPhotoData> getUrls(Context context) {
        if (mUrls == null) {
            getImageUrl(context);
        }
        if (isUpdate == true) {
            getImageUrl(context);
            isUpdate = false;
        }
        return mUrls;
    }

    private static long[] mDate;
    private static Cursor cc = null;

    private static int maxImageId = 0;


    private static void getImageUrl(Context context) {
        cc = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);
        loadUrl();
    }

    private static void loadUrl() {
        if (cc != null) {
            cc.moveToFirst();
            mUrls = new LinkedList<>();
            mDate = new long[cc.getCount()];
            for (int i =  cc.getCount() - 1; i >= 0; i--) {
                cc.moveToPosition(i);
                Uri temp = Uri.parse(cc.getString(1));
                mUrls.add(new ItemPhotoData(temp ,Boolean.parseBoolean(isLikeImage(temp,ExifInterface.TAG_USER_COMMENT))));;
            }
        }
        maxImageId = mUrls.size();
    }

    private static String isLikeImage(Uri uri ,String tag ) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String attribute = exif.getAttribute(tag);
        return attribute;
    }

}
