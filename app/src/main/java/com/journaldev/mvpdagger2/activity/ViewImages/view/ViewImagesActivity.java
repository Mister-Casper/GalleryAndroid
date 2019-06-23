package com.journaldev.mvpdagger2.activity.ViewImages.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Window;
import android.view.WindowManager;

import com.journaldev.mvpdagger2.Data.ImageUrls;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.activity.ViewImages.model.ViewImagesModel;
import com.journaldev.mvpdagger2.adapters.ImagesPageAdapter;

import java.io.File;

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
        getOtherIntent();
    }

    private void getOtherIntent() {
        Uri imageUri = null;
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_VIEW.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                imageUri = intent.getData();
                Uri[] uri = ImageUrls.getUrls(getApplicationContext());
                for (int i = 0; i < uri.length; i++) {
                    File file = new File(String.valueOf(uri[i]));
                    Uri temp = Uri.fromFile(file);
                    if (imageUri.equals(temp)) ;
                    {
                        pager.setCurrentItem(i);
                        return;
                    }
                }
            }
        }
    }

    public void startActivity(Intent intent) {

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
