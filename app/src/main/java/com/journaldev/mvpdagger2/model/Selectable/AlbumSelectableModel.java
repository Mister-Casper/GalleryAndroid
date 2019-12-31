package com.journaldev.mvpdagger2.model.Selectable;

import android.net.Uri;

import com.journaldev.mvpdagger2.model.AlbumModel;
import com.journaldev.mvpdagger2.model.ImageModel;
import com.journaldev.mvpdagger2.model.Selectable.Selectable;

import java.util.ArrayList;

public class AlbumSelectableModel extends AlbumModel implements AlbumSelectable {

    private boolean isSelected;

    public AlbumSelectableModel(AlbumModel item, boolean isSelected) {
        super(item.getName(), item.getUri(), item.getLike());
        this.isSelected = isSelected;
    }

    public AlbumSelectableModel(AlbumModel item) {
        super(item.getName(), item.getUri(), item.getLike());
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
        return getUri().get(position);
    }
}
