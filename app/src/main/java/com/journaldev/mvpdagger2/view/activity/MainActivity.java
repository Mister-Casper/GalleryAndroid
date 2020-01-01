package com.journaldev.mvpdagger2.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.view.adapter.FragmentPagerAdapter;
import com.journaldev.mvpdagger2.view.fragment.*;
import com.journaldev.mvpdagger2.view.fragment.ImagesFragment.gridImages.GridAlbumsFragment;
import com.journaldev.mvpdagger2.view.fragment.ImagesFragment.gridImages.GridImagesFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseThemeActivity {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("");
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        getWindow().setBackgroundDrawable(null);
    }

    private void isToSettings() {
        if (getIntent().getBooleanExtra("isOptionsChange", false))
            toSettings();
    }

    private void toSettings() {
        viewpager.setCurrentItem(3);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 1 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadActivity();
                isToSettings();
            } else {
                throw new SecurityException();
            }
        }
    }

    private void loadActivity() {
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        viewpager.setAdapter(loadAdapter());
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        setTableSelector();
        viewpager.setOffscreenPageLimit(3);
    }

    private FragmentPagerAdapter loadAdapter() {
        GridImagesFragment fragment1 = new GridImagesFragment();
        GridAlbumsFragment fragment2 = new GridAlbumsFragment();
        PreferenceScreenFragment fragment3 = new PreferenceScreenFragment();

        final FragmentPagerAdapter adapter = new FragmentPagerAdapter
                (getSupportFragmentManager(), tabs.getTabCount(), new Fragment[]{fragment1, fragment2, fragment3});

        return adapter;
    }

    private void setTableSelector() {
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        OnBackPressedListener backPressedListener = null;
        for (Fragment fragment : fm.getFragments()) {
            if (fragment instanceof OnBackPressedListener) {
                backPressedListener = (OnBackPressedListener) fragment;
                break;
            }
        }

        if (backPressedListener != null) {
            backPressedListener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    public interface OnBackPressedListener {
        void onBackPressed();
    }

    @Override
    int getDarkTheme() {
        return R.style.DarkTheme2;
    }

    @Override
    int getLightTheme() {
        return R.style.LightTheme2;
    }
}

