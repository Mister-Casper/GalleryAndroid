package com.journaldev.mvpdagger2.model;

import java.util.ArrayList;

public class SelectableAlbumModel extends AlbumModel implements Selectable{

    private boolean isSelected;

    public SelectableAlbumModel(AlbumModel item, boolean isSelected) {
        super(item.getName(), item.getUri(), item.getLike());
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static String[] convertAlbumsToStringArray(ArrayList<Selectable> albums) {
        ArrayList<String> urls = new ArrayList<>();
        String[] urlArray = new String[urls.size()];

        for (int i = 0; i < albums.size(); i++) {
            ArrayList<ImageModel> images = albums.get(i).getImages();
            for (int q = 0; q < images.size(); q++) {
                urls.add(images.get(q).getImage().toString());
            }
        }

        return urls.toArray(urlArray);
    }


    @Override
    public ArrayList<ImageModel> getImages() {
        return super.getImages();
    }
}
