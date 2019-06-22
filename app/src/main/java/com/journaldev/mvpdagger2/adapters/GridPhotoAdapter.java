package com.journaldev.mvpdagger2.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.activity.ViewImages.view.ViewImagesActivity;
import com.journaldev.mvpdagger2.fragments.ViewAllImagesByDate.ItemDate;
import com.journaldev.mvpdagger2.utils.MyImageView;

import java.util.ArrayList;

public class GridPhotoAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ItemDate> objects;
    int idImage = 0;

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
            idImage = 0;
            for (int i = 0; i < photo.length; i++) {
                createImageView(grid, photo[i], lInflater);
            }
        }
        return view;
    }

    View.OnClickListener toActivity = new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(ctx, ViewImagesActivity.class);
            int viewIdImage = Integer.parseInt(view.getTag().toString());
            intent.putExtra("idImage", viewIdImage);
            ctx.startActivity(intent);
        }
    };

    private void createImageView(GridLayout field, Uri photoUri, LayoutInflater inflate) {
        MyImageView imageView = new MyImageView(ctx);
        imageView.setTag(idImage);
        imageView.setOnClickListener(toActivity);
        int columnwidth = getColumnWidth(field);
        imageView.setPadding(3, 3, 3, 3);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(columnwidth, columnwidth);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageUrl(photoUri);
        field.addView(imageView);
        idImage++;
    }

    private int getScreenWidth() {
        Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getWidth();
    }

    private int getColumnWidth(GridLayout field) {
        return getScreenWidth() / field.getColumnCount();
    }


    // товар по позиции
    ItemDate getProduct(int position) {
        return ((ItemDate) getItem(position));
    }

}

