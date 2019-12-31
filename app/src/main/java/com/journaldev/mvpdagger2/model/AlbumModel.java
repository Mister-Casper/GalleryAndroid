package com.journaldev.mvpdagger2.model;

import android.net.Uri;

import java.util.ArrayList;

public class AlbumModel {
    private final String name;

    private ArrayList<String> like = new ArrayList<>();
    private ArrayList<Uri> uri = new ArrayList<>();

    public String getName() {
        return name;
    }

    public ArrayList<Uri> getUri() {
        return uri;
    }

    public ArrayList<String> getLike() {
        return like;
    }

    public void setLike(int position, Boolean like) {
        if (like != null && this.like.size() > position)
            this.like.set(position, like.toString());
    }

    public ArrayList<ImageModel> getImages() {
        ArrayList<ImageModel> images = new ArrayList<>();

        for (int i = 0; i < uri.size(); i++) {
            images.add(new ImageModel(uri.get(i), Boolean.parseBoolean(like.get(i))));
        }

        return images;
    }

    public AlbumModel(String name, ArrayList<Uri> uri, ArrayList<String> like) {
        this.name = name;
        this.uri = uri;
        this.like = like;
    }

    public AlbumModel(String name) {
        this.name = name;
    }

}