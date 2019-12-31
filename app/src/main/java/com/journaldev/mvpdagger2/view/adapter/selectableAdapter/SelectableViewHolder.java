package com.journaldev.mvpdagger2.view.adapter.selectableAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.model.Selectable.Selectable;
import com.journaldev.mvpdagger2.view.customView.SquareImageView;

public class SelectableViewHolder extends RecyclerView.ViewHolder {
    public CheckBox selectMultiPhoto;
    public SquareImageView image;
    public TextView albumName;
    public Selectable item;
    public ImageView like;
    public SelectableViewHolder.OnItemSelectedListener itemSelectedListener;

    SelectableViewHolder(View itemView, SelectableViewHolder.OnItemSelectedListener listener) {
        super(itemView);
        itemSelectedListener = listener;
        image = itemView.findViewById(R.id.picture);
        albumName = itemView.findViewById(R.id.text);
        like = itemView.findViewById(R.id.like);
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
        void onItemSelected(Selectable item);
    }
}

