package com.journaldev.mvpdagger2.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.journaldev.mvpdagger2.data.Album.AlbumRepository;
import com.journaldev.mvpdagger2.data.Album.AlbumRepositoryObserver;
import com.journaldev.mvpdagger2.data.Image.ImageRepository;
import com.journaldev.mvpdagger2.data.Image.ImageRepositoryObserver;
import com.journaldev.mvpdagger2.model.AlbumModel;
import com.journaldev.mvpdagger2.model.ImageModel;

import java.util.ArrayList;


public class GridAlbumImagesFragment extends BaseGridImagesFragment implements AlbumRepositoryObserver {

    String albumName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlbumRepository.AlbumObserver.addImageUrlsRepositoryObserver(this);
        albumName = getArguments().getString("albumName");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AlbumRepository.AlbumObserver.removeImageUrlsRepositoryObserver(this);
    }

    @Override
    public void onUpdateAlbum(ArrayList<AlbumModel> updateAlbums) {
        this.images = getCurrentAlbum(updateAlbums).getImages();
        adapter.setImages(images);
    }

    private AlbumModel getCurrentAlbum(ArrayList<AlbumModel> updateAlbums){
        for(int i = 0 ; i < updateAlbums.size();i++)
        {
            AlbumModel albumModel = updateAlbums.get(i);
            if(albumModel.getName().equals(albumName))
                return albumModel;
        }

        return new AlbumModel(null,null,null);
    }
}
