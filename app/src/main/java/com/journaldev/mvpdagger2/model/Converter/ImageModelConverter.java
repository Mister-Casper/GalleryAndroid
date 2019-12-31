package com.journaldev.mvpdagger2.model.Converter;

import com.journaldev.mvpdagger2.model.AlbumModel;
import com.journaldev.mvpdagger2.model.ImageModel;
import com.journaldev.mvpdagger2.model.Selectable.AlbumSelectableModel;
import com.journaldev.mvpdagger2.model.Selectable.Selectable;
import com.journaldev.mvpdagger2.model.Selectable.ImageSelectableModel;

import java.util.ArrayList;

public class ImageModelConverter {

    public static ArrayList<Selectable> convertImagesToSelectable(ArrayList<ImageModel> images){
        ArrayList<Selectable> items = new ArrayList<>();

        for(int i = 0 ; i < images.size();i++){
            items.add(new ImageSelectableModel(images.get(i)));
        }

        return items;
    }
}
