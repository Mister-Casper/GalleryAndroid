package com.journaldev.mvpdagger2.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.journaldev.mvpdagger2.Data.AlbumsInfo;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.activity.ViewImages.view.ViewImagesActivity;
import com.journaldev.mvpdagger2.myVIew.MyImageView;
import com.journaldev.mvpdagger2.utils.SquareImageView;

import java.io.File;
import java.util.ArrayList;

public class GridAlbomsAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    AlbumsInfo.Album[] albums;
    int idImage = 0;

    public GridAlbomsAdapter(Context context, AlbumsInfo.Album[] albums) {
        ctx = context;
        this.albums = albums;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return albums.length;
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return albums[position];
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
            view = lInflater.inflate(R.layout.albumitem, parent, false);

            AlbumsInfo.Album album = getProduct(position);

            MyImageView  icon = view.findViewById(R.id.picture);
            TextView title = view.findViewById(R.id.text);
            view.setTag(idImage);
            File file = new File(String.valueOf(album.getUri().get(0)));
            Uri uri = Uri.fromFile(file);
            Glide.with(ctx).load(uri).into(icon);
            title.setText(album.getName());
            view.setPadding(20, 20, 20, 20);
            view.setOnClickListener(toActivity);
            view.setTag(idImage);
            idImage++;
        }
        return view;
    }

    View.OnClickListener toActivity = new View.OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent(ctx, ViewImagesActivity.class);
            int viewIdImage = Integer.parseInt(view.getTag().toString());
            ArrayList<Uri> uri = albums[viewIdImage].getUri();
            ArrayList<String> strUri = new ArrayList<>();
            for(int i = 0 ; i < uri.size();i++)
                strUri.add(uri.get(i).toString());
            intent.putStringArrayListExtra("uri",strUri);
            ctx.startActivity(intent);
        }
    };

    // товар по позиции
    AlbumsInfo.Album getProduct(int position) {
        return ((AlbumsInfo.Album) getItem(position));
    }

}

