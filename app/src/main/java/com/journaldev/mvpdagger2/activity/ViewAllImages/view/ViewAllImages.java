package com.journaldev.mvpdagger2.activity.ViewAllImages.view;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.activity.ViewImages.view.ViewImagesActivity;
import com.journaldev.mvpdagger2.fragments.ViewAllImagesByDate.view.ViewAllImagesByDate;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewAllImages extends AppCompatActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.frame)
    FrameLayout frame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewallimages);
        ButterKnife.bind(this);
        ActivityCompat.requestPermissions(ViewAllImages.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
// Create new fragment and transaction
        ViewAllImagesByDate newFragment = new ViewAllImagesByDate();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
        transaction.replace(R.id.frame, newFragment);
        transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();
    }


}

