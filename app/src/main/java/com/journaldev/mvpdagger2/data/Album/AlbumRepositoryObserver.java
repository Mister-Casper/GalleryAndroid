package com.journaldev.mvpdagger2.data.Album;

import com.journaldev.mvpdagger2.model.AlbumModel;
import com.journaldev.mvpdagger2.model.ImageModel;

import java.util.ArrayList;
import java.util.LinkedList;

public interface AlbumRepositoryObserver {

    void onUpdateAlbum(ArrayList<AlbumModel> updateAlbums);

}
