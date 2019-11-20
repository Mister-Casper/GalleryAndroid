package com.journaldev.mvpdagger2.data;

import android.net.Uri;

public class ItemPhotoData {

    public Uri getPhoto() {
        return photo;
    }
    public Boolean getLike() {
        return isLike;
    }

    public void setLike(Boolean value) {
        isLike = value;
    }

    private Uri photo;
    private Boolean isLike;



    public ItemPhotoData(Uri photo , Boolean isLike) {
        this.photo = photo;
        this.isLike = isLike;
    }
}
