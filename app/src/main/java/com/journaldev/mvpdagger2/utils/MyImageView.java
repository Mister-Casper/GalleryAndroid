package com.journaldev.mvpdagger2.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.journaldev.mvpdagger2.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
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
                Bitmap miniature = getThumbnail(context.getContentResolver(),photoUri);
                return miniature;
            } catch (Exception e) {
                return null;
            }
        }

        public Bitmap getThumbnail(ContentResolver cr, String path) throws Exception {
            Cursor ca = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.MediaColumns._ID }, MediaStore.MediaColumns.DATA + "=?", new String[] {path}, null);
            if (ca != null && ca.moveToFirst()) {
                int id = ca.getInt(ca.getColumnIndex(MediaStore.MediaColumns._ID));
                ca.close();
                return MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MICRO_KIND, null );
            }
            ca.close();
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                setImageBitmap(result);
            }
        }
    }
}