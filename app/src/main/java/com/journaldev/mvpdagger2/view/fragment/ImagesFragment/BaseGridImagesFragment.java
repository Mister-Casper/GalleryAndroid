package com.journaldev.mvpdagger2.view.fragment.ImagesFragment;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
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
import com.journaldev.mvpdagger2.data.Image.ImageRepository;
import com.journaldev.mvpdagger2.model.ImageModel;
import com.journaldev.mvpdagger2.utils.AppPreferenceUtils;
import com.journaldev.mvpdagger2.utils.ImageUtils;
import com.journaldev.mvpdagger2.view.activity.MainActivity;
import com.journaldev.mvpdagger2.view.activity.ViewImagesActivity;
import com.journaldev.mvpdagger2.view.adapter.ImagesAdapter;
import com.journaldev.mvpdagger2.model.SelectableImageModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BaseGridImagesFragment extends Fragment implements ImagesAdapter.SelectableViewHolder.OnItemClickListener, ImagesAdapter.SelectableViewHolder.OnItemSelectedListener, MainActivity.OnBackPressedListener, ImageUtils.alertDialogListener, PopupMenu.OnMenuItemClickListener {

    ArrayList<ImageModel> images = null;
    Unbinder unbinder;
    ImagesAdapter adapter;

    @BindView(R.id.DataList)
    RecyclerView DataList;
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

    private ArrayList<SelectableImageModel> selectedItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            images = getArguments().getParcelableArrayList("image");

        if (images == null) {
            images = ImageRepository.getUrls(getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewallimages, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRecyclerView();
        return view;
    }

    private void showStartInstrumentsMenu() {
        selectablemenu.setVisibility(View.GONE);
    }

    private void showSelectableInstrumentsMenu() {
        selectablemenu.setVisibility(View.VISIBLE);
        itemSelected.setText(Integer.toString(selectedItems.size()));
    }

    private void initRecyclerView() {
        adapter = new ImagesAdapter(getActivity().getApplicationContext(),images);
        adapter.setSelectedItemClickListener(this);
        adapter.setClickListener(this);
        DataList.setAdapter(adapter);
        DataList.setLayoutManager(new GridLayoutManager(getContext(), 4));
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
        intent.putParcelableArrayListExtra("uri", images);

        if (AppPreferenceUtils.getIsAnim()) {
            String name = view.getTransitionName();
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, name);
            startActivity(intent, options.toBundle());
        } else
            startActivity(intent);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onItemSelected(SelectableImageModel item) {
        selectedItems = adapter.getSelectedItems();

        if (!adapter.isSelectable())
            showStartInstrumentsMenu();
        else {
            showSelectableInstrumentsMenu();
        }
    }


    @OnClick(R.id.exitButton)
    public void exitButtonClick() {
        adapter.setSelectable(false);
        showStartInstrumentsMenu();
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

    @OnClick(R.id.shareButton)
    public void shareButtonClick() {
        ImageUtils.shareImages(getContext(), getAllFilePath(selectedItems));
    }

    private ArrayList<Uri> getAllFilePath(ArrayList<SelectableImageModel> selectedItems) {
        ArrayList<Uri> files = new ArrayList<>();

        for (int i = 0; i < selectedItems.size(); i++) {
            files.add(ImageUtils.getGlobalPath(getContext(), selectedItems.get(i).getPhoto().toString()));
        }

        return files;
    }

    @Override
    public void onBackPressed() {
        if (adapter.isSelectable()) {
            adapter.setSelectable(false);
            showStartInstrumentsMenu();
        } else
            getActivity().finish();
    }

    @Override
    public void deleteClick() {
        selectedItems = adapter.getSelectedItems();
        ImageUtils.deleteImage(getActivity().getContentResolver(), selectedItems);
        showStartInstrumentsMenu();
        removeSelectedItems();
        adapter.setSelectable(false);
    }

    public static AlertDialog.Builder createErrorAlertDialog(final Context context, String message) {
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setMessage(message);
        ad.setPositiveButton("Ок", null);
        return ad;
    }

    private void removeSelectedItems() {
        for (int i = 0; i < selectedItems.size(); i++) {
            images.remove(selectedItems.get(i));
        }
        adapter.notifyDataSetChanged();
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

