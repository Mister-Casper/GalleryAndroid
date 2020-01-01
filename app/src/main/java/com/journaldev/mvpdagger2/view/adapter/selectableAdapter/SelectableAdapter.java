package com.journaldev.mvpdagger2.view.adapter.selectableAdapter;

import com.journaldev.mvpdagger2.model.Selectable.Selectable;

import java.util.ArrayList;

public interface SelectableAdapter {
    void setItemsSelectable(boolean selectable);
    void setSelectable(boolean value);
    boolean isSelectable();
    void notifyDataSetChanged();
    ArrayList<Selectable> getSelectedItems();
    void setImages(ArrayList<Selectable> items);
}
