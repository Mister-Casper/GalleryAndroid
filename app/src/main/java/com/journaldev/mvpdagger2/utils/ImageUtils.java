package com.journaldev.mvpdagger2.utils;

import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
import com.journaldev.mvpdagger2.model.Selectable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static com.journaldev.mvpdagger2.model.SelectableAlbumModel.convertAlbumsToStringArray;
import static com.journaldev.mvpdagger2.model.SelectableImageModel.convertImagesToStringArray;

public class ImageUtils {

    public static void deleteImage(ContentResolver contentResolver, Uri uri) {
        contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.ImageColumns.DATA + "=?", new String[]{uri.toString()});
    }

    public static void deleteImage(ContentResolver contentResolver, ArrayList<Selectable> uri) {
        String[] uriStr = convertImagesToStringArray(uri);

        for (int i = 0; i < uriStr.length; i++) {
            contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    MediaStore.Images.ImageColumns.DATA + "=?", new String[]{uriStr[i]});
        }
    }

    public static AlertDialog.Builder createDeleteImageAlertDialog(final Context context, String message, final alertDialogListener listener) {
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setMessage(message);
        ad.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                listener.deleteClick();
            }
        });
        ad.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });
        return ad;
    }

    public static void shareImages(Context context, ArrayList<Uri> urls) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        sendIntent.setType("image/*");
        sendIntent.putExtra(Intent.EXTRA_STREAM, urls);
        context.startActivity(sendIntent);
    }

    public static Uri getGlobalPath(Context context, String path) {
        File file = new File(path);
        Uri imageUri = FileProvider.getUriForFile(
                context,
                "com.journaldev.mvpdagger2.provider",
                file);
        return imageUri;
    }

    public static void Wallpaper(Context context, Bitmap image) {
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(context);
        try {
            myWallpaperManager.setBitmap(image);
            Toast.makeText(context,"Обои установленны",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context,"Не удалось установить обои",Toast.LENGTH_LONG).show();
        }
    }

    public static Bitmap convertUriToBitmap(Uri uri , ContentResolver contentResolver)
    {
        Bitmap bitmap=null;
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
