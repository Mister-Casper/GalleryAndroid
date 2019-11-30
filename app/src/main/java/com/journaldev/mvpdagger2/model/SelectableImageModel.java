package com.journaldev.mvpdagger2.model;


import java.util.ArrayList;

public class SelectableImageModel extends ImageModel implements Selectable {

    private boolean isSelected;

    public SelectableImageModel(ImageModel item, boolean isSelected) {
        super(item.getImage(), item.getLike());
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static String[] convertImagesToStringArray(ArrayList<Selectable> uri) {
        String[] str = new String[uri.size()];

        for (int i = 0; i < str.length; i++) {
            str[i] = uri.get(i).getImages().get(0).toString();
        }

        return str;
    }

    @Override
    public ArrayList<ImageModel> getImages() {
        ArrayList<ImageModel> imageModels = new ArrayList<>();
        imageModels.add(this);
        return imageModels;
    }
}
