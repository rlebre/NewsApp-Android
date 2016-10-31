package com.ua.cm.project.unews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ua.cm.project.unews.topics_fragments.FeedFragment;
import com.ua.cm.project.unews.topics_fragments.LocalFragment;
import com.ua.cm.project.unews.topics_fragments.TopicsFragment;

/**
 * Created by rui on 10/28/16.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TopicsFragment tab1 = new TopicsFragment();
                return tab1;
            case 1:
                FeedFragment tab2 = new FeedFragment();
                return tab2;
            case 2:
                LocalFragment tab3 = new LocalFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}