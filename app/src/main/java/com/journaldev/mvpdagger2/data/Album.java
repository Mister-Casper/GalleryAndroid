package com.journaldev.mvpdagger2.data;

import android.net.Uri;

import java.util.ArrayList;

public class Album {
    private String name;
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

    public void setLike(int position , Boolean like) {
        if (like != null && this.like.size() > position)
            this.like.set(position,like.toString());
    }

    Album(String name, ArrayList<Uri> uri, ArrayList<String> like) {
        this.name = name;
        this.uri = uri;
        this.like = like;
    }

    Album(String name) {
        this.name = name;
    }

}