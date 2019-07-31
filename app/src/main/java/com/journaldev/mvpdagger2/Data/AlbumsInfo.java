package com.journaldev.mvpdagger2.Data;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SymbolTable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class AlbumsInfo {
    static LinkedHashSet<String> albumsName = new LinkedHashSet<>();
    static ArrayList<ArrayList<Uri>> albumsUri = new ArrayList<>();

    static int albumId = -1;

    private static final String[] PROJECTION_BUCKET = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
    };

    public static Album[] getAllAlbum() {
        Album[] albums = new Album[albumsName.size()];
        int i = 0;

        for (Iterator<String> it = albumsName.iterator(); it.hasNext(); ) {
            String search = it.next();
           albums[i] = new Album(search,albumsUri.get(i));
           i++;
        }

        return albums;
    }


    private static void getAccess(Context context) {
        ActivityCompat.requestPermissions((Activity) context,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    private static boolean checkReadExternalPermission(Context context) {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


    public static void loadImageUrl(Context context) {
        albumsName = new LinkedHashSet<>();
        albumsUri = new ArrayList<>();
        albumId = -1;
        if (!checkReadExternalPermission(context)) {
            getAccess(context);
        }
        readAlboms(context);
    }

    private static void readAlboms(Context context) {
        final Uri mBaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(
                mBaseUri, PROJECTION_BUCKET, null, null, null);

        if (cursor.moveToLast()) {
            int position = cursor.getPosition();
            System.out.print(position);
            int bucketColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int dateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            while (cursor.moveToPrevious()) {
                int position1 = cursor.getPosition();
                System.out.print(position1);
                String bucket = cursor.getString(bucketColumn);
                String date = cursor.getString(dateColumn);
                Uri uri = Uri.parse(date);
                loadAlboms(uri, bucket);
            }
        }
    }

    private static void loadAlboms(Uri uri, String bucket) {
        int id = checkLinkedHaskSetEquals(albumsName, bucket);
        if (id != -1) {
            albumsUri.get(id).add(uri);
        } else {
            albumsName.add(bucket);
            albumsUri.add(new ArrayList<Uri>());
            albumId++;
            albumsUri.get(albumId).add(uri);
        }
    }

    private static int checkLinkedHaskSetEquals(LinkedHashSet<String> list, String element) {
        int id = 0;
        for (String albumName : list) {
            if (albumName.equals(element)) {
                return id;
            }
            id++;
        }
        return -1;
    }


    public static class Album {
        private String name;

        public String getName() {
            return name;
        }

        public ArrayList<Uri> getUri() {
            return uri;
        }

        private ArrayList<Uri> uri;


        Album(String name, ArrayList<Uri> uri) {
            this.name = name;
            this.uri = uri;
        }
    }

}
