package com.journaldev.mvpdagger2.data.Image;

import com.journaldev.mvpdagger2.model.ImageModel;

import java.util.LinkedList;

public interface ImageRepositoryObserver {

    void onUpdateImage(LinkedList<ImageModel> updateUrls);

}
