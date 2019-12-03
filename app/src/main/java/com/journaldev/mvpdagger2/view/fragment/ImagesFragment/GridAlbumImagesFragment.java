package com.journaldev.mvpdagger2.view.fragment.ImagesFragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.journaldev.mvpdagger2.data.Album.AlbumRepository;
import com.journaldev.mvpdagger2.data.Album.AlbumRepositoryObserver;
import com.journaldev.mvpdagger2.model.AlbumModel;
import com.journaldev.mvpdagger2.view.fragment.ImagesFragment.BaseGridImagesFragment;

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

        return new AlbumModel("",new ArrayList<Uri>(),new ArrayList<String>());
    }
}
