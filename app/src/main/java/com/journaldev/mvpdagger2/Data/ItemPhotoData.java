package com.journaldev.mvpdagger2.Data;

import android.net.Uri;

public class ItemPhotoData {

    public Uri getPhoto() {
        return photo;
    }
    public Boolean getLike() {
        return isLike;
    }

    private Uri photo;
    private Boolean isLike;



    public ItemPhotoData(Uri photo , Boolean isLike) {
        this.photo = photo;
        this.isLike = isLike;
    }
}
