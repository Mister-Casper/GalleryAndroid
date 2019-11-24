package com.journaldev.mvpdagger2.data;

import android.content.Context;
import android.database.Cursor;
import androidx.exifinterface.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;
import java.util.ArrayList;


public class AlbumsInfo {
    private static ArrayList<Album> imageAlbums = new ArrayList<>();

    private static final String[] PROJECTION_BUCKET = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
    };

    public static ArrayList<Album> getAllAlbum(Context context) {
        if(imageAlbums.size() != 0) {
            return imageAlbums;
        }else {
            readAlbums(context);
            return imageAlbums;
        }
    }

    private static void readAlbums(Context context) {
        final Uri mBaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(
                mBaseUri, PROJECTION_BUCKET, null, null, null);

        readAlbumsFromCursor(cursor);
    }

    private static void readAlbumsFromCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.moveToFirst();

            int bucketColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int dateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

            for (int i = cursor.getCount() - 1; i >= 1; i--) {
                cursor.moveToPosition(i);
                String albumName = cursor.getString(bucketColumn);
                String albumUri = cursor.getString(dateColumn);
                Uri uri = Uri.parse(albumUri);
                loadAlbums(uri, albumName);
            }
        }
    }

    private static void loadAlbums(Uri uri, String albumName) {
        int albumID = getAlbumIdByName(albumName);
        Album album = imageAlbums.get(albumID);
        String isLikeImage = isLikeImage(uri, ExifInterface.TAG_USER_COMMENT);
        album.getUri().add(uri);
        album.getLike().add(isLikeImage);
    }

    private static int createAlbum(String albumName) {
        imageAlbums.add(new Album(albumName));
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
}

