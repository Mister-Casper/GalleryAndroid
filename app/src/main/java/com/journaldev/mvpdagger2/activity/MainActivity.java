package com.journaldev.mvpdagger2.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.journaldev.mvpdagger2.Data.AppPreference;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.adapters.FragmentPagerAdapter;
import com.journaldev.mvpdagger2.utils.ThemeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("");
        AppPreference.load(this);
        ThemeUtils.chandgeTheme(this, R.style.DarkTheme2, R.style.LightTheme2);
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        getWindow().setBackgroundDrawable(null);
    }

    private void isToSettings() {
        if (getIntent().getBooleanExtra("isOptionsChandge", false))
            toSettings();
    }

    private void toSettings() {
        viewpager.setCurrentItem(3);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // если пользователь закрыл запрос на разрешение, не дав ответа, массив grantResults будет пустым
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadActivity();
                    isToSettings();
                } else {
                    throw new SecurityException();
                }
                return;
            }
        }
    }

    private void loadActivity() {
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        final FragmentPagerAdapter adapter = new FragmentPagerAdapter
                (getSupportFragmentManager(), tabs.getTabCount());
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        setTableSelector();
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
        for (Fragment fragment: fm.getFragments()) {
            if (fragment instanceof  OnBackPressedListener) {
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

    public interface OnBackPressedListener
    {
        void onBackPressed();
    }
}

