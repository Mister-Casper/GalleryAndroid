package com.journaldev.mvpdagger2.utils;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.application.App;
import com.journaldev.mvpdagger2.model.Selectable.Selectable;
import com.journaldev.mvpdagger2.view.utils.DialogsUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Singleton;

import static com.journaldev.mvpdagger2.model.Selectable.ImageSelectableModel.convertImagesToStringArray;

@Singleton
public class ImageHelper {

    static Context context = App.getApp();
    static ContentResolver contentResolver = App.getApp().getContentResolver();

    public static void deleteImage(Uri uri) {
        contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.ImageColumns.DATA + "=?", new String[]{uri.toString()});
    }

    public static void deleteImage(ArrayList<Selectable> uri) {
        String[] uriStr = convertImagesToStringArray(uri);

        for (int i = 0; i < uriStr.length; i++) {
            contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    MediaStore.Images.ImageColumns.DATA + "=?", new String[]{uriStr[i]});
        }
    }

    public static void createDeleteImageAlertDialog(Activity activity, String message, final alertDialogListener listener) {
        AlertDialog createAlbumDialog = new AlertDialog.Builder(activity)
                .setTitle(message).create();
        View createAlbumView = DialogsUtils.setViewToDialog(activity, createAlbumDialog, R.layout.delete_images_dialog);

        createAlbumView.findViewById(R.id.ok).setOnClickListener(view -> {
            listener.deleteClick();
            createAlbumDialog.cancel();
        });

        createAlbumView.findViewById(R.id.cancel).setOnClickListener(view ->
                createAlbumDialog.cancel());

        createAlbumDialog.show();
    }

    public static void shareImages(ArrayList<Uri> urls) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        sendIntent.setType("image/*");
        sendIntent.putExtra(Intent.EXTRA_STREAM, urls);
        context.startActivity(sendIntent);
    }

    public static Uri getGlobalPath(String path) {
        File file = new File(path);
        Uri imageUri = FileProvider.getUriForFile(
                context,
                "com.journaldev.mvpdagger2.provider",
                file);
        return imageUri;
    }

    public static void setWallpaper(Bitmap Wallpaper) {
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(context);
        try {
            myWallpaperManager.setBitmap(Wallpaper);
            Toast.makeText(context, context.getString(R.string.setting_wallpaper_successful), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, context.getString(R.string.setting_wallpaper_fail), Toast.LENGTH_LONG).show();
        }
    }

    public static Bitmap convertUriToBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            Uri imageUri = Uri.fromFile(new File(Objects.requireNonNull(uri.getPath())));
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static String getFileName(Uri uri) {
        String result;
        result = uri.getPath();
        int cut = result.lastIndexOf('/');
        if (cut != -1) {
            result = result.substring(cut + 1);
        }
        return result;
    }

    public interface alertDialogListener {
        void deleteClick();
    }

}
