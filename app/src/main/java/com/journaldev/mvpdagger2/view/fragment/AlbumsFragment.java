package com.journaldev.mvpdagger2.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.data.Album.AlbumRepository;
import com.journaldev.mvpdagger2.data.Album.AlbumRepositoryObserver;
import com.journaldev.mvpdagger2.model.AlbumModel;
import com.journaldev.mvpdagger2.model.ImageModel;
import com.journaldev.mvpdagger2.model.SelectableAlbumModel;
import com.journaldev.mvpdagger2.model.SelectableImageModel;
import com.journaldev.mvpdagger2.utils.ImageUtils;
import com.journaldev.mvpdagger2.view.activity.MainActivity;
import com.journaldev.mvpdagger2.view.activity.ViewImagesGridActivity;
import com.journaldev.mvpdagger2.view.adapter.AlbumsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.journaldev.mvpdagger2.view.fragment.BaseGridImagesFragment.createErrorAlertDialog;


public class AlbumsFragment extends Fragment implements AlbumsAdapter.SelectableViewHolder.OnItemClickListener, AlbumsAdapter.SelectableViewHolder.OnItemSelectedListener, AlbumRepositoryObserver, PopupMenu.OnMenuItemClickListener, ImageUtils.alertDialogListener, MainActivity.OnBackPressedListener {

    @BindView(R.id.field)
    RecyclerView field;
    Unbinder unbinder;

    private ArrayList<AlbumModel> albums;
    private AlbumsAdapter albumsAdapter;
    private ArrayList<SelectableAlbumModel> selectedItems;

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
    public void onStart() {
        super.onStart();
        albums = AlbumRepository.getAllAlbum(getContext());
        setAdapter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlbumRepository.AlbumObserver.addImageUrlsRepositoryObserver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AlbumRepository.AlbumObserver.removeImageUrlsRepositoryObserver(this);
    }

    private void setAdapter() {
        field.setLayoutManager(new GridLayoutManager(getContext(), 2));
        albumsAdapter = new AlbumsAdapter(getActivity().getApplicationContext(), albums);
        albumsAdapter.setSelectedItemClickListener(this);
        albumsAdapter.setClickListener(this);
        field.setAdapter(albumsAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private ArrayList<ImageModel> getImages(int position) {
        ArrayList<ImageModel> images = new ArrayList<>();

        ArrayList<String> uri = convertArraysUrlToArraysString(position);
        ArrayList<String> like = convertArraysLikeToArraysString(position);

        for (int i = 0; i < uri.size(); i++) {
            images.add(new ImageModel(Uri.parse(uri.get(i)), Boolean.parseBoolean(like.get(i))));
        }

        return images;
    }

    private ArrayList<String> convertArraysUrlToArraysString(int id) {
        ArrayList<Uri> uri = albums.get(id).getUri();
        ArrayList<String> strUri = new ArrayList<>();
        for (int i = 0; i < uri.size(); i++)
            strUri.add(uri.get(i).toString());
        return strUri;
    }

    private ArrayList<String> convertArraysLikeToArraysString(int id) {
        return albums.get(id).getLike();
    }

    @Override
    public void onUpdateAlbum(ArrayList<AlbumModel> updateAlbums) {
        this.albums = updateAlbums;
        albumsAdapter.setAlbums(updateAlbums);
    }

    @Override
    public void onItemSelected(SelectableAlbumModel item) {
        selectedItems = albumsAdapter.getSelectedItems();

        if (!albumsAdapter.isSelectable())
            showStartInstrumentsMenu();
        else {
            showSelectableInstrumentsMenu();
        }
    }

    private void showStartInstrumentsMenu() {
        selectablemenu.setVisibility(View.GONE);
    }

    private void showSelectableInstrumentsMenu() {
        selectablemenu.setVisibility(View.VISIBLE);
        itemSelected.setText(Integer.toString(selectedItems.size()));
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), ViewImagesGridActivity.class);
        intent.putParcelableArrayListExtra("image", getImages(position));
        intent.putExtra("albumName", albums.get(position).getName());
        getContext().startActivity(intent);
    }

    @Override
    public void deleteClick() {
        selectedItems = albumsAdapter.getSelectedItems();
        ImageUtils.deleteAlbums(getActivity().getContentResolver(), selectedItems);
        showStartInstrumentsMenu();
        removeSelectedItems();
        albumsAdapter.setSelectable(false);
    }

    private void removeSelectedItems() {
        for (int i = 0; i < selectedItems.size(); i++) {
            albums.remove(selectedItems.get(i));
        }
        albumsAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.showMenuButton)
    public void showMenuButtonClick(View view) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.selectable_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.selectAll:
                albumsAdapter.setItemsSelectable(true);
                return true;
            case R.id.offSelectAll:
                albumsAdapter.setItemsSelectable(false);
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (albumsAdapter.isSelectable()) {
            albumsAdapter.setSelectable(false);
            showStartInstrumentsMenu();
        } else
            getActivity().finish();
    }

    @OnClick(R.id.shareButton)
    public void shareButtonClick() {
        ImageUtils.shareImages(getContext(), getAllFilePath(selectedItems));
    }

    private ArrayList<Uri> getAllFilePath(ArrayList<SelectableAlbumModel> selectedItems) {
        ArrayList<Uri> files = new ArrayList<>();

        for (int i = 0; i < selectedItems.size(); i++) {
            ArrayList<ImageModel> images= selectedItems.get(i).getImages();
            for (int q = 0; q < images.size(); q++) {
                files.add(ImageUtils.getGlobalPath(getActivity().getApplicationContext(),
                        images.get(q).getPhoto().toString()));
            }
        }

        return files;
    }

    @OnClick(R.id.deleteItemsSelected)
    public void deleteItemsSelectedClick() {
        AlertDialog.Builder dialog;

        if (selectedItems.size() != 0) {
            dialog = ImageUtils.createDeleteImageAlertDialog(
                    getActivity()
                    , "Вы действительно хотите удалить изображения?"
                    , this);
        } else {
            dialog = createErrorAlertDialog(
                    getActivity()
                    , "Выберите изображения , которые хотите удалить");
        }

        dialog.show();
    }
}
