package com.journaldev.mvpdagger2.activity.ViewAllImages.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.adapters.FragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewAllImages extends AppCompatActivity {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewallimages);
        ButterKnife.bind(this);
        final FragmentPagerAdapter adapter = new FragmentPagerAdapter
                (getSupportFragmentManager(), tabs.getTabCount());
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        setTableSelector();
    }

    private void setTableSelector()
    {
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
        super.onBackPressed();
        finish();
    }
}

