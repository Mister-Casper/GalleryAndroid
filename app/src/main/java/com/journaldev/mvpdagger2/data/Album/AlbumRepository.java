package com.journaldev.mvpdagger2.data.Album;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;

import androidx.exifinterface.media.ExifInterface;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;

import com.journaldev.mvpdagger2.data.BaseImageObserver;
import com.journaldev.mvpdagger2.model.AlbumModel;
import com.journaldev.mvpdagger2.model.Converter.AlbumModelConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class AlbumRepository {
    private ArrayList<AlbumModel> imageAlbums;
    private AlbumObserver albumObserver;

    private static final String[] PROJECTION_BUCKET = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
    };

    public ArrayList<AlbumModel> getAllAlbum() {
        return imageAlbums;
    }

    public AlbumObserver getAlbumObserver() {
        return albumObserver;
    }

    public AlbumRepository(Context context) {
        Cursor cursor = getCursor(context);
        readAlbumsFromCursor(cursor);

        registerContentObserver(context);
    }

    private Cursor getCursor(Context context) {
        return context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION_BUCKET,
                null, null, null);
    }

    private void registerContentObserver(Context context) {
        Handler handler = new Handler(Looper.getMainLooper());

        albumObserver = new AlbumRepository.AlbumObserver(handler, context);
        context.getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true, albumObserver);
    }

    private void readAlbumsFromCursor(Cursor cursor) {
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

    private void loadAlbum(Uri uri, String albumName) {
        int albumID = getAlbumIdByName(albumName);
        AlbumModel album = imageAlbums.get(albumID);
        String isLikeImage = isLikeImage(uri, ExifInterface.TAG_USER_COMMENT);
        album.getUri().add(uri);
        album.getLike().add(isLikeImage);
    }

    private int createAlbum(String albumName) {
        imageAlbums.add(new AlbumModel(albumName));
        return imageAlbums.size() - 1;
    }

    private int getAlbumIdByName(String albumName) {
        for (int i = 0; i < imageAlbums.size(); i++) {
            if (imageAlbums.get(i).getName().equals(albumName))
                return i;
        }

        return createAlbum(albumName);
    }

    private String isLikeImage(Uri uri, String tag) {
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

    public class AlbumObserver extends BaseImageObserver {

        private ArrayList<AlbumRepositoryObserver> observers = new ArrayList<>();

        public void addImageUrlsRepositoryObserver(AlbumRepositoryObserver repositoryObserver) {
            if (!observers.contains(repositoryObserver)) {
                observers.add(repositoryObserver);
            }
        }

        public void removeImageUrlsRepositoryObserver(AlbumRepositoryObserver repositoryObserver) {
            observers.remove(repositoryObserver);
        }

        AlbumObserver(Handler handler, Context context) {
            super(handler, context);
        }

        @Override
        public void loadData(Cursor cursor) {
            readAlbumsFromCursor(cursor);
        }

        @Override
        public Cursor getCursor(Context context) {
            return AlbumRepository.this.getCursor(context);
        }

        @Override
        public void notifyImage() {
            for (int i = 0; i < observers.size(); i++) {
                observers.get(i).onUpdateAlbum(imageAlbums);
            }
        }
    }
}

