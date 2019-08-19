package com.journaldev.mvpdagger2.fragments;

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
import android.support.v4.app.SharedElementCallback;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.journaldev.mvpdagger2.Data.AlbumsInfo;
import com.journaldev.mvpdagger2.Data.ImageUrls;
import com.journaldev.mvpdagger2.Data.ItemPhotoData;
import com.journaldev.mvpdagger2.Data.SelectableItemPhotoData;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.activity.MainActivity;
import com.journaldev.mvpdagger2.activity.ViewImagesActivity;
import com.journaldev.mvpdagger2.adapters.PhotosAdapter;
import com.journaldev.mvpdagger2.adapters.SelectableViewHolder;
import com.journaldev.mvpdagger2.utils.FabricEvents;
import com.journaldev.mvpdagger2.utils.ImageUtils;
import com.journaldev.mvpdagger2.utils.MeasurementLaunchTime;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static android.provider.Settings.AUTHORITY;

public class ViewAllImagesByDate extends Fragment implements SelectableViewHolder.OnItemClickListener, SelectableViewHolder.OnItemSelectedListener, MainActivity.OnBackPressedListener, ImageUtils.alertDialogListener {


    @BindView(R.id.DataList)
    RecyclerView DataList;
    Unbinder unbinder;
    LinkedList<ItemPhotoData> uri = null;
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
    PhotosAdapter adapter;
    private int exitPosition;
    private ArrayList<SelectableItemPhotoData> selectedItems;

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
        adapter = new PhotosAdapter(getActivity().getApplicationContext(), loadUri(), this);
        adapter.setClickListener(this);
        DataList.setAdapter(adapter);
        DataList.invalidate();
    }

    private ArrayList<ItemPhotoData> loadUri() {
        uri = ImageUrls.getUrls(getContext());
        ArrayList<ItemPhotoData> photo = new ArrayList<>();
        for (int i = 0; i < uri.size(); i++)
            photo.add(new ItemPhotoData(uri.get(i).getPhoto(), uri.get(i).getLike()));
        return photo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragmentviewallimagesbydate, container, false);
        unbinder = ButterKnife.bind(this, view);
        viewStandartMod();
        setAdapter();
        DataList.setLayoutManager(new GridLayoutManager(getContext(), 4));

        if (savedInstanceState == null) {
            writeStartTime(container);
        }

        return view;
    }

    private void writeStartTime(final View container) {
        container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                container.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                MeasurementLaunchTime.loadTime = System.currentTimeMillis();
                FabricEvents.sendLaunchTime(MeasurementLaunchTime.getLaunchTime());
            }
        });
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
        String name = view.getTransitionName();
        ActivityOptions options =
                ActivityOptions.makeSceneTransitionAnimation(getActivity(), view,
                     name);
        startActivity(intent, options.toBundle());
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onItemSelected(SelectableItemPhotoData item) {
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
        ImageUrls.isUpdate = true;
        AlbumsInfo.isUpdate = true;
    }

    @OnClick(R.id.shareButton)
    public void shareButtonClick() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        sendIntent.setType("image/*");
        sendIntent.putExtra(Intent.EXTRA_STREAM, getAllFilePath(selectedItems));
        startActivity(sendIntent);
    }

    private ArrayList<Uri> getAllFilePath(ArrayList<SelectableItemPhotoData> selectedItems) {
        ArrayList<Uri> files = new ArrayList<>();

        for (int i = 0; i < selectedItems.size(); i++) {
            File file = new File(selectedItems.get(i).getPhoto().toString());
            Uri imageUri = FileProvider.getUriForFile(
                    getContext(),
                    "com.journaldev.mvpdagger2.provider",
                    file);
            files.add(imageUri);
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


}
