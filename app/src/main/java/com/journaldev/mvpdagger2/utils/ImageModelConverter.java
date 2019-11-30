package com.journaldev.mvpdagger2.utils;

import com.journaldev.mvpdagger2.model.ImageModel;
import com.journaldev.mvpdagger2.model.SelectableImageModel;

import java.util.ArrayList;

public class ImageModelConverter {


    public static ArrayList<SelectableImageModel> convertImageToSelectableImage(ArrayList<ImageModel> imageModels) {
        ArrayList<SelectableImageModel> selectableImages = new ArrayList<>();
        for (ImageModel item : imageModels) {
            selectableImages.add(new SelectableImageModel(item, false));
        }
        return selectableImages;
    }
}
