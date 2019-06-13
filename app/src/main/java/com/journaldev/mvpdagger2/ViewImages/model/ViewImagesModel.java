package com.journaldev.mvpdagger2.ViewImages.presenter.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.journaldev.mvpdagger2.MainContract;

import java.io.File;
import java.util.Date;

public class ViewImagesModel implements MainContract.ModelCallBack {
    private static Uri[] mUrls;
    private int maxImageId = 0;
    public static int currentImageId = 0;

    private Cursor cc = null;

    @Override
    public void init(Context context) {
        getImageUrl(context);
    }

    public void getImageUrl(Context context) {
        cc = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);

        if (cc != null) {
            cc.moveToFirst();
            mUrls = new Uri[cc.getCount()];
            for (int i = 0; i < cc.getCount(); i++) {
                cc.moveToPosition(i);
                mUrls[i] = Uri.parse(cc.getString(1));
                File file = new File(String.valueOf(mUrls[i]));
                long data = file.lastModified();
                Date date = new Date(data);
            }
        }
        maxImageId = mUrls.length;
    }


    @Override
    public Uri getImage(int idImage) {
        if (mUrls != null) {
            return mUrls[idImage];
        }
        return Uri.parse("");
    }

    @Override
    public int getMaxId() {
        return maxImageId;
    }
}
