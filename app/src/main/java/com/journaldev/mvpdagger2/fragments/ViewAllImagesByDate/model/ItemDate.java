package com.journaldev.mvpdagger2.fragments.ViewAllImagesByDate.model;

import android.net.Uri;

public class ItemDate {

    private String date;

    public String getDate() {
        return date;
    }

    public Uri[] getPhoto() {
        return photo;
    }

    private Uri[] photo;

    public ItemDate(String date, Uri[] photo) {
        this.date = date;
        this.photo = photo;
    }
}
