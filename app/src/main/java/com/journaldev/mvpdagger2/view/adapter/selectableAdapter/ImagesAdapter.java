package com.journaldev.mvpdagger2.view.adapter.selectableAdapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.journaldev.mvpdagger2.model.ImageModel;
import com.journaldev.mvpdagger2.model.Selectable.ImageSelectable;
import com.journaldev.mvpdagger2.model.Selectable.ImageSelectableModel;
import com.journaldev.mvpdagger2.R;
import java.io.File;
import java.util.ArrayList;

public class ImagesAdapter extends BaseSelectableAdapter {

    public ImagesAdapter(Context context, ArrayList<ImageModel> items){
        this.mInflater = LayoutInflater.from(context);
        for (int i = 0; i < items.size(); i++) {
            super.items.add(new ImageSelectableModel(items.get(i)));
        }
    }

    @Override
    public SelectableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.imageitem, parent, false);
        return new SelectableViewHolder(itemView, selectedItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectableViewHolder holder, final int position) {
        super.onBindViewHolder(holder,position);
        Uri imageUri = ((ImageSelectable)items.get(position)).getImage();
        File file = new File(String.valueOf(imageUri));
        Uri uri = Uri.fromFile(file);
        buttonLikeVisibility(holder, position);
        viewImage(holder, uri);
        setTransitionName(holder, position);
    }

    private void viewImage(SelectableViewHolder holder, Uri uri) {
        showUri(holder,uri);
        holder.image.setPadding(3, 3, 3, 3);
    }

    private void buttonLikeVisibility(SelectableViewHolder holder, int position) {
        if (((ImageSelectable)items.get(position)).getLike())
            holder.like.setVisibility(View.VISIBLE);
        else
            holder.like.setVisibility(View.GONE);
    }
}