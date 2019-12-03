package com.journaldev.mvpdagger2.data.Album;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;

import androidx.exifinterface.media.ExifInterface;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;

import com.journaldev.mvpdagger2.data.Image.ImageRepository;
import com.journaldev.mvpdagger2.data.Image.ImageRepositoryObserver;
import com.journaldev.mvpdagger2.model.AlbumModel;

import java.io.IOException;
import java.util.ArrayList;


public class AlbumRepository {
    private static ArrayList<AlbumModel> imageAlbums;

    private static final String[] PROJECTION_BUCKET = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
    };

    public static ArrayList<AlbumModel> getAllAlbum(Context context) {
        if (imageAlbums == null) {
            readAlbums(context);
        }
        return imageAlbums;
    }

    private static void readAlbums(Context context) {
        Cursor cursor = getCursor(context);
        readAlbumsFromCursor(cursor);

        registerContentObserver(context);
    }

    private static Cursor getCursor(Context context) {
        return context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION_BUCKET,
                null, null, null);
    }

    private static void registerContentObserver(Context context) {
        Handler handler = new Handler(Looper.getMainLooper());

        context.getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true, new AlbumRepository.AlbumObserver(handler, context));
    }

    private static void readAlbumsFromCursor(Cursor cursor) {
        if (cursor != null) {
            imageAlbums = new ArrayList<>();

            int bucketColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int dateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

            for (int i = cursor.getCount()-1; i >= 0; i--) {
                cursor.moveToPosition(i);
                String albumName = cursor.getString(bucketColumn);
                String albumUri = cursor.getString(dateColumn);
                Uri uri = Uri.parse(albumUri);
                loadAlbum(uri, albumName);
            }
        }
    }

    private static void loadAlbum(Uri uri, String albumName) {
        int albumID = getAlbumIdByName(albumName);
        AlbumModel album = imageAlbums.get(albumID);
        String isLikeImage = isLikeImage(uri, ExifInterface.TAG_USER_COMMENT);
        album.getUri().add(uri);
        album.getLike().add(isLikeImage);
    }

    private static int createAlbum(String albumName) {
        imageAlbums.add(new AlbumModel(albumName));
        return imageAlbums.size() - 1;
    }

    private static int getAlbumIdByName(String albumName) {
        for (int i = 0; i < imageAlbums.size(); i++) {
            if (imageAlbums.get(i).getName().equals(albumName))
                return i;
        }

        return createAlbum(albumName);
    }

    private static String isLikeImage(Uri uri, String tag) {
        ExifInterface exif;
        try {
            exif = new ExifInterface(uri.toString());
            String attribute = exif.getAttribute(tag);

            if (attribute != null)
                return attribute;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "false";
    }

    public static class AlbumObserver extends ContentObserver {

        private static ArrayList<AlbumRepositoryObserver> observers = new ArrayList<>();

        public static void addImageUrlsRepositoryObserver(AlbumRepositoryObserver repositoryObserver) {
            if (!observers.contains(repositoryObserver)) {
                observers.add(repositoryObserver);
            }
        }

        public static void removeImageUrlsRepositoryObserver(AlbumRepositoryObserver repositoryObserver) {
            observers.remove(repositoryObserver);
        }

        Context context;

        public AlbumObserver(Handler handler,Context context) {
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
            readAlbumsFromCursor(cursor);
            for (int i = 0; i < observers.size(); i++) {
                observers.get(i).onUpdateAlbum(imageAlbums);
            }
        }
    }
}

