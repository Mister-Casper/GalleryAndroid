package com.journaldev.mvpdagger2.activity.ViewAllImages.view;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.fragments.ViewAllImagesByDate.ViewAllImagesByDate;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewAllImages extends AppCompatActivity {

    @BindView(R.id.frame)
    FrameLayout frame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewallimages);
        ButterKnife.bind(this);

        ViewAllImagesByDate newFragment = new ViewAllImagesByDate();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.frame, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

