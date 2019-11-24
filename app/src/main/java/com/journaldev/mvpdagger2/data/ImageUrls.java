package com.journaldev.mvpdagger2.data;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;


import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class ImageUrls {
    private static LinkedList<Image> imageUrls;

    public static LinkedList<Image> getUrls(Context context) {
        if (imageUrls == null) {
            getImageUrl(context);
        }
        return imageUrls;
    }

    private static void getImageUrl(Context context) {
        Cursor imageCursor = getCursor(context);
        registerContentObserver(context, imageCursor);
        loadUrl(imageCursor);
    }

    private static Cursor getCursor(Context context) {
        return context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, null,
                null, null);
    }

    private static void registerContentObserver(Context context, Cursor imageCursor) {
        Handler handler = new Handler(Looper.getMainLooper());

        context.getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true, new ImageObserver(handler, imageCursor));
    }

    private static void loadUrl(Cursor imageCursor) {
        if (imageCursor != null) {
            imageCursor.moveToFirst();
            imageUrls = new LinkedList<>();
            for (int i = imageCursor.getCount() - 1; i >= 0; i--) {
                imageCursor.moveToPosition(i);
                Uri temp = Uri.parse(imageCursor.getString(1));
                File file = new File(temp.toString());
                if (file.exists())
                    imageUrls.add(new Image(temp, Boolean.parseBoolean(isLikeImage(temp, ExifInterface.TAG_USER_COMMENT))));
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
        Cursor cursor;

        public ImageObserver(Handler handler, Cursor cursor) {
            super(handler);
            this.cursor = cursor;
        }

        @Override
        public boolean deliverSelfNotifications() {
            return false;
        }

        @Override
        public void onChange(boolean arg0) {
            super.onChange(arg0);
            loadUrl(cursor);
        }
    }
}
