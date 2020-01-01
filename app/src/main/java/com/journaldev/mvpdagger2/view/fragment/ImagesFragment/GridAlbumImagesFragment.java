package com.journaldev.mvpdagger2.view.fragment.ImagesFragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.journaldev.mvpdagger2.application.App;
import com.journaldev.mvpdagger2.data.Album.AlbumRepository;
import com.journaldev.mvpdagger2.data.Album.AlbumRepositoryObserver;
import com.journaldev.mvpdagger2.model.AlbumModel;
import com.journaldev.mvpdagger2.model.Converter.AlbumModelConverter;
import com.journaldev.mvpdagger2.model.Converter.ImageModelConverter;

import java.util.ArrayList;

public class GridAlbumImagesFragment extends BaseGridImagesFragment implements AlbumRepositoryObserver {

    String albumName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAlbumRepository().getAlbumObserver().addImageUrlsRepositoryObserver(this);
        albumName = getArguments().getString("albumName");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.getAlbumRepository().getAlbumObserver().removeImageUrlsRepositoryObserver(this);
    }

    @Override
    public void onUpdateAlbum(ArrayList<AlbumModel> updateAlbums) {
        AlbumModel currentAlbum = getCurrentAlbum(updateAlbums);
        this.images = currentAlbum.getImages();
        adapter.setImages(ImageModelConverter.convertImagesToSelectable(images));
    }

    private AlbumModel getCurrentAlbum(ArrayList<AlbumModel> updateAlbums){
        for(int i = 0 ; i < updateAlbums.size();i++)
        {
            AlbumModel albumModel = updateAlbums.get(i);
            if(albumModel.getName().equals(albumName))
                return albumModel;
        }

        return new AlbumModel("",new ArrayList<>(),new ArrayList<>());
    }
}
