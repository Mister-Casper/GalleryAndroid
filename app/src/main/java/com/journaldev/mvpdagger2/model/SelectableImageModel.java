package com.journaldev.mvpdagger2.model;


import java.util.ArrayList;

public class SelectableImageModel extends ImageModel {

    private boolean isSelected;

    public SelectableImageModel(ImageModel item, boolean isSelected) {
        super(item.getPhoto(),item.getLike());
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static String[] convertImagesToStringArray(ArrayList<SelectableImageModel> uri) {
        String[] str = new String[uri.size()];

        for (int i = 0; i < str.length; i++) {
            str[i] = uri.get(i).getPhoto().toString();
        }

        return str;
    }
}
