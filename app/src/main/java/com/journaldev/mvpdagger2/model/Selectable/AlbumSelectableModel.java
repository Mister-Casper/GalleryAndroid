package com.journaldev.mvpdagger2.model.Selectable;

import android.net.Uri;
import com.journaldev.mvpdagger2.model.AlbumModel;

public class AlbumSelectableModel extends AlbumModel implements AlbumSelectable {

    private boolean isSelected;

    public AlbumSelectableModel(AlbumModel item, boolean isSelected) {
        super(item.getName(),item.getImages());
        this.isSelected = isSelected;
    }

    public AlbumSelectableModel(AlbumModel item) {
        super(item.getName(),item.getImages());
        this.isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public Uri getUri(int position) {
        return getImages().get(position).getImage();
    }
}
