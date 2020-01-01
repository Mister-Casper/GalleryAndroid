package com.journaldev.mvpdagger2.application;

import android.app.Application;

import com.journaldev.mvpdagger2.data.Album.AlbumRepository;
import com.journaldev.mvpdagger2.data.Image.ImageRepository;

public class App extends Application {
    private static App app;
    private static AppPreference appPreference;
    private static ImageRepository imageRepository;
    private static AlbumRepository albumRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        appPreference = new AppPreference(app);
        imageRepository = new ImageRepository(app);
        albumRepository = new AlbumRepository(app);
    }

    public static App getApp() {
        return app;
    }

    public static AppPreference getAppPreference() {
        return appPreference;
    }

    public static ImageRepository getImageRepository() {
        return imageRepository;
    }

    public static AlbumRepository getAlbumRepository() {
        return albumRepository;
    }
}
