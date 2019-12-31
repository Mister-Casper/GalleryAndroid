package com.journaldev.mvpdagger2.model.Selectable;

import android.net.Uri;

import com.journaldev.mvpdagger2.model.ImageModel;

import java.util.ArrayList;

public interface Selectable {

    ArrayList<ImageModel> getImages();

    static  ArrayList<Uri> getAll(ArrayList<Selectable> selectedItems) {
        ArrayList<Uri> images = new ArrayList<>();
        for (int i = selectedItems.size()-1; i >=0; i--) {
            ArrayList<ImageModel> selectImages = selectedItems.get(i).getImages();
            for (int q = selectImages.size()-1; q >=0; q--) {
                images.add(selectedItems.get(i).getImages().get(q).getImage());
            }
        }
        return images;
    }

    boolean isSelected();
    void setSelected(boolean selected);
}
