package com.journaldev.mvpdagger2.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.activity.ViewAllImages.view.ViewAllImages;
import com.journaldev.mvpdagger2.activity.ViewImages.view.ViewImagesActivity;
import com.journaldev.mvpdagger2.fragments.ViewAllImagesByDate.model.ItemDate;
import com.journaldev.mvpdagger2.utils.MyImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Logger;

public class GridPhotoAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ItemDate> objects;

    public GridPhotoAdapter(Context context, ArrayList<ItemDate> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.datelistitem, parent, false);


            ItemDate item = getProduct(position);

            final GridLayout grid = view.findViewById(R.id.field);
            final Uri[] photo = item.getPhoto();

                grid.removeAllViews();

            for (int i = 0; i < photo.length; i++) {
                createImageView(grid, photo[i], lInflater);
            }
        }
        return view;
    }


    private void createImageView(GridLayout field, Uri photoUri, LayoutInflater inflate) {
        MyImageView imageView = new MyImageView(ctx);
        int columnwidth = getColumnWidth(field);
        imageView.setPadding(3,3,3,3);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(columnwidth,columnwidth);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageUrl(photoUri);
         field.addView(imageView);
    }

    private int getScreenWidth()
    {
        Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getWidth();
    }

    private int getColumnWidth(GridLayout field)
    {
        return  getScreenWidth() / field.getColumnCount();
    }


    // товар по позиции
    ItemDate getProduct(int position) {
        return ((ItemDate) getItem(position));
    }

}

