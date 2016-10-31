package com.ua.cm.project.unews.topics_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.ua.cm.project.unews.R;
import com.ua.cm.project.unews.model.News;
import com.ua.cm.project.unews.model.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rui on 10/25/16.
 */

public class TopicsFragment extends Fragment {
    private static final String TAG = TopicsFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private NewsAdapter newsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.topics, container, false);

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.news_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        newsAdapter = new NewsAdapter(getActivity(), getData());

        mRecyclerView.setAdapter(newsAdapter);

        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //mCardView = (CardView) view.findViewById(R.id.cardview);
    }

    public static List<News> getData() {
        List<News> data = new ArrayList<>();
        int[] icons = {R.drawable.com_facebook_favicon_blue, R.drawable.com_facebook_favicon_blue, R.drawable.com_facebook_favicon_blue, R.drawable.com_facebook_favicon_blue, R.drawable.com_facebook_favicon_blue};
        String[] titles = {"Title 1", "Title 2", "Title 3", "Title 4", "Title 5"};
        String[] descriptions = {"Description 1", "Description 2", "Description 3", "Description 4", "Description 5"};
        String[] categories = {"Category 1", "Category 2", "Category 3", "Category 4", "Category 5"};
        String[] urls = {"URL 1", "URL 2", "URL 3", "URL 4", "URL 5"};

        for (int i = 0; i < icons.length && i < titles.length && i < descriptions.length; i++) {
            data.add(i, new News(titles[i], descriptions[i], categories[i], urls[i]));
        }

        return data;
    }
}