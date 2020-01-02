package com.journaldev.mvpdagger2.view.fragment.ImagesFragment.gridImages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.model.AlbumModel;
import com.journaldev.mvpdagger2.model.ImageModel;
import com.journaldev.mvpdagger2.view.adapter.selectableAdapter.AlbumsAdapter;
import com.journaldev.mvpdagger2.view.adapter.selectableAdapter.SelectableAdapter;
import com.journaldev.mvpdagger2.view.adapter.selectableAdapter.SelectableViewHolder;

import java.util.ArrayList;

public class GridGetAlbumsFragment extends GridAlbumsFragment implements SelectableViewHolder.OnItemClickListener, SelectableViewHolder.OnItemSelectedListener {

    public interface GetAlbumListener {
        void albumSelect(AlbumModel album, boolean isCreateAlbum);
    }

    private GetAlbumListener getAlbumListener;
    private AlbumsAdapter albumsAdapter;
    private ArrayList<AlbumModel> gridAlbums;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private AlbumModel addDrawableAlbum() {
        AlbumModel album = new AlbumModel(getString(R.string.create_album),
                new ArrayList<ImageModel>() {{
                    add(new ImageModel(Uri.parse("STUB!!")));
                }});
        return album;
    }

    @Override
    protected void initRecyclerView(ArrayList<AlbumModel> albums) {
        this.gridAlbums = new ArrayList<>(albums);
        gridAlbums.add(addDrawableAlbum());
        albumsAdapter = new AlbumsAdapter(getContext(), gridAlbums);
        albumsAdapter.setSelectableMode(false);
        albumsAdapter.setClickListener(this);
        field.setLayoutManager(new GridLayoutManager(getContext(), 2));
        field.setAdapter(albumsAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        getAlbumListener.albumSelect(gridAlbums.get(position), position == gridAlbums.size() - 1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getAlbumListener = (GetAlbumListener) context;
    }

    @Override
    protected SelectableAdapter getAdapter() {
        return albumsAdapter;
    }
}
