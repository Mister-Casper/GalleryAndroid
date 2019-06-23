package com.journaldev.mvpdagger2.activity.ViewImages.view;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
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


public class ViewImagesActivity extends AppCompatActivity implements ViewImagesContract.ViewCallBack {

    @BindView(R.id.image)
    ImageView image;

    ViewImagesContract.PresenterCallBack presenter;

    private float scaleImageFactor = 1.0f;
    private ScaleGestureDetector scaleGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allScreen();
        setContentView(R.layout.viewimages);
        ButterKnife.bind(this);

        ActivityCompat.requestPermissions(ViewImagesActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        initPresenter();
        if (savedInstanceState == null)
            selectStandartImage();
        image.setOnTouchListener(new OnSwipeTouchListener(ViewImagesActivity.this));
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    private void allScreen() {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }


    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
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
        scaleImageFactor = 1f;
        image.setImageURI(imageUri);
        chandgeScale();
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

    private void chandgeScale() {
        image.setScaleX(scaleImageFactor);
        image.setScaleY(scaleImageFactor);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            scaleImageFactor *= scaleGestureDetector.getScaleFactor();
            scaleImageFactor = Math.max(0.1f,
                    Math.min(scaleImageFactor, 10.0f));
            image.setScaleX(scaleImageFactor);
            image.setScaleY(scaleImageFactor);
            return true;
        }
    }

}
