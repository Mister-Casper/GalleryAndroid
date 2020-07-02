package com.journaldev.mvpdagger2.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.view.fragment.ImagesFragment.gridImages.album.GridAlbumImagesFragment;
import butterknife.ButterKnife;

public class ViewImagesGridActivity extends BaseThemeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_images_grid);
        ButterKnife.bind(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment baseGridImages = new GridAlbumImagesFragment();
        Bundle imagesBundle = getIntent().getExtras();
        baseGridImages.setArguments(imagesBundle);
        fragmentTransaction.add(R.id.imagesFragment, baseGridImages);
        fragmentTransaction.commit();

        setTitle(imagesBundle.getString("albumName"));
    }

    private void setTitle(String albumName) {
        getSupportActionBar().setTitle(albumName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    int getDarkTheme() {
        return R.style.DarkTheme3;
    }

    @Override
    int getLightTheme() {
        return R.style.LightTheme3;
    }
}
