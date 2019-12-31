package com.journaldev.mvpdagger2.data.Album;

import com.journaldev.mvpdagger2.model.AlbumModel;
import com.journaldev.mvpdagger2.model.ImageModel;
import com.journaldev.mvpdagger2.model.Selectable.AlbumSelectable;

import java.util.ArrayList;
import java.util.LinkedList;

public interface AlbumRepositoryObserver {

    void onUpdateAlbum(ArrayList<AlbumModel> updateAlbums);

}
