package com.journaldev.mvpdagger2.view.fragment.ImagesFragment;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.model.ImageModel;
import com.journaldev.mvpdagger2.model.Selectable;
import com.journaldev.mvpdagger2.utils.ImageUtils;
import com.journaldev.mvpdagger2.view.activity.MainActivity;
import com.journaldev.mvpdagger2.view.adapter.SelectableAdapter;

import java.util.ArrayList;

import butterknife.OnClick;

abstract public class BaseSelectableFragment extends Fragment implements MainActivity.OnBackPressedListener, ImageUtils.alertDialogListener, PopupMenu.OnMenuItemClickListener {

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
        ImageUtils.deleteImage(getActivity().getContentResolver(), getSelectedItems());
        showStartInstrumentsMenu();
        removeSelectedItems();
        getAdapter().setSelectable(false);
    }

    public static AlertDialog.Builder createErrorAlertDialog(final Context context, String message) {
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setMessage(message);
        ad.setPositiveButton("Ок", null);
        return ad;
    }

    private void removeSelectedItems() {
        for (int i = 0; i < getSelectedItems().size(); i++) {
            getImages().remove(getSelectedItems().get(i));
        }
        getAdapter().notifyDataSetChanged();
    }

    @OnClick(R.id.shareButton)
    public void shareButtonClick() {
        ImageUtils.shareImages(getContext(), getAllFilePath(getSelectedItems()));
    }

    private ArrayList<Uri> getAllFilePath(ArrayList<Selectable> selectedItems) {
        ArrayList<Uri> files = new ArrayList<>();

        for (int i = 0; i < selectedItems.size(); i++) {
            ArrayList<ImageModel> images = selectedItems.get(i).getImages();
            for (int q = 0; q < images.size(); q++) {
                Uri imageUri = images.get(q).getImage();
                files.add(ImageUtils.getGlobalPath(getContext(), imageUri.toString()));
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
        AlertDialog.Builder dialog;

        if (getSelectedItems().size() != 0) {
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

    abstract ArrayList<ImageModel> getImages();
    abstract ArrayList<Selectable> getSelectedItems();
    abstract void setSelectedItems(ArrayList<Selectable> selectedItems);
    abstract void showStartInstrumentsMenu();
    abstract SelectableAdapter getAdapter();
    abstract int getMenu();

}
