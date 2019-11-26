package com.journaldev.mvpdagger2.view.fragment;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
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

import com.journaldev.mvpdagger2.data.AppPreference;
import com.journaldev.mvpdagger2.data.ImageUrls;
import com.journaldev.mvpdagger2.data.Image;
import com.journaldev.mvpdagger2.view.customView.SelectableImage;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.utils.ImageUtils;
import com.journaldev.mvpdagger2.view.activity.MainActivity;
import com.journaldev.mvpdagger2.view.activity.ViewImagesActivity;
import com.journaldev.mvpdagger2.view.adapter.ImagesAdapter;
import com.journaldev.mvpdagger2.view.adapter.ImagesAdapter.SelectableViewHolder;

import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GridImages extends Fragment implements SelectableViewHolder.OnItemClickListener, SelectableViewHolder.OnItemSelectedListener, MainActivity.OnBackPressedListener, ImageUtils.alertDialogListener, PopupMenu.OnMenuItemClickListener {


    @BindView(R.id.DataList)
    RecyclerView DataList;
    Unbinder unbinder;
    LinkedList<Image> uri = null;
    @BindView(R.id.textView)
    TextView textView;
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
    ImagesAdapter adapter;
    @BindView(R.id.showMenuButton)
    Button showMenuButton;

    private ArrayList<SelectableImage> selectedItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        setAdapter();
        if (unbinder != null) {
            viewStandartMod();
        }
    }

    private void viewStandartMod() {
        selectablemenu.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
    }

    private void setAdapter() {
        adapter = new ImagesAdapter(getActivity().getApplicationContext(), loadUri(), this);
        adapter.setClickListener(this);
        DataList.setAdapter(adapter);
        DataList.invalidate();
    }

    private ArrayList<Image> loadUri() {
        uri = ImageUrls.getUrls(getContext());
        ArrayList<Image> photo = new ArrayList<>();
        for (int i = 0; i < uri.size(); i++)
            photo.add(new Image(uri.get(i).getPhoto(), uri.get(i).getLike()));
        return photo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewallimagesbydate, container, false);
        unbinder = ButterKnife.bind(this, view);
        viewStandartMod();
        setAdapter();
        DataList.setLayoutManager(new GridLayoutManager(getContext(), 4));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), ViewImagesActivity.class);
        intent.putExtra("idImage", position);
        if (AppPreference.getIsAnim()) {
            String name = view.getTransitionName();
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(getActivity(), view,
                            name);
            startActivity(intent, options.toBundle());
        } else
            startActivity(intent);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onItemSelected(SelectableImage item) {
        selectedItems = adapter.getSelectedItems();

        if (!adapter.isSelectable())
            viewStandartMod();
        else {
            selectablemenu.setVisibility(View.VISIBLE);
            itemSelected.setText(Integer.toString(selectedItems.size()));
            textView.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.exitButton)
    public void exitButtonClick() {
        adapter.setSelectable(false);
        viewStandartMod();
    }

    @OnClick(R.id.deleteItemsSelected)
    public void deleteItemsSelectedClick() {
        AlertDialog.Builder dialog;

        if (selectedItems.size() != 0) {
            dialog = ImageUtils.createAlertDialogDeleteImage(
                    getActivity()
                    , "Вы действительно хотите удалить изображения?"
                    , this);
        } else {
            dialog = createAlertDialogNoHaveDeleteImage(
                    getActivity()
                    , "Выберите изображения , которые хотите удалить");
        }

        dialog.show();
    }

    @OnClick(R.id.shareButton)
    public void shareButtonClick() {
     ImageUtils.shareImages(getContext(),getAllFilePath(selectedItems));
    }

    private ArrayList<Uri> getAllFilePath(ArrayList<SelectableImage> selectedItems) {
        ArrayList<Uri> files = new ArrayList<>();

        for (int i = 0; i < selectedItems.size(); i++) {
            files.add(ImageUtils.getGlobalPath(getContext(),selectedItems.get(i).getPhoto().toString()));
        }

        return files;
    }

    @Override
    public void onBackPressed() {
        if (adapter.isSelectable()) {
            adapter.setSelectable(false);
            viewStandartMod();
        } else
            getActivity().finish();
    }

    @Override
    public void deleteClick() {
        selectedItems = adapter.getSelectedItems();
        ImageUtils.deleteImage(getActivity().getContentResolver(), selectedItems);
        viewStandartMod();
        removeSelectedItems();
        adapter.setSelectable(false);
    }

    public static AlertDialog.Builder createAlertDialogNoHaveDeleteImage(final Context context, String message) {
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setMessage(message);
        ad.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        return ad;
    }

    private void removeSelectedItems() {
        for (int i = 0; i < selectedItems.size(); i++) {
            for (int q = 0; q < uri.size(); q++) {
                if (selectedItems.get(i).getPhoto().equals(uri.get(q).getPhoto())) {
                    adapter.removeItem(q);
                    uri.remove(q);
                }
            }
        }
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
                adapter.setItemsSelectable(true);
                return true;
            case R.id.offSelectAll:
                adapter.setItemsSelectable(false);
                return true;
        }
        return false;
    }
}