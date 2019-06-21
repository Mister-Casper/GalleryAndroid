package com.journaldev.mvpdagger2.Data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

public class ImageUrls {
    private static Uri[] mUrls;

    public static Uri[] getUrls() {
        return mUrls;
    }
    public static long[] getDate() {
        return mDate;
    }

    private static long[] mDate;
    private static Cursor cc = null;

    private static int maxImageId = 0;

    public static void getImageUrl(Context context) {
        cc = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);

        if (cc != null) {
            cc.moveToFirst();
            mUrls = new Uri[cc.getCount()];
            mDate = new long[cc.getCount()];
            for (int i = 0; i < cc.getCount(); i++) {
                cc.moveToPosition(i);
                Uri temp  = Uri.parse(cc.getString(1));
                File file = new File(String.valueOf(temp));
               // mUrls[i] = Uri.fromFile(file);
                mUrls[i] = temp;
                long date = file.lastModified();
                mDate[i] = date;
            }
        }
        maxImageId = mUrls.length;
        sortResultByDate();
    }
    private static void sortResultByDate() {
        Uri tempNum;
        long tempName;
        for (int i = 0; i < mDate.length; i++) {
            for (int j = i + 1; j < mDate.length; j++) {
                if (mDate[i] < mDate[j]) {
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
    public static Uri getImage(int idImage) {
        if (mUrls != null) {
            return mUrls[idImage];
        }
        return Uri.parse("");
    }

}
