package com.journaldev.mvpdagger2.myVIew;

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
import com.journaldev.mvpdagger2.utils.FabricEvents;
import com.journaldev.mvpdagger2.utils.MeasurementLaunchTime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class zoomImageView extends ImageViewTouch {

    public zoomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public zoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


}