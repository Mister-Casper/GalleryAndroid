package com.journaldev.mvpdagger2.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.journaldev.mvpdagger2.Data.SelectableItemPhotoData;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.myVIew.MyImageView;

public class SelectableViewHolder extends RecyclerView.ViewHolder {


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemSelectedListener {

        void onItemSelected(SelectableItemPhotoData item);
    }


    public static final int MULTI_SELECTION = 2;
    public static final int SINGLE_SELECTION = 1;
    CheckBox selectMultiPhoto;
    MyImageView image;
    ImageView like;
    SelectableItemPhotoData mItem;
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
