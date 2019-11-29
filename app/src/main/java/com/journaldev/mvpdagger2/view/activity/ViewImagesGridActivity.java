package com.journaldev.mvpdagger2.view.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.utils.ThemeUtils;
import com.journaldev.mvpdagger2.view.fragment.BaseGridImagesFragment;



public class ViewImagesGridActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.changeTheme(this, R.style.DarkTheme2, R.style.LightTheme2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_images_grid);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment baseGridImages = new BaseGridImagesFragment();
        Bundle images = getIntent().getExtras();
        baseGridImages.setArguments(images);
        fragmentTransaction.add(R.id.imagesFragment,baseGridImages);
        fragmentTransaction.commit();
    }


}
