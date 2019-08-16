package com.journaldev.mvpdagger2.Data;

import android.content.ClipData;
import android.net.Uri;

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
