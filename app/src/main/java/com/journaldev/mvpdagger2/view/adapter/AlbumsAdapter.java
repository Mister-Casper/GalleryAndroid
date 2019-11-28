package com.journaldev.mvpdagger2.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.journaldev.mvpdagger2.utils.AppPreferenceUtils;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.model.AlbumModel;
import com.journaldev.mvpdagger2.view.customView.SquareImageView;
import com.journaldev.mvpdagger2.utils.GlideUtils;

import java.io.File;
import java.util.ArrayList;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {

    private ArrayList<AlbumModel> albums;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public AlbumsAdapter(Context context, ArrayList<AlbumModel> albums) {
        this.mInflater = LayoutInflater.from(context);
        this.albums = albums;
    }

    public void setAlbums(ArrayList<AlbumModel> albums){
        this.albums = albums;
        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public AlbumsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.albumitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumsAdapter.ViewHolder holder, int position) {
        Uri albumPhoto = albums.get(position).getUri().get(0);
        String AlbumName = albums.get(position).getName();
        File file = new File(String.valueOf(albumPhoto));
        Uri uri = Uri.fromFile(file);

        RequestOptions options = new RequestOptions();

        if (!AppPreferenceUtils.getIsCache())
            options = GlideUtils.optionsCleanCache(options);

        options = options.fitCenter();
        options = options.centerCrop();
        options =options.placeholder(R.drawable.placeholder);

        Glide.with(mInflater.getContext())
                .load(uri)
                .apply(options)
                .into(holder.image);

        holder.albumName.setText(AlbumName);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return albums.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        SquareImageView image;
        TextView albumName;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.picture);
            albumName = itemView.findViewById(R.id.text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}