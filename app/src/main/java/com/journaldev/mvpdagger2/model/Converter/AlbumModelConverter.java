package com.journaldev.mvpdagger2.model.Converter;

import com.journaldev.mvpdagger2.model.AlbumModel;
import com.journaldev.mvpdagger2.model.Selectable.AlbumSelectableModel;
import com.journaldev.mvpdagger2.model.Selectable.ImageSelectableModel;
import com.journaldev.mvpdagger2.model.Selectable.Selectable;

import java.util.ArrayList;

public class AlbumModelConverter {

    public static ArrayList<Selectable> convertAlbumsToSelectable(ArrayList<AlbumModel> albums){
        ArrayList<Selectable> items = new ArrayList<>();

        for(int i = 0 ; i < albums.size();i++){
            items.add(new AlbumSelectableModel(albums.get(i)));
        }

        return items;
    }
}
