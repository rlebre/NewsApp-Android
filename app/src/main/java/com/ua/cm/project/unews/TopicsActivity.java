package com.ua.cm.project.unews;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.ua.cm.project.unews.firebase.Firebase;
import com.ua.cm.project.unews.topics_fragments.FeedFragment;
import com.ua.cm.project.unews.topics_fragments.LocalFragment;
import com.ua.cm.project.unews.topics_fragments.TopicsFragment;

public class TopicsActivity extends AppCompatActivity {
    private Firebase firebase;
    private FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        firebase = new Firebase();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.topics));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.feed));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.local));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), 3);
        viewPager.setAdapter(adapterViewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            case R.id.action_logout:
                firebase.logout();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int mNumOfTabs;

        public MyPagerAdapter(FragmentManager fm, int numOfTabs) {
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
}

