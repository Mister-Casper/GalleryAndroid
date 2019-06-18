package com.journaldev.mvpdagger2.activity.ViewImages.view;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


    @BindView(R.id.backImageButton)
    Button backImageButton;
    @BindView(R.id.nextImageButton)
    Button nextImageButton;
    @BindView(R.id.image)
    ImageView image;

    ViewImagesContract.PresenterCallBack presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewimages);
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
