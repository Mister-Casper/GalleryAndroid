package com.journaldev.mvpdagger2.data.ImageUrls;

import com.journaldev.mvpdagger2.data.Image;

import java.util.LinkedList;

public interface ImageUrlsRepositoryObserver {

    void onUpdateImage(LinkedList<Image> updateUrls);

}
