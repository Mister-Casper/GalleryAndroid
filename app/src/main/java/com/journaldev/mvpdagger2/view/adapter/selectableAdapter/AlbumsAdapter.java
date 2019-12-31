package com.journaldev.mvpdagger2.view.adapter.selectableAdapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.journaldev.mvpdagger2.model.AlbumModel;
import com.journaldev.mvpdagger2.model.Selectable.AlbumSelectable;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.model.Selectable.AlbumSelectableModel;
import java.io.File;
import java.util.ArrayList;

public class AlbumsAdapter extends BaseSelectableAdapter {

    public AlbumsAdapter(Context context, ArrayList<AlbumModel> items) {
        this.mInflater = LayoutInflater.from(context);
        for (int i = 0; i < items.size(); i++) {
            super.items.add(new AlbumSelectableModel(items.get(i)));
        }
    }

    @Override
    public SelectableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.albumitem, parent, false);
        return new SelectableViewHolder(view, selectedItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectableViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Uri firstImage = ((AlbumSelectable) items.get(position)).getUri(0);
        String AlbumName = ((AlbumSelectable) items.get(position)).getName();
        showFirstImageInAlbum(holder, firstImage);
        holder.albumName.setText(AlbumName);
    }

    private void showFirstImageInAlbum(SelectableViewHolder holder, Uri firstImage) {
        File file = new File(String.valueOf(firstImage));
        Uri uri = Uri.fromFile(file);
        showUri(holder, uri);
    }
}