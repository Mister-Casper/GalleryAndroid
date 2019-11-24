package com.journaldev.mvpdagger2.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.journaldev.mvpdagger2.data.Album;
import com.journaldev.mvpdagger2.data.AlbumsInfo;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.view.activity.ViewImagesActivity;
import com.journaldev.mvpdagger2.view.adapter.AlbumsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class Albums extends Fragment implements AlbumsAdapter.ItemClickListener {

    @BindView(R.id.field)
    RecyclerView field;
    Unbinder unbinder;
    ArrayList<Album> albums;

    @Override
    public void onStart() {
        super.onStart();
        albums = AlbumsInfo.getAllAlbum(getContext());
        setAdapter();
    }

    private void setAdapter() {
        field.setLayoutManager(new GridLayoutManager(getContext(), 2));
        AlbumsAdapter adapter = new AlbumsAdapter(getActivity().getApplicationContext(), albums);
        adapter.setClickListener(this);
        field.setAdapter(adapter);
        field.invalidate();
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

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), ViewImagesActivity.class);
        intent.putStringArrayListExtra("uri", convertArraysUrlToArraysString(position));
        intent.putStringArrayListExtra("like", convertArraysLikeToArraysString(position));
        getContext().startActivity(intent);
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

}
