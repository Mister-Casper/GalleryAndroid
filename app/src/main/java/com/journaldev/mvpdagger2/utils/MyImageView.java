package com.journaldev.mvpdagger2.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

public class MyImageView extends android.support.v7.widget.AppCompatImageView {

    private Uri placeholder;
    private Bitmap image;

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPlaceholderImage(Uri url) {
        placeholder = url;
        if (image == null) {
            setImageUrl(placeholder);
        }
    }

    public void setImageUrl(Uri url) {
        DownloadTask task = new DownloadTask();
        task.execute(String.valueOf(url));
    }

    private class DownloadTask extends AsyncTask<String, Void, Bitmap> {

        Context context;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            context = getContext().getApplicationContext();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String photoUri = params[0];
            try {
                File file = new File(String.valueOf(photoUri));
                Uri uri = Uri.fromFile(file);

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap decoded = Bitmap.createScaledBitmap(bitmap,200,200,true);
                return decoded;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            image = result;
            if (image != null) {
                setImageBitmap(image);
            }
        }
    }
}