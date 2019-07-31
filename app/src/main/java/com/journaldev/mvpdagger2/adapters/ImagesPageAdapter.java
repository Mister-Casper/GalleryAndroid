package com.journaldev.mvpdagger2.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.journaldev.mvpdagger2.Data.ImageUrls;
import com.journaldev.mvpdagger2.R;

import java.io.File;
import java.util.LinkedList;

public class ImagesPageAdapter extends PagerAdapter {

    LinkedList<Uri> imageUri;
    Context mContext;
    LayoutInflater mLayoutInflater;

    public ImagesPageAdapter(Context context, LinkedList<Uri> images) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageUri = images;
    }

    @Override
    public int getCount() {
        return imageUri.size();
    }

    public Uri getCurrentUri(int position)
    {
        return imageUri.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView =  itemView.findViewById(R.id.imageView);
        File file = new File(String.valueOf(imageUri.get(position)));
        Uri uri = Uri.fromFile(file);
        Glide.with(mContext).load(uri).into(imageView);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}