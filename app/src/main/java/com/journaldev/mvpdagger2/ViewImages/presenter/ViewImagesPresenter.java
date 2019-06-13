package com.journaldev.mvpdagger2.ViewImages.presenter;

import android.support.annotation.NonNull;

import com.journaldev.mvpdagger2.MainContract;
import com.journaldev.mvpdagger2.ViewImages.model.ViewImagesModel;

import javax.inject.Singleton;

@Singleton
public class ViewImagesPresenter implements MainContract.PresenterCallBack{

    private MainContract.ViewCallBack mainView;
    private ViewImagesModel model;

    public ViewImagesPresenter(@NonNull MainContract.ViewCallBack imageView, @NonNull ViewImagesModel imageModelContract) {
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