package com.journaldev.mvpdagger2.adapters;


import android.content.Context;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


import com.journaldev.mvpdagger2.fragments.PreferenceScreen;
import com.journaldev.mvpdagger2.fragments.ViewAllImagesByDate;
import com.journaldev.mvpdagger2.fragments.albums;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Fragment[] fragments = new Fragment[3];

    public FragmentPagerAdapter(FragmentManager fm, int NumOfTabs, Fragment[] fragments) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        if (fragments.length == 3) {
            this.fragments[0] = fragments[0];
            this.fragments[1] = fragments[1];
            this.fragments[2] = fragments[2];
        }
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
