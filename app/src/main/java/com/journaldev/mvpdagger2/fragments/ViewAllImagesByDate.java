package com.journaldev.mvpdagger2.fragments;


//grid:layout_columnWeight="1"

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.journaldev.mvpdagger2.Data.ImageUrls;
import com.journaldev.mvpdagger2.Data.ItemPhotoData;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.activity.ViewImagesActivity;
import com.journaldev.mvpdagger2.adapters.PhotosAdapter;
import com.journaldev.mvpdagger2.utils.FabricEvents;
import com.journaldev.mvpdagger2.utils.MeasurementLaunchTime;


import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ViewAllImagesByDate extends Fragment implements PhotosAdapter.ItemClickListener {


    @BindView(R.id.DataList)
    RecyclerView DataList;
    Unbinder unbinder;
    LinkedList<Uri> uri = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        setAdapter();
    }

    private void setAdapter() {
        PhotosAdapter adapter = new PhotosAdapter(getActivity().getApplicationContext(), loadUri());
        adapter.setClickListener(this);
        DataList.setAdapter(adapter);
        DataList.invalidate();
    }

    private ArrayList<ItemPhotoData> loadUri() {
        uri = ImageUrls.getUrls(getContext());
        ArrayList<ItemPhotoData> photo = new ArrayList<>();
        for (int i = 0; i < uri.size(); i++)
            photo.add(new ItemPhotoData(uri.get(i)));
        return photo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragmentviewallimagesbydate, container, false);
        unbinder = ButterKnife.bind(this, view);
        setAdapter();
        DataList.setLayoutManager(new GridLayoutManager(getContext(), 4));
        container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                container.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                MeasurementLaunchTime.loadTime = System.currentTimeMillis();
                FabricEvents.sendLaunchTime(MeasurementLaunchTime.getLaunchTime());
            }
        });

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
        getContext().startActivity(intent);
    }

}
