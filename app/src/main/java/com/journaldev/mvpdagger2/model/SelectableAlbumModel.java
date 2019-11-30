package com.journaldev.mvpdagger2.model;

public class SelectableAlbumModel extends AlbumModel {

    private boolean isSelected;

    public SelectableAlbumModel(AlbumModel item, boolean isSelected) {
        super(item.getName(),item.getUri(),item.getLike());
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
