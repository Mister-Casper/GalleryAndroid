package com.journaldev.mvpdagger2.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.model.AlbumModel;
import com.journaldev.mvpdagger2.view.fragment.ImagesFragment.gridImages.GridGetAlbumsFragment;

public class GetAlbumActivity extends BaseThemeActivity implements GridGetAlbumsFragment.GetAlbumListener {

    @Override
    public void albumSelect(AlbumModel album, boolean isCreateAlbum) {
        Intent intent = new Intent();
        intent.putExtra("albumName", album.getName());
        intent.putExtra("isCreateAlbum", isCreateAlbum);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_album);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment albumsFragment = new GridGetAlbumsFragment();
        fragmentTransaction.add(R.id.albumsFragment, albumsFragment);
        fragmentTransaction.commit();
    }

    @Override
    int getDarkTheme() {
        return R.style.DarkTheme2;
    }

    @Override
    int getLightTheme() {
        return R.style.LightTheme2;
    }

}
