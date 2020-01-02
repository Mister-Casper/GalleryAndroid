package com.journaldev.mvpdagger2.view.adapter.selectableAdapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.application.App;
import com.journaldev.mvpdagger2.model.Selectable.Selectable;
import com.journaldev.mvpdagger2.utils.GlideUtils;

import java.util.ArrayList;

abstract class BaseSelectableAdapter extends RecyclerView.Adapter<SelectableViewHolder> implements SelectableAdapter {

    SelectableViewHolder.OnItemClickListener itemClickListener;
    SelectableViewHolder.OnItemSelectedListener selectedItemClickListener;

    boolean isSelectable = false;
    boolean isSelectableMode = true;
    LayoutInflater mInflater;
    ArrayList<Selectable> items = new ArrayList<>();

    public void setClickListener(SelectableViewHolder.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setSelectedItemClickListener(SelectableViewHolder.OnItemSelectedListener selectedItemClickListener) {
        this.selectedItemClickListener = selectedItemClickListener;
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public void setSelectable(boolean value) {
        if (isSelectableMode) {
            if (value)
                setItemsSelectable(false);
            isSelectable = value;
            notifyDataSetChanged();
        }
    }

    public void setSelectableMode(boolean selectableMode) {
        setSelectable(false);
        isSelectableMode = selectableMode;
    }

    public void setImages(ArrayList<Selectable> items) {
        this.items = items;
        notifyDataSetChanged();
        selectedItemClickListener.onItemSelected();
    }

    void settingSelectableMod(SelectableViewHolder holder, Selectable selectableItem) {
        if (isSelectable) {
            holder.selectMultiPhoto.setChecked(selectableItem.isSelected());
            holder.selectMultiPhoto.setVisibility(View.VISIBLE);
        } else {
            holder.selectMultiPhoto.setChecked(false);
            holder.selectMultiPhoto.setVisibility(View.GONE);
        }
    }

    void showUri(SelectableViewHolder holder, Uri uri) {
        RequestOptions options = new RequestOptions();
        options = options.sizeMultiplier(0.75f);
        options = options.centerCrop();
        options = options.fitCenter();
        options = options.placeholder(R.drawable.placeholder);

        if (!App.getAppPreference().getIsCache())
            options = GlideUtils.optionsCleanCache(options);

        Glide.with(mInflater.getContext())
                .load(uri)
                .apply(options)
                .into(holder.image);

    }

    @Override
    public void onBindViewHolder(@NonNull SelectableViewHolder holder, int position) {
        Selectable selectableItem = items.get(position);
        setImageClickListener(holder, position);
        holder.item = selectableItem;
        holder.setChecked(holder.item.isSelected());
        settingSelectableMod(holder, selectableItem);
    }

    private void setImageClickListener(final SelectableViewHolder holder, final int position) {
        setImageOnClickListener(holder, position);
        setImageLongClickListener(holder);
    }

    private void setImageOnClickListener(final SelectableViewHolder holder, final int position) {
        holder.image.setOnClickListener(view -> {
            if (!isSelectable)
                itemClickListener.onItemClick(view, position);
            else {
                checkedCheckBox(holder);
            }
        });
    }

    private void setImageLongClickListener(final SelectableViewHolder holder) {
        holder.image.setOnLongClickListener(view -> {
            setSelectable(!isSelectable);
            checkedCheckBox(holder);
            return false;
        });
    }

    private void checkedCheckBox(SelectableViewHolder holder) {
        if (isSelectableMode) {
            boolean checked = !holder.selectMultiPhoto.isChecked();
            holder.selectMultiPhoto.setChecked(checked);
            holder.setChecked(holder.selectMultiPhoto.isChecked());
            onItemSelected();
            selectedItemClickListener.onItemSelected();
        }
    }

    private void onItemSelected() {
        selectedItemClickListener.onItemSelected();
    }

    void setTransitionName(SelectableViewHolder holder, int position) {
        String name = holder.image.getContext().getString(R.string.transition_name, position);
        holder.image.setTransitionName(name);
    }

    public ArrayList<Selectable> getSelectedItems() {
        ArrayList<Selectable> selectedItems = new ArrayList<>();
        for (Selectable item : items) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    public void setItemsSelectable(boolean selectable) {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setSelected(selectable);
        }
        selectedItemClickListener.onItemSelected();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
