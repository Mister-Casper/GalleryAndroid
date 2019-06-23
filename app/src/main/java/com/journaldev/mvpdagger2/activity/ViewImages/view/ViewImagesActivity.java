package com.journaldev.mvpdagger2.activity.ViewImages.view;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Window;
import android.view.WindowManager;

import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.activity.ViewImages.model.ViewImagesModel;
import com.journaldev.mvpdagger2.adapters.ImagesPageAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ViewImagesActivity extends AppCompatActivity {


    //  ViewImagesContract.PresenterCallBack presenter;
    @BindView(R.id.pager)
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allScreen();
        setContentView(R.layout.viewimages);
        ButterKnife.bind(this);

        ImagesPageAdapter mCustomPagerAdapter = new ImagesPageAdapter(this);
        pager.setAdapter(mCustomPagerAdapter);
        pager.setOffscreenPageLimit(3);
        if (savedInstanceState == null)
            selectStandartImage();
    }

    private void allScreen() {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }


    private void selectStandartImage() {
        int newStartImageId = getIntent().getIntExtra("idImage", 0);
        pager.setCurrentItem(newStartImageId);
    }
}
