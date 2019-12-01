package com.journaldev.mvpdagger2.model.Converter;

import com.journaldev.mvpdagger2.model.AlbumModel;
import com.journaldev.mvpdagger2.model.SelectableAlbumModel;

import java.util.ArrayList;

public class AlbumModelConverter {
    public static ArrayList<SelectableAlbumModel> convertAlbumsToSelectableAlbums(ArrayList<AlbumModel> albums) {
        ArrayList<SelectableAlbumModel> selectableImages = new ArrayList<>();
        for (AlbumModel item : albums) {
            selectableImages.add(new SelectableAlbumModel(item, false));
        }
        return selectableImages;
    }

}
