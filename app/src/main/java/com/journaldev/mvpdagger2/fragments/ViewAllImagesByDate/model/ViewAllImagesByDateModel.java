package com.journaldev.mvpdagger2.fragments.ViewAllImagesByDate.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.journaldev.mvpdagger2.Data.ImageUrls;
import com.journaldev.mvpdagger2.activity.ViewImages.ViewImagesContract;

import java.io.File;

public class ViewAllImagesByDateModel implements ViewImagesContract.ModelCallBack {
    private int maxImageId = 0;

    @Override
    public void init(Context context) {
        ImageUrls.getImageUrl(context);
    }

    @Override
    public Uri getImage(int idImage) {
        return ImageUrls.getImage(idImage);
    }

    @Override
    public int getMaxId() {
        return maxImageId;
    }
}
