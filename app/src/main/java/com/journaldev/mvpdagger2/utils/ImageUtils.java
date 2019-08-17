package com.journaldev.mvpdagger2.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;

import com.journaldev.mvpdagger2.Data.ImageUrls;
import com.journaldev.mvpdagger2.Data.ItemPhotoData;
import com.journaldev.mvpdagger2.Data.SelectableItemPhotoData;

import java.util.ArrayList;
import java.util.LinkedList;

public class ImageUtils {

    public static void deleteImage(ContentResolver contentResolver, Uri uri) {
        contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.ImageColumns.DATA + "=?", new String[]{uri.toString()});
    }

    public static void deleteImage(ContentResolver contentResolver,ArrayList<SelectableItemPhotoData>  uri) {
        String[] uriStr = convertToStringArray(uri);

        for(int i = 0 ; i < uriStr.length ; i++) {
            contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    MediaStore.Images.ImageColumns.DATA + "=?", new String[]{uriStr[i]});
        }
    }

    private static String[] convertToStringArray(ArrayList<SelectableItemPhotoData> uri) {
        String[] str = new String[uri.size()];

        for (int i = 0; i < str.length; i++) {
            str[i] = uri.get(i).getPhoto().toString();
        }

        return str;
    }

    public static AlertDialog.Builder createAlertDialogDeleteImage(final Context context , String message, final alertDialogListener listener) {
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

    public interface alertDialogListener{
        void deleteClick();
    }

}
