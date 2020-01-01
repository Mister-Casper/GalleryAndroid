package com.journaldev.mvpdagger2.model;

import java.util.ArrayList;

public class AlbumModel {
    private final String name;
    private ArrayList<ImageModel> images = new ArrayList<>();

    public AlbumModel(String name, ArrayList<ImageModel> images) {
        this.name = name;
        this.images = images;
    }

    public AlbumModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ImageModel> getImages() {
        return images;
    }
    public void setLike(int position, Boolean like) {
        if (like != null && this.images.size() > position)
            this.images.get(position).setLike(like);
    }

}