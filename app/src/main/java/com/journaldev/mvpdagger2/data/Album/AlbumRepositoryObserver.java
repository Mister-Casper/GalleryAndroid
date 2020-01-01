package com.journaldev.mvpdagger2.data.Album;

import com.journaldev.mvpdagger2.model.AlbumModel;
import java.util.ArrayList;

public interface AlbumRepositoryObserver {

    void onUpdateAlbum(ArrayList<AlbumModel> updateAlbums);

}
