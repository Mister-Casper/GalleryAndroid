package com.journaldev.mvpdagger2.activity.ViewImages.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;


import java.io.File;

public class ViewImagesModel{
    private static Uri[] mUrls;
    private static long[] mDate;
    private int maxImageId = 0;
    public static int currentImageId = 0;

    private Cursor cc = null;

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
            mDate = new long[cc.getCount()];
            for (int i = 0; i < cc.getCount(); i++) {
                cc.moveToPosition(i);
                mUrls[i] = Uri.parse(cc.getString(1));
                File file = new File(String.valueOf(mUrls[i]));

                long data = file.lastModified();
                mDate[i] = data;
            }
        }
        maxImageId = mUrls.length;
        sortResultByDate();
    }

    private void sortResultByDate() {
        Uri tempNum;
        long tempName;
        for (int i = 0; i < mDate.length; i++)
        {
            for (int j = i + 1; j < mDate.length; j++)
            {
                if (mDate[i] < mDate[j])
                {
                    tempNum = mUrls[i];
                    tempName = mDate[i];

                    mUrls[i] = mUrls[j];
                    mDate[i] = mDate[j];

                    mUrls[j] = tempNum;
                    mDate[j] = tempName;
                }
            }
        }
    }

    public Uri getImage(int idImage) {
        if (mUrls != null) {
            return mUrls[idImage];
        }
        return Uri.parse("");
    }

    public int getMaxId() {
        return maxImageId;
    }
}
