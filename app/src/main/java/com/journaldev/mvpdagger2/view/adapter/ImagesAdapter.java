package com.journaldev.mvpdagger2.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.journaldev.mvpdagger2.utils.AppPreferenceUtils;
import com.journaldev.mvpdagger2.model.ImageModel;
import com.journaldev.mvpdagger2.view.customView.SelectableImageModel;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.utils.GlideUtils;
import com.journaldev.mvpdagger2.view.customView.SquareImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import static com.journaldev.mvpdagger2.view.adapter.ImagesAdapter.SelectableViewHolder.MULTI_SELECTION;


public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.SelectableViewHolder> {

    private LinkedList<SelectableImageModel> items;
    private boolean isMultiSelectionEnabled = true;
    private LayoutInflater mInflater;
    private SelectableViewHolder.OnItemSelectedListener selectedItemClickListener;
    private SelectableViewHolder.OnItemClickListener itemClickListener;

    public boolean isSelectable() {
        return isSelectable;
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    private boolean isSelectable = false;

    public ImagesAdapter(Context context, LinkedList<ImageModel> items, SelectableViewHolder.OnItemSelectedListener selectedItemClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.selectedItemClickListener = selectedItemClickListener;
        this.items = convertImageToSelectableImage(items);
    }

    public void setImages(LinkedList<ImageModel> items){
        this.items = convertImageToSelectableImage(items);
        notifyDataSetChanged();
    }

    private LinkedList<SelectableImageModel> convertImageToSelectableImage(LinkedList<ImageModel> imageModels) {
        LinkedList<SelectableImageModel> selectableImages = new LinkedList<>();
        for (ImageModel item : imageModels) {
            selectableImages.add(new SelectableImageModel(item, false));
        }
        return selectableImages;
    }

    // allows clicks events to be caught
    public void setClickListener(SelectableViewHolder.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public SelectableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.imageitem, parent, false);

        return new SelectableViewHolder(itemView, selectedItemClickListener);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final SelectableViewHolder holder, final int position) {
        Uri photo = items.get(position).getPhoto();
        SelectableImageModel selectableItem = items.get(position);
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

    private void settingSelectableMod(SelectableViewHolder holder, SelectableImageModel selectableItem) {
        if (isSelectable) {
            holder.selectMultiPhoto.setChecked(selectableItem.isSelected());
            holder.selectMultiPhoto.setVisibility(View.VISIBLE);
        } else
            holder.selectMultiPhoto.setVisibility(View.GONE);
    }

    private void imageClickListener(final SelectableViewHolder holder, final int position, final SelectableImageModel selectableItem) {
        imageOnClickListener(holder, position, selectableItem);
        imageLongClickListener(holder, selectableItem);
    }

    private void imageOnClickListener(final SelectableViewHolder holder, final int position, final SelectableImageModel selectableItem) {
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSelectable)
                    itemClickListener.onItemClick(view, position);
                else {
                    checkedCheckBox(holder, selectableItem);
                }
            }
        });
    }

    private void imageLongClickListener(final SelectableViewHolder holder, final SelectableImageModel selectableItem) {
        holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setSelectable(!isSelectable);
                checkedCheckBox(holder, selectableItem);
                return false;
            }
        });
    }

    private void checkedCheckBox(SelectableViewHolder holder, SelectableImageModel selectableItem) {
        holder.selectMultiPhoto.setChecked(!holder.selectMultiPhoto.isChecked());
        holder.setChecked(holder.selectMultiPhoto.isChecked());
        onItemSelected(selectableItem);
        selectedItemClickListener.onItemSelected(selectableItem);
    }

    private void viewImage(SelectableViewHolder holder, Uri uri) {
        RequestOptions options = new RequestOptions();

        options = options.sizeMultiplier(0.75f);
        options = options.centerCrop();
        options = options.placeholder(R.drawable.placeholder);

        if (!AppPreferenceUtils.getIsCache())
            options = GlideUtils.optionsCleanCache(options);

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
        selectedItemClickListener.onItemSelected(null);
        notifyDataSetChanged();
    }


    public ArrayList<SelectableImageModel> getSelectedItems() {

        ArrayList<SelectableImageModel> selectedItems = new ArrayList<>();
        for (SelectableImageModel item : items) {
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

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void onItemSelected(SelectableImageModel item) {
        selectedItemClickListener.onItemSelected(item);
    }

    public static class SelectableViewHolder extends RecyclerView.ViewHolder {
        public interface OnItemClickListener {
            void onItemClick(View view, int position);
        }

        public interface OnItemSelectedListener {
            void onItemSelected(SelectableImageModel item);
        }

        public static final int MULTI_SELECTION = 2;
        public static final int SINGLE_SELECTION = 1;
        CheckBox selectMultiPhoto;
        public SquareImageView image;
        ImageView like;
        SelectableImageModel mItem;
        OnItemSelectedListener itemSelectedListener;

        public SelectableViewHolder(View view, OnItemSelectedListener listener) {
            super(view);
            itemSelectedListener = listener;
            image = itemView.findViewById(R.id.picture);
            like = itemView.findViewById(R.id.like);
            selectMultiPhoto = view.findViewById(R.id.checked_text_item);
            selectMultiPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItem.isSelected() && getItemViewType() == MULTI_SELECTION) {
                        setChecked(false);
                    } else {
                        setChecked(true);
                    }
                    itemSelectedListener.onItemSelected(mItem);
                }
            });
        }

        public void setChecked(boolean value) {
            mItem.setSelected(value);
        }
    }
}