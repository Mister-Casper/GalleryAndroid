package com.journaldev.mvpdagger2.view.fragment.ImagesFragment.gridImages.image;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.journaldev.mvpdagger2.application.App;
import com.journaldev.mvpdagger2.model.Converter.ImageModelConverter;
import com.journaldev.mvpdagger2.model.ImageModel;
import com.journaldev.mvpdagger2.data.Image.ImageRepositoryObserver;
import com.journaldev.mvpdagger2.view.fragment.ImagesFragment.gridImages.BaseGridImagesFragment;

import java.util.ArrayList;


public class GridImagesFragment extends BaseGridImagesFragment implements ImageRepositoryObserver {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getImageRepository().getImageObserver().addImageUrlsRepositoryObserver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.getImageRepository().getImageObserver().removeImageUrlsRepositoryObserver(this);
    }

    @Override
    public void onUpdateImage(ArrayList<ImageModel> updateUrls) {
        this.images = updateUrls;
        adapter.setImages(ImageModelConverter.convertImagesToSelectable(updateUrls));
    }
}
