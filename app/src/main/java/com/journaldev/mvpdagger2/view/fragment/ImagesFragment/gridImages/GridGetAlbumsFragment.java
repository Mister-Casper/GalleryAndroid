package com.journaldev.mvpdagger2.view.fragment.ImagesFragment.gridImages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.application.App;
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
    private ArrayList<AlbumModel> albums;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albums = App.getAlbumRepository().getAllAlbum();
        albums.add(addDrawableAlbum());
        albumsAdapter = new AlbumsAdapter(getContext(), albums);
        albumsAdapter.setSelectableMode(false);
    }

    private AlbumModel addDrawableAlbum() {
        AlbumModel album = new AlbumModel(getString(R.string.create_album),
                new ArrayList<ImageModel>() {{
                    add(new ImageModel(Uri.parse("STUB")));
                }});
        return album;
    }


    @Override
    public void onItemClick(View view, int position) {
        getAlbumListener.albumSelect(albums.get(position), position == albums.size() - 1);
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
