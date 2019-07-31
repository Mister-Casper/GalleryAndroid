package com.journaldev.mvpdagger2.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.journaldev.mvpdagger2.fragments.ViewAllImagesByDate;
import com.journaldev.mvpdagger2.fragments.albums;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Fragment[] fragments = new Fragment[2];

    public FragmentPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        fragments[0] = new ViewAllImagesByDate();
        fragments[1] = new albums();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
