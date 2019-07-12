package com.journaldev.mvpdagger2.fragments.albums;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.journaldev.mvpdagger2.Data.AlbumsInfo;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.activity.ViewImages.view.ViewImagesActivity;
import com.journaldev.mvpdagger2.adapters.AlbumsAdapter;
import com.journaldev.mvpdagger2.adapters.PhotosAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class albums extends Fragment  implements AlbumsAdapter.ItemClickListener{

    @BindView(R.id.field)
    RecyclerView field;
    Unbinder unbinder;
    AlbumsInfo.Album[] albums;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        unbinder = ButterKnife.bind(this, view);
        AlbumsInfo.loadImageUrl(getContext());
        albums = AlbumsInfo.getAllAlbum();
        field.setLayoutManager(new GridLayoutManager(getContext(), 2));
        AlbumsAdapter adapter = new AlbumsAdapter(getActivity().getApplicationContext(), albums);
        adapter.setClickListener(this);
        field.setAdapter(adapter);
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
        intent.putStringArrayListExtra("uri",convertArraysUrlToArraysString(position));
        getContext().startActivity(intent);
    }

    private ArrayList<String> convertArraysUrlToArraysString(int id)
    {
        ArrayList<Uri> uri = albums[id].getUri();
        ArrayList<String> strUri = new ArrayList<>();
        for(int i = 0 ; i < uri.size();i++)
            strUri.add(uri.get(i).toString());
        return strUri;
    }
}
