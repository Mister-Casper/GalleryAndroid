package com.journaldev.mvpdagger2.adapters;



import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


import com.journaldev.mvpdagger2.fragments.PreferenceScreen;
import com.journaldev.mvpdagger2.fragments.ViewAllImagesByDate;
import com.journaldev.mvpdagger2.fragments.albums;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Fragment[] fragments = new Fragment[3];

    public FragmentPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        fragments[0] = new ViewAllImagesByDate();
        fragments[1] = new albums();
        fragments[2] = new PreferenceScreen();
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
