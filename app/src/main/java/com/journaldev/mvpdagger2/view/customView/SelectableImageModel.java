package com.journaldev.mvpdagger2.view.customView;

import com.journaldev.mvpdagger2.model.ImageModel;

public class SelectableImageModel extends ImageModel {

    private boolean isSelected = false;

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
}
