package com.journaldev.mvpdagger2.data.Image;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;

import com.journaldev.mvpdagger2.data.BaseImageObserver;
import com.journaldev.mvpdagger2.model.Converter.ImageModelConverter;
import com.journaldev.mvpdagger2.model.ImageModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ImageRepository {
    private ArrayList<ImageModel> imageModelUrls;
    private ImageObserver imageObserver;

    public ArrayList<ImageModel> getUrls() {
        return imageModelUrls;
    }

    public ImageObserver getImageObserver() {
        return imageObserver;
    }

    public ImageRepository(Context context) {
        Cursor imageCursor = getCursor(context);
        loadData(imageCursor);
        registerContentObserver(context);
    }

    private Cursor getCursor(Context context) {
        return context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, null,
                null, null);
    }

    private void registerContentObserver(Context context) {
        Handler handler = new Handler(Looper.getMainLooper());

        imageObserver = new ImageObserver(handler, context);
        context.getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true, imageObserver);
    }

    private void loadData(Cursor imageCursor) {
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

    private String isLikeImage(Uri uri, String tag) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exif.getAttribute(tag);
    }

    public class ImageObserver extends BaseImageObserver {

        private ArrayList<ImageRepositoryObserver> observers = new ArrayList<>();

        public void addImageUrlsRepositoryObserver(ImageRepositoryObserver repositoryObserver) {
            if (!observers.contains(repositoryObserver)) {
                observers.add(repositoryObserver);
            }
        }

        public void removeImageUrlsRepositoryObserver(ImageRepositoryObserver repositoryObserver) {
            observers.remove(repositoryObserver);
        }

        ImageObserver(Handler handler, Context context) {
            super(handler, context);
        }

        @Override
        public void loadData(Cursor cursor) {
            ImageRepository.this.loadData(cursor);
        }

        @Override
        public Cursor getCursor(Context context) {
            return ImageRepository.this.getCursor(context);
        }

        @Override
        public void notifyImage() {
            for (int i = 0; i < observers.size(); i++) {
                observers.get(i).onUpdateImage(imageModelUrls);
            }
        }
    }
}
