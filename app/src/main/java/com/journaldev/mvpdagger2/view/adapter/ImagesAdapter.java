package com.journaldev.mvpdagger2.view.adapter;

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
import com.journaldev.mvpdagger2.model.Selectable;
import com.journaldev.mvpdagger2.utils.AppPreferenceUtils;
import com.journaldev.mvpdagger2.model.ImageModel;
import com.journaldev.mvpdagger2.model.SelectableImageModel;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.utils.GlideUtils;
import com.journaldev.mvpdagger2.view.customView.SquareImageView;

import java.io.File;
import java.util.ArrayList;

import static com.journaldev.mvpdagger2.model.Converter.ImageModelConverter.convertImageToSelectableImage;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.SelectableViewHolder> implements SelectableAdapter {

    private SelectableViewHolder.OnItemSelectedListener selectedItemClickListener;
    private SelectableViewHolder.OnItemClickListener itemClickListener;

    public void setClickListener(SelectableViewHolder.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setSelectedItemClickListener(SelectableViewHolder.OnItemSelectedListener selectedItemClickListener) {
        this.selectedItemClickListener = selectedItemClickListener;
    }

    private boolean isSelectable = false;

    public boolean isSelectable() {
        return isSelectable;
    }

    public void setSelectable(boolean value) {
        if (value)
            setItemsSelectable(false);
        isSelectable = value;
        notifyDataSetChanged();
    }

    private ArrayList<SelectableImageModel> items;
    private LayoutInflater mInflater;

    public ImagesAdapter(Context context, ArrayList<ImageModel> items) {
        this.mInflater = LayoutInflater.from(context);
        this.items = convertImageToSelectableImage(items);
    }

    public void setImages(ArrayList<ImageModel> items) {
        this.items = convertImageToSelectableImage(items);
        notifyDataSetChanged();
    }

    @Override
    public SelectableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.imageitem, parent, false);

        return new SelectableViewHolder(itemView, selectedItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectableViewHolder holder, final int position) {
        Uri imageUri = items.get(position).getImage();
        File file = new File(String.valueOf(imageUri));
        Uri uri = Uri.fromFile(file);

        SelectableImageModel selectableItem = items.get(position);
        buttonLikeVisibility(holder, position);
        viewImage(holder, uri);
        setImageClickListener(holder, position, selectableItem);
        holder.item = selectableItem;
        holder.setChecked(holder.item.isSelected());
        settingSelectableMod(holder, selectableItem);
        setTransitionName(holder, position);
    }

    private void settingSelectableMod(SelectableViewHolder holder, SelectableImageModel selectableItem) {
        if (isSelectable) {
            holder.selectMultiPhoto.setChecked(selectableItem.isSelected());
            holder.selectMultiPhoto.setVisibility(View.VISIBLE);
        } else {
            holder.selectMultiPhoto.setChecked(false);
            holder.selectMultiPhoto.setVisibility(View.GONE);
        }
    }

    private void setImageClickListener(final SelectableViewHolder holder, final int position, final SelectableImageModel selectableItem) {
        setImageOnClickListener(holder, position, selectableItem);
        setImageLongClickListener(holder, selectableItem);
    }

    private void setImageOnClickListener(final SelectableViewHolder holder, final int position, final SelectableImageModel selectableItem) {
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

    private void setImageLongClickListener(final SelectableViewHolder holder, final SelectableImageModel selectableItem) {
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
        boolean checked = !holder.selectMultiPhoto.isChecked();
        holder.selectMultiPhoto.setChecked(checked);
        holder.setChecked(holder.selectMultiPhoto.isChecked());
        onItemSelected(selectableItem);
        selectedItemClickListener.onItemSelected(selectableItem);
    }

    private void viewImage(SelectableViewHolder holder, Uri uri) {
        RequestOptions options = new RequestOptions();

        options = options.sizeMultiplier(0.75f);
        options = options.centerCrop();
        options = options.fitCenter();
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

    public void setItemsSelectable(boolean selectable) {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setSelected(selectable);
        }
        selectedItemClickListener.onItemSelected(null);
        notifyDataSetChanged();
    }

    public ArrayList<Selectable> getSelectedItems() {
        ArrayList<Selectable> selectedItems = new ArrayList<>();
        for (SelectableImageModel item : items) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
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

        CheckBox selectMultiPhoto;
        SquareImageView image;
        ImageView like;
        SelectableImageModel item;
        OnItemSelectedListener itemSelectedListener;

        SelectableViewHolder(View view, OnItemSelectedListener listener) {
            super(view);
            itemSelectedListener = listener;
            image = itemView.findViewById(R.id.picture);
            like = itemView.findViewById(R.id.like);
            selectMultiPhoto = view.findViewById(R.id.checked_text_item);
            selectMultiPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setChecked(!item.isSelected());
                    itemSelectedListener.onItemSelected(item);
                }
            });
        }

        void setChecked(boolean value) {
            item.setSelected(value);
        }
    }
}