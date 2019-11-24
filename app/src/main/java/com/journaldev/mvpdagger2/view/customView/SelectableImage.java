package com.journaldev.mvpdagger2.view.customView;

import com.journaldev.mvpdagger2.data.Image;

public class SelectableImage extends Image {

    private boolean isSelected = false;

    public SelectableImage(Image item, boolean isSelected) {
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
