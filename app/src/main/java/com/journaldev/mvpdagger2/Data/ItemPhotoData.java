package com.journaldev.mvpdagger2.Data;

import android.net.Uri;

public class ItemPhotoData {

    public Uri getPhoto() {
        return photo;
    }

    private Uri photo;

    public ItemPhotoData(Uri photo) {
        this.photo = photo;
    }
}
