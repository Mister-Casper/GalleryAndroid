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
        loadUrl(imageCursor);
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

    private void loadUrl(Cursor imageCursor) {
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

    public class ImageObserver extends ContentObserver {

        private ArrayList<ImageRepositoryObserver> observers = new ArrayList<>();
        private Context context;
        private Timer waitingTimer;
        private final Handler handler;

        public void addImageUrlsRepositoryObserver(ImageRepositoryObserver repositoryObserver) {
            if (!observers.contains(repositoryObserver)) {
                observers.add(repositoryObserver);
            }
        }

        public void removeImageUrlsRepositoryObserver(ImageRepositoryObserver repositoryObserver) {
            observers.remove(repositoryObserver);
        }

        public void notifyImage() {
            for (int i = 0; i < observers.size(); i++) {
                observers.get(i).onUpdateImage(imageModelUrls);
            }
        }

        ImageObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
            this.handler = handler;
        }

        @Override
        public boolean deliverSelfNotifications() {
            return false;
        }

        @Override
        public void onChange(boolean arg0) {
            super.onChange(arg0);
            sendDelayAction();
        }

        private void sendDelayAction() {

            if (waitingTimer != null) {
                waitingTimer.cancel();
                waitingTimer = null;
            }

            waitingTimer = new Timer();

            final TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        Cursor cursor = getCursor(context);
                        loadUrl(cursor);
                        notifyImage();
                    });
                }
            };

            waitingTimer.schedule(timerTask, 250);
        }

        private void runOnUiThread(Runnable r) {
            handler.post(r);
        }
    }

}
