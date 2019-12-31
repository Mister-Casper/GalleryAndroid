package com.journaldev.mvpdagger2.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.journaldev.mvpdagger2.model.Selectable;
import com.journaldev.mvpdagger2.model.SelectableAlbumModel;
import com.journaldev.mvpdagger2.application.App;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.model.AlbumModel;
import com.journaldev.mvpdagger2.view.customView.SquareImageView;
import com.journaldev.mvpdagger2.utils.GlideUtils;

import java.io.File;
import java.util.ArrayList;

import static com.journaldev.mvpdagger2.model.Converter.AlbumModelConverter.convertAlbumsToSelectableAlbums;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.SelectableViewHolder> implements SelectableAdapter {

    private boolean isSelectable = false;
    private ArrayList<SelectableAlbumModel> albums;
    private final LayoutInflater mInflater;

    private SelectableViewHolder.OnItemClickListener itemClickListener;
    private SelectableViewHolder.OnItemSelectedListener selectedItemClickListener;

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
        if (value)
            setItemsSelectable(false);
        isSelectable = value;
        notifyDataSetChanged();
    }

    public ArrayList<Selectable> getSelectedItems() {
        ArrayList<Selectable> selectedItems = new ArrayList<>();
        for (SelectableAlbumModel item : albums) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    public void setItemsSelectable(boolean selectable) {
        for (int i = 0; i < albums.size(); i++) {
            albums.get(i).setSelected(selectable);
        }
        selectedItemClickListener.onItemSelected(null);
        notifyDataSetChanged();
    }

    public AlbumsAdapter(Context context, ArrayList<AlbumModel> albums) {
        this.mInflater = LayoutInflater.from(context);
        this.albums = convertAlbumsToSelectableAlbums(albums);
    }

    public void setAlbums(ArrayList<AlbumModel> albums) {
        this.albums = convertAlbumsToSelectableAlbums(albums);
        notifyDataSetChanged();
    }

    @Override
    public SelectableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.albumitem, parent, false);
        return new SelectableViewHolder(view, selectedItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectableViewHolder holder, int position) {
        Uri firstImage = albums.get(position).getUri().get(0);
        String AlbumName = albums.get(position).getName();
        SelectableAlbumModel selectableItem = albums.get(position);
        showFirstImageInAlbum(holder, firstImage);
        setImageClickListener(holder, position, selectableItem);
        holder.item = selectableItem;
        holder.setChecked(holder.item.isSelected());
        settingSelectableMod(holder, selectableItem);
        holder.albumName.setText(AlbumName);
    }

    private void settingSelectableMod(AlbumsAdapter.SelectableViewHolder holder, SelectableAlbumModel selectableItem) {
        if (isSelectable) {
            holder.selectMultiPhoto.setChecked(selectableItem.isSelected());
            holder.selectMultiPhoto.setVisibility(View.VISIBLE);
        } else {
            holder.selectMultiPhoto.setChecked(false);
            holder.selectMultiPhoto.setVisibility(View.GONE);
        }
    }

    private void showFirstImageInAlbum(SelectableViewHolder holder, Uri firstImage) {
        File file = new File(String.valueOf(firstImage));
        Uri uri = Uri.fromFile(file);

        RequestOptions options = new RequestOptions();

        if (!App.getAppPreference().getIsCache())
            options = GlideUtils.optionsCleanCache(options);

        options = options.fitCenter();
        options = options.centerCrop();
        options = options.placeholder(R.drawable.placeholder);

        Glide.with(mInflater.getContext())
                .load(uri)
                .apply(options)
                .into(holder.image);
    }

    private void setImageClickListener(final AlbumsAdapter.SelectableViewHolder holder, final int position, final SelectableAlbumModel selectableItem) {
        setImageOnClickListener(holder, position, selectableItem);
        setImageLongClickListener(holder, selectableItem);
    }

    private void setImageOnClickListener(final AlbumsAdapter.SelectableViewHolder holder, final int position, final SelectableAlbumModel selectableItem) {
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

    private void setImageLongClickListener(final AlbumsAdapter.SelectableViewHolder holder, final SelectableAlbumModel selectableItem) {
        holder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setSelectable(!isSelectable);
                checkedCheckBox(holder, selectableItem);
                return false;
            }
        });
    }

    private void checkedCheckBox(AlbumsAdapter.SelectableViewHolder holder, SelectableAlbumModel selectableItem) {
        boolean checked = !holder.selectMultiPhoto.isChecked();
        holder.selectMultiPhoto.setChecked(checked);
        holder.setChecked(holder.selectMultiPhoto.isChecked());
        onItemSelected(selectableItem);
        selectedItemClickListener.onItemSelected(selectableItem);
    }

    private void onItemSelected(SelectableAlbumModel item) {
        selectedItemClickListener.onItemSelected(item);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }


    public static class SelectableViewHolder extends RecyclerView.ViewHolder {
        CheckBox selectMultiPhoto;
        SquareImageView image;
        TextView albumName;
        SelectableAlbumModel item;
        SelectableViewHolder.OnItemSelectedListener itemSelectedListener;

        SelectableViewHolder(View itemView, SelectableViewHolder.OnItemSelectedListener listener) {
            super(itemView);
            itemSelectedListener = listener;
            image = itemView.findViewById(R.id.picture);
            albumName = itemView.findViewById(R.id.text);
            selectMultiPhoto = itemView.findViewById(R.id.checked_text_item);
            selectMultiPhoto.setOnClickListener(view -> {
                setChecked(!item.isSelected());
                itemSelectedListener.onItemSelected(item);
            });
        }

        void setChecked(boolean value) {
            item.setSelected(value);
        }

        public interface OnItemClickListener {
            void onItemClick(View view, int position);
        }

        public interface OnItemSelectedListener {
            void onItemSelected(SelectableAlbumModel item);
        }
    }

}