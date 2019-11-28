package com.journaldev.mvpdagger2.model;

import android.net.Uri;

public class ImageModel {

    private Uri photo;
    private Boolean isLike;

    public Uri getPhoto() {
        return photo;
    }

    public Boolean getLike() {
        return isLike;
    }
    public void setLike(Boolean value) {
        isLike = value;
    }

    public ImageModel(Uri photo , Boolean isLike) {
        this.photo = photo;
        this.isLike = isLike;
    }
}
