package com.journaldev.mvpdagger2.data.Image;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;


import com.journaldev.mvpdagger2.model.ImageModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class ImageRepository {
    private static ArrayList<ImageModel> imageModelUrls;

    public static ArrayList<ImageModel> getUrls(Context context) {
        if (imageModelUrls == null) {
            getImageUrl(context);
        }
        return imageModelUrls;
    }

    private static void getImageUrl(Context context) {
        Cursor imageCursor = getCursor(context);
        loadUrl(imageCursor);

        registerContentObserver(context);
    }

    private static Cursor getCursor(Context context) {
        return context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, null,
                null, null);
    }

    private static void registerContentObserver(Context context) {
        Handler handler = new Handler(Looper.getMainLooper());

        context.getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true, new ImageObserver(handler,context));
    }

    private static void loadUrl(Cursor imageCursor) {
        if (imageCursor != null) {
            imageModelUrls = new ArrayList<>();
            for (int i = imageCursor.getCount() - 1; i >= 0; i--) {
                imageCursor.moveToPosition(i);
                Uri temp = Uri.parse(imageCursor.getString(1));
                File file = new File(temp.toString());
                if (file.exists())
                    imageModelUrls.add(new ImageModel(temp, Boolean.parseBoolean(isLikeImage(temp, ExifInterface.TAG_USER_COMMENT))));
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

    public static class ImageObserver extends ContentObserver {

        private static ArrayList<ImageRepositoryObserver> observers = new ArrayList<>();

        public static void addImageUrlsRepositoryObserver(ImageRepositoryObserver repositoryObserver) {
            if (!observers.contains(repositoryObserver)) {
                observers.add(repositoryObserver);
            }
        }

        public static void removeImageUrlsRepositoryObserver(ImageRepositoryObserver repositoryObserver) {
            observers.remove(repositoryObserver);
        }

        Context context;

        public ImageObserver(Handler handler,Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public boolean deliverSelfNotifications() {
            return false;
        }

        @Override
        public void onChange(boolean arg0) {
            super.onChange(arg0);
            Cursor cursor = getCursor(context);
            loadUrl(cursor);
            for (int i = 0; i < observers.size(); i++) {
                observers.get(i).onUpdateImage(imageModelUrls);
            }
        }
    }
}
