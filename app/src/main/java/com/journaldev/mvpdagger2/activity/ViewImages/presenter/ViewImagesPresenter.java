package com.journaldev.mvpdagger2.activity.ViewImages.presenter;

import android.support.annotation.NonNull;

import com.journaldev.mvpdagger2.activity.ViewImages.ViewImagesContract;
import com.journaldev.mvpdagger2.activity.ViewImages.model.ViewImagesModel;


public class ViewImagesPresenter implements ViewImagesContract.PresenterCallBack{

    private ViewImagesContract.ViewCallBack mainView;
    private ViewImagesModel model;

    public ViewImagesPresenter(@NonNull ViewImagesContract.ViewCallBack imageView, @NonNull ViewImagesModel imageModelContract) {
        this.model = imageModelContract;
        this.mainView = imageView;
    }

    @Override
    public void chandgeCurrentImage(int chandgeCurrentImageId) {
        int newCurrentImageId = ViewImagesModel.currentImageId + chandgeCurrentImageId;
        checkBoundaries(newCurrentImageId);
        mainView.viewImage(model.getImage(ViewImagesModel.currentImageId));
    }

    private void checkBoundaries(int newCurrentImageId) {
        if (newCurrentImageId >= 0 && newCurrentImageId <= model.getMaxId()) {
            ViewImagesModel.currentImageId = newCurrentImageId;
        }
    }

}