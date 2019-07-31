package com.journaldev.mvpdagger2.Data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;


import java.io.File;
import java.util.LinkedList;

public class ImageUrls {
    private static LinkedList<Uri> mUrls;

    public static boolean isUpdate = false;

    public static LinkedList<Uri> getUrls(Context context) {
        if(mUrls == null) {
            getImageUrl(context);
        }
        if(isUpdate == true) {
            getImageUrl(context);
            isUpdate = false;
        }
        return mUrls;
    }

    public static long[] getDate() {
        return mDate;
    }

    private static long[] mDate;
    private static Cursor cc = null;

    private static int maxImageId = 0;


    private static void getImageUrl(Context context) {
        cc = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);
        loadUrl();
    }

    private static void loadUrl(){
        if (cc != null) {
            cc.moveToFirst();
            mUrls = new LinkedList<Uri>();
            mDate = new long[cc.getCount()];
            for (int i = 0; i < cc.getCount(); i++) {
                cc.moveToPosition(i);
                Uri temp = Uri.parse(cc.getString(1));
                File file = new File(String.valueOf(temp));
                mUrls.add(i,temp);
                long date = file.lastModified();
                mDate[i] = date;
            }
        }
        maxImageId = mUrls.size();
        sortResultByDate();
    }

    private static void sortResultByDate() {
        Uri tempNum;
        long tempName;
        for (int i = 0; i < mDate.length; i++) {
            for (int j = i + 1; j < mDate.length; j++) {
                if (mDate[i] < mDate[j]) {
                    tempNum = mUrls.get(i);
                    tempName = mDate[i];

                    mUrls.set(i,mUrls.get(j));
                    mDate[i] = mDate[j];

                    mUrls.set(j,tempNum);
                    mDate[j] = tempName;
                }
            }
        }
    }

}
