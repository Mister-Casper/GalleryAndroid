package com.journaldev.mvpdagger2.fragments.albums;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.journaldev.mvpdagger2.Data.AlbumsInfo;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.adapters.GridAlbomsAdapter;
import com.journaldev.mvpdagger2.adapters.GridPhotoAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class albums extends Fragment {

    @BindView(R.id.field)
    GridView field;
    Unbinder unbinder;

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
        AlbumsInfo.Album[] albums = AlbumsInfo.getAllAlbum();
        GridAlbomsAdapter adapter = new GridAlbomsAdapter(getActivity(), albums);
        field.setAdapter(adapter);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
