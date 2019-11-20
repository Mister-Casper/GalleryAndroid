package com.journaldev.mvpdagger2.data;

public class SelectableItemPhotoData extends ItemPhotoData {

    private boolean isSelected = false;

    public SelectableItemPhotoData(ItemPhotoData item, boolean isSelected) {
        super(item.getPhoto(),item.getLike());
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
