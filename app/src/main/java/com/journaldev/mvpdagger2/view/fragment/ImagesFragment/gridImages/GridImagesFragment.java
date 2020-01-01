package com.journaldev.mvpdagger2.view.fragment.ImagesFragment.gridImages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.test.espresso.core.internal.deps.guava.collect.Lists;

import com.journaldev.mvpdagger2.application.App;
import com.journaldev.mvpdagger2.data.Image.ImageRepository;
import com.journaldev.mvpdagger2.model.Converter.ImageModelConverter;
import com.journaldev.mvpdagger2.model.ImageModel;
import com.journaldev.mvpdagger2.data.Image.ImageRepositoryObserver;
import com.journaldev.mvpdagger2.model.Selectable.ImageSelectable;
import com.journaldev.mvpdagger2.model.Selectable.ImageSelectableModel;
import com.journaldev.mvpdagger2.model.Selectable.Selectable;
import com.journaldev.mvpdagger2.view.adapter.selectableAdapter.ImagesAdapter;
import com.journaldev.mvpdagger2.view.fragment.ImagesFragment.BaseGridImagesFragment;

import java.util.ArrayList;
import java.util.List;


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
