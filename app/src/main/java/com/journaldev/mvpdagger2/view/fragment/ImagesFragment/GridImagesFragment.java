package com.journaldev.mvpdagger2.view.fragment.ImagesFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.journaldev.mvpdagger2.data.Image.ImageRepository;
import com.journaldev.mvpdagger2.model.ImageModel;
import com.journaldev.mvpdagger2.data.Image.ImageRepositoryObserver;
import com.journaldev.mvpdagger2.view.fragment.ImagesFragment.BaseGridImagesFragment;

import java.util.ArrayList;


public class GridImagesFragment extends BaseGridImagesFragment implements ImageRepositoryObserver {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageRepository.ImageObserver.addImageUrlsRepositoryObserver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ImageRepository.ImageObserver.removeImageUrlsRepositoryObserver(this);
    }

    @Override
    public void onUpdateImage(ArrayList<ImageModel> updateUrls) {
        this.images = updateUrls;
        adapter.setImages(images);
    }
}
