package com.journaldev.mvpdagger2.fragments.ViewAllImagesByDate.model;

import android.net.Uri;

public class ItemDate {

    public Uri[] getPhoto() {
        return photo;
    }

    private Uri[] photo;

    public ItemDate(Uri[] photo) {
        this.photo = photo;
    }
}
