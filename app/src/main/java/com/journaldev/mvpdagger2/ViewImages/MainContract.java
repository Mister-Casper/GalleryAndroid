package com.journaldev.mvpdagger2;

import android.content.Context;
import android.net.Uri;

/**
 * Created by anupamchugh on 11/08/17.
 */

public interface MainContract {

    interface ViewCallBack {
        void viewImage(Uri imageUri);

        void onBackImageButton();

        void onNextImageButton();

        Context getContext();

    }

    interface ModelCallBack {

        void init(Context context);

        Uri getImage(int idImage);

        int getMaxId();
    }

    interface PresenterCallBack {
        void chandgeCurrentImage(int chandgeCurrentImageId);

    }
}
