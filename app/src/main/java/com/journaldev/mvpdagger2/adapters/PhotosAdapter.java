package com.journaldev.mvpdagger2.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.journaldev.mvpdagger2.Data.AppPreference;
import com.journaldev.mvpdagger2.Data.ItemPhotoData;
import com.journaldev.mvpdagger2.Data.SelectableItemPhotoData;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.myVIew.MyImageView;
import com.journaldev.mvpdagger2.utils.GlideUtils;

import java.io.File;
import java.util.ArrayList;

import static com.journaldev.mvpdagger2.adapters.SelectableViewHolder.MULTI_SELECTION;

public class PhotosAdapter extends RecyclerView.Adapter<SelectableViewHolder> implements SelectableViewHolder.OnItemSelectedListener {

    private ArrayList<SelectableItemPhotoData> items;
    private boolean isMultiSelectionEnabled = true;
    SelectableViewHolder.OnItemSelectedListener listener;
    private LayoutInflater mInflater;
    private SelectableViewHolder.OnItemClickListener mClickListener;

    public boolean isSelectable() {
        return isSelectable;
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    private boolean isSelectable = false;

    // data is passed into the constructor
    public PhotosAdapter(Context context, ArrayList<ItemPhotoData> items, SelectableViewHolder.OnItemSelectedListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.listener = listener;
        this.items = new ArrayList<>();
        for (ItemPhotoData item : items) {
            this.items.add(new SelectableItemPhotoData(item, false));
        }
    }


    // allows clicks events to be caught
    public void setClickListener(SelectableViewHolder.OnItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    @Override
    public SelectableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.imageitem, parent, false);

        return new SelectableViewHolder(itemView, this);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final SelectableViewHolder holder, final int position) {
        Uri photo = items.get(position).getPhoto();
        SelectableItemPhotoData selectableItem = items.get(position);
        File file = new File(String.valueOf(photo));
        Uri uri = Uri.fromFile(file);
        buttonLikeVisibility(holder, position);
        viewImage(holder, uri);
        imageClickListener(holder, position, selectableItem);
        holder.mItem = selectableItem;
        holder.setChecked(holder.mItem.isSelected());
        settingSelectableMod(holder, selectableItem);
        setTransitionName(holder, position);
    }

    private void settingSelectableMod(SelectableViewHolder holder, SelectableItemPhotoData selectableItem) {
        if (isSelectable) {
            holder.selectMultiPhoto.setChecked(selectableItem.isSelected());
            holder.selectMultiPhoto.setVisibility(View.VISIBLE);
        } else
            holder.selectMultiPhoto.setVisibility(View.GONE);
    }

    private void imageClickListener(final SelectableViewHolder holder, final int position, final SelectableItemPhotoData selectableItem) {
        imageOnClickListener(holder, position, selectableItem);
        imageLongClickListener(holder, selectableItem);
    }

    private void imageOnClickListener(final SelectableViewHolder holder, final int position, final SelectableItemPhotoData selectableItem) {
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSelectable)
                    mClickListener.onItemClick(view, position);
                else {
                    checkedCheckBox(holder, selectableItem);
                }
            }
        });
    }

    private void imageLongClickListener(final SelectableViewHolder holder, final SelectableItemPhotoData selectableItem) {
        holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setSelectable(!isSelectable);
                checkedCheckBox(holder, selectableItem);
                return false;
            }
        });
    }

    private void checkedCheckBox(SelectableViewHolder holder, SelectableItemPhotoData selectableItem) {
        holder.selectMultiPhoto.setChecked(!holder.selectMultiPhoto.isChecked());
        holder.setChecked(holder.selectMultiPhoto.isChecked());
        onItemSelected(selectableItem);
        listener.onItemSelected(selectableItem);
    }

    private void viewImage(SelectableViewHolder holder, Uri uri) {
        RequestOptions options = new RequestOptions();
        options.fitCenter();
        options.sizeMultiplier(0.75f);
        options.centerCrop();

        if (!AppPreference.getIsCache())
            GlideUtils.optionsCleanCache(options);

        options.placeholder(R.drawable.standartphoto);

        Glide.with(mInflater.getContext())
                .load(uri)
                .apply(options)
                .into(holder.image);

        holder.image.setPadding(3, 3, 3, 3);
    }

    private void buttonLikeVisibility(SelectableViewHolder holder, int position) {
        if (items.get(position).getLike())
            holder.like.setVisibility(View.VISIBLE);
        else
            holder.like.setVisibility(View.GONE);
    }

    private void setTransitionName(SelectableViewHolder holder, int position) {
        String name = holder.image.getContext().getString(R.string.transition_name, position);
        holder.image.setTransitionName(name);
    }


    public void setSelectable(boolean value) {
        if (value)
            setItemsSelectable(false);
        isSelectable = value;
        notifyDataSetChanged();
    }

    public void setItemsSelectable(boolean selectable) {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setSelected(selectable);
        }
        listener.onItemSelected(null);
        notifyDataSetChanged();
    }


    public ArrayList<SelectableItemPhotoData> getSelectedItems() {

        ArrayList<SelectableItemPhotoData> selectedItems = new ArrayList<>();
        for (SelectableItemPhotoData item : items) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (isMultiSelectionEnabled) {
            return MULTI_SELECTION;
        } else {
            return SelectableViewHolder.SINGLE_SELECTION;
        }
    }


    // total number of cells
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onItemSelected(SelectableItemPhotoData item) {
        listener.onItemSelected(item);
    }


}