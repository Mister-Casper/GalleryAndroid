package com.journaldev.mvpdagger2.view.fragment.ImagesFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.model.ImageModel;
import com.journaldev.mvpdagger2.model.Selectable;
import com.journaldev.mvpdagger2.utils.App;
import com.journaldev.mvpdagger2.utils.CreateAlbumHelper;
import com.journaldev.mvpdagger2.utils.ImageHelper;
import com.journaldev.mvpdagger2.view.Utils.DialogsUtils;
import com.journaldev.mvpdagger2.view.activity.MainActivity;
import com.journaldev.mvpdagger2.view.adapter.SelectableAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.OnClick;

abstract public class BaseSelectableFragment extends Fragment implements MainActivity.OnBackPressedListener, ImageHelper.alertDialogListener, PopupMenu.OnMenuItemClickListener, DialogsUtils.DialogsListener {

    @Inject
    ImageHelper imageHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageHelper =  App.getImageHelper();
    }

    @OnClick(R.id.showMenuButton)
    public void showMenuButtonClick(View view) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(getMenu(), popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.selectAll:
                getAdapter().setItemsSelectable(true);
                return true;
            case R.id.offSelectAll:
                getAdapter().setItemsSelectable(false);
                return true;
            case R.id.createAlbum:
                DialogsUtils.showAlbumNameDialog(getContext(), "Название альбома", this);
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (getAdapter().isSelectable()) {
            getAdapter().setSelectable(false);
            showStartInstrumentsMenu();
        } else
            getActivity().finish();
    }

    @Override
    public void deleteClick() {
        setSelectedItems(getAdapter().getSelectedItems());
        imageHelper.deleteImage(getSelectedItems());
        showStartInstrumentsMenu();
        removeSelectedItems();
        getAdapter().setSelectable(false);
    }

    public static AlertDialog createErrorAlertDialog(final Context context, String message) {
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setMessage(message);
        ad.setPositiveButton("Ок", null);
        return ad.show();
    }

    private void removeSelectedItems() {
        for (int i = 0; i < getSelectedItems().size(); i++) {
            getImages().remove(getSelectedItems().get(i));
        }
        getAdapter().notifyDataSetChanged();
    }

    @OnClick(R.id.shareButton)
    public void shareButtonClick() {
        imageHelper.shareImages(getAllFilePath(getSelectedItems()));
    }

    private ArrayList<Uri> getAllFilePath(ArrayList<Selectable> selectedItems) {
        ArrayList<Uri> files = new ArrayList<>();

        for (int i = 0; i < selectedItems.size(); i++) {
            ArrayList<ImageModel> images = selectedItems.get(i).getImages();
            for (int q = 0; q < images.size(); q++) {
                Uri imageUri = images.get(q).getImage();
                files.add(imageHelper.getGlobalPath(imageUri.toString()));
            }
        }

        return files;
    }

    @OnClick(R.id.exitButton)
    public void exitButtonClick() {
        getAdapter().setSelectable(false);
        showStartInstrumentsMenu();
    }

    @OnClick(R.id.deleteItemsSelected)
    public void deleteItemsSelectedClick() {
        if (getSelectedItems().size() != 0) {
             imageHelper.createDeleteImageAlertDialog(getActivity(),
                    "Вы действительно хотите удалить изображения?"
                    , this);
        } else {
            createErrorAlertDialog(
                    getActivity()
                    , "Выберите изображения , которые хотите удалить");
        }

    }

    @Override
    public void createAlbum(String albumName) {
        ArrayList<Uri> images = Selectable.getAll(getSelectedItems());
        CreateAlbumHelper createAlbumHelper = new CreateAlbumHelper();
        createAlbumHelper.createImageFolder(getContext(),images,albumName);
        showStartInstrumentsMenu();
        getAdapter().setSelectable(false);
    }

    abstract ArrayList<ImageModel> getImages();

    abstract ArrayList<Selectable> getSelectedItems();

    abstract void setSelectedItems(ArrayList<Selectable> selectedItems);

    abstract void showStartInstrumentsMenu();

    abstract SelectableAdapter getAdapter();

    abstract int getMenu();

}
