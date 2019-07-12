package com.journaldev.mvpdagger2.fragments.ViewAllImagesByDate;


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
import com.journaldev.mvpdagger2.activity.ViewImages.view.ViewImagesActivity;
import com.journaldev.mvpdagger2.adapters.PhotosAdapter;
import com.journaldev.mvpdagger2.utils.FabricEvents;
import com.journaldev.mvpdagger2.utils.MeasurementLaunchTime;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ViewAllImagesByDate extends Fragment  implements PhotosAdapter.ItemClickListener{


    @BindView(R.id.DataList)
    RecyclerView DataList;
    Unbinder unbinder;

    public static ViewAllImagesByDate getInstance() {
        ViewAllImagesByDate fragment = new ViewAllImagesByDate();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragmentviewallimagesbydate, container, false);
        unbinder = ButterKnife.bind(this, view);
        Uri[] uri = ImageUrls.getUrls(getContext());
        ArrayList<ItemPhotoData> photo = new ArrayList<>();
        for (int i = 0; i < uri.length; i++)
            photo.add(new ItemPhotoData(uri[i]));
        DataList.setLayoutManager(new GridLayoutManager(getContext(), 4));
        PhotosAdapter adapter = new PhotosAdapter(getActivity().getApplicationContext(), photo);
        adapter.setClickListener(this);
        DataList.setAdapter(adapter);

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
