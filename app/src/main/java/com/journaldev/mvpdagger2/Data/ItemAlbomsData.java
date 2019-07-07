package com.journaldev.mvpdagger2.Data;

import android.net.Uri;

import java.util.ArrayList;

public class ItemAlbomsData {

    ArrayList<Uri> uri;
    String name;

    public ItemAlbomsData(ArrayList<Uri> uri, String name) {
        this.uri = uri;
        this.name = name;
    }

}
