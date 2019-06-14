package com.journaldev.mvpdagger2.ViewImages.view;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

import com.journaldev.mvpdagger2.MainContract;
import com.journaldev.mvpdagger2.ViewImages.model.ViewImagesModel;
import com.journaldev.mvpdagger2.utils.OnSwipeTouchListener;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.ViewImages.presenter.ViewImagesPresenter;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewImagesActivity extends AppCompatActivity implements MainContract.ViewCallBack {


    @BindView(R.id.backImageButton)
    Button backImageButton;
    @BindView(R.id.nextImageButton)
    Button nextImageButton;
    @BindView(R.id.image)
    ImageView image;

    MainContract.PresenterCallBack presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ActivityCompat.requestPermissions(ViewImagesActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        initPresenter();
        presenter.chandgeCurrentImage(0);
        image.setOnTouchListener(new OnSwipeTouchListener(ViewImagesActivity.this));
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

    @OnClick(R.id.backImageButton)
    public void onBackImageButton() {
        presenter.chandgeCurrentImage(-1);
    }

    @OnClick(R.id.nextImageButton)
    public void onNextImageButton() {
        presenter.chandgeCurrentImage(1);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}
