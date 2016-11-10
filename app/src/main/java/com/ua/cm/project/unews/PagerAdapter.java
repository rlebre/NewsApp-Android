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

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return TopicsFragment.newInstance();
            case 1:
                return FeedFragment.newInstance();
            case 2:
                return LocalFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}