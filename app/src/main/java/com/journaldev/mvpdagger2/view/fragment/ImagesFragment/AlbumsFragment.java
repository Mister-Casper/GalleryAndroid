package com.journaldev.mvpdagger2.view.fragment.ImagesFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.application.App;
import com.journaldev.mvpdagger2.data.Album.AlbumRepositoryObserver;
import com.journaldev.mvpdagger2.model.AlbumModel;
import com.journaldev.mvpdagger2.model.Converter.AlbumModelConverter;
import com.journaldev.mvpdagger2.model.ImageModel;
import com.journaldev.mvpdagger2.model.Selectable.Selectable;
import com.journaldev.mvpdagger2.view.activity.ViewImagesGridActivity;
import com.journaldev.mvpdagger2.view.adapter.selectableAdapter.AlbumsAdapter;
import com.journaldev.mvpdagger2.view.adapter.selectableAdapter.SelectableAdapter;
import com.journaldev.mvpdagger2.view.adapter.selectableAdapter.SelectableViewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AlbumsFragment extends BaseSelectableFragment implements SelectableViewHolder.OnItemClickListener, SelectableViewHolder.OnItemSelectedListener, AlbumRepositoryObserver {

    @BindView(R.id.field)
    RecyclerView field;
    Unbinder unbinder;

    private ArrayList<AlbumModel> albums;
    private AlbumsAdapter albumsAdapter;
    private ArrayList<Selectable> selectedItems;

    @BindView(R.id.exitButton)
    Button exitButton;
    @BindView(R.id.itemSelected)
    TextView itemSelected;
    @BindView(R.id.deleteItemsSelected)
    Button deleteItemsSelected;
    @BindView(R.id.shareButton)
    Button shareButton;
    @BindView(R.id.selectablemenu)
    ConstraintLayout selectablemenu;
    @BindView(R.id.showMenuButton)
    Button showMenuButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAlbumRepository().getAlbumObserver().addImageUrlsRepositoryObserver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.getAlbumRepository().getAlbumObserver().removeImageUrlsRepositoryObserver(this);
    }

    private void initRecyclerView() {
        field.setLayoutManager(new GridLayoutManager(getContext(), 2));
        albumsAdapter = new AlbumsAdapter(getContext(), albums);
        albumsAdapter.setSelectedItemClickListener(this);
        albumsAdapter.setClickListener(this);
        field.setAdapter(albumsAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        unbinder = ButterKnife.bind(this, view);
        albums =   App.getAlbumRepository().getAllAlbum();
        initRecyclerView();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private ArrayList<ImageModel> getImagesFromAlbum(int position) {
        return albums.get(position).getImages();
    }

    @Override
    public void onUpdateAlbum(ArrayList<AlbumModel> updateAlbums) {
        this.albums = updateAlbums;
        albumsAdapter.setImages(AlbumModelConverter.convertAlbumsToSelectable(updateAlbums));
    }

    @Override
    public void onItemSelected(Selectable item) {
        selectedItems = albumsAdapter.getSelectedItems();

        if (!albumsAdapter.isSelectable())
            showStartInstrumentsMenu();
        else {
            showSelectableInstrumentsMenu();
        }
    }

    private void showSelectableInstrumentsMenu() {
        selectablemenu.setVisibility(View.VISIBLE);
        itemSelected.setText(Integer.toString(selectedItems.size()));
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), ViewImagesGridActivity.class);
        intent.putParcelableArrayListExtra("image", getImagesFromAlbum(position));
        intent.putExtra("albumName", albums.get(position).getName());
        getContext().startActivity(intent);
    }

    @Override
    ArrayList<ImageModel> getImages() {
        ArrayList<ImageModel> images = new ArrayList<>();

        for (int i = 0; i < albums.size(); i++) {
            images.addAll(albums.get(i).getImages());
        }

        return images;
    }

    @Override
    ArrayList<Selectable> getSelectedItems() {
        return selectedItems;
    }

    @Override
    void setSelectedItems(ArrayList<Selectable> selectedItems) {
        this.selectedItems = selectedItems;
    }

    @Override
    public void showStartInstrumentsMenu() {
        selectablemenu.setVisibility(View.GONE);
    }

    @Override
    SelectableAdapter getAdapter() {
        return albumsAdapter;
    }

    @Override
    int getMenu() {
        return R.menu.album_selectable_menu;
    }
}
