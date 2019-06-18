package com.journaldev.mvpdagger2.fragments.ViewAllImagesByDate;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.journaldev.mvpdagger2.R;
public interface ViewAllImagesByDateContract {

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
