package com.journaldev.mvpdagger2.activity.ViewImages.view;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.journaldev.mvpdagger2.activity.ViewImages.ViewImagesContract;
import com.journaldev.mvpdagger2.activity.ViewImages.model.ViewImagesModel;
import com.journaldev.mvpdagger2.activity.ViewImages.presenter.ViewImagesPresenter;
import com.journaldev.mvpdagger2.utils.OnSwipeTouchListener;
import com.journaldev.mvpdagger2.R;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewImagesActivity extends AppCompatActivity implements ViewImagesContract.ViewCallBack {

    @BindView(R.id.image)
    ImageView image;

    ViewImagesContract.PresenterCallBack presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        setContentView(R.layout.viewimages);
        ButterKnife.bind(this);

        ActivityCompat.requestPermissions(ViewImagesActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        initPresenter();
        if(savedInstanceState == null)
        selectStandartImage();
        image.setOnTouchListener(new OnSwipeTouchListener(ViewImagesActivity.this));
    }


    private void selectStandartImage() {
        int newStartImageId = getIntent().getIntExtra("idImage", 0);
        presenter.setCurrentImage(newStartImageId);
    }

    private void initPresenter() {
        ViewImagesModel model = new ViewImagesModel();
        model.init(this);
        presenter = new ViewImagesPresenter(this, model);
    }

    @Override
    public void viewImage(Uri imageUri) {
        image.setImageURI(imageUri);
    }

    public void onBackImageButton() {
        presenter.chandgeCurrentImage(-1);
    }

    public void onNextImageButton() {
        presenter.chandgeCurrentImage(1);
    }


    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}
