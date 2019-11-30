package com.journaldev.mvpdagger2.view.adapter;

import com.journaldev.mvpdagger2.model.Selectable;

import java.util.ArrayList;

public interface SelectableAdapter {
    void setItemsSelectable(boolean selectable);
    void setSelectable(boolean value);
    boolean isSelectable();
    void notifyDataSetChanged();
    ArrayList<Selectable> getSelectedItems();
}
