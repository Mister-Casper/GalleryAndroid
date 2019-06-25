package com.journaldev.mvpdagger2.activity.ViewImages.view;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.journaldev.mvpdagger2.Data.ImageUrls;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.activity.FileInfo.FIleInfo;
import com.journaldev.mvpdagger2.adapters.ImagesPageAdapter;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ViewImagesActivity extends AppCompatActivity {

    @BindView(R.id.pager)
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("");
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
        ColorDrawable abDrawable = new ColorDrawable(getResources().getColor(R.color.gray));
        abDrawable.setAlpha(0);
        getSupportActionBar().setBackgroundDrawable(abDrawable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.file_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.info:
                Intent intent = new Intent(getApplicationContext(), FIleInfo.class);
                Uri currentUri = ImageUrls.getImage(pager.getCurrentItem());
                intent.putExtra("uri", currentUri.toString());
                getApplicationContext().startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void allScreen() {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }


    private void selectStandartImage() {
        int newStartImageId = getIntent().getIntExtra("idImage", 0);
        pager.setCurrentItem(newStartImageId);
    }
}
