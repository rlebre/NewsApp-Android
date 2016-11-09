package com.ua.cm.project.unews.topics_fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.ua.cm.project.unews.R;
import com.ua.cm.project.unews.feed.FeedReader;
import com.ua.cm.project.unews.firebase.Firebase;
import com.ua.cm.project.unews.model.News;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rui on 10/25/16.
 */

public class FeedFragment extends Fragment {
    private ArrayAdapter<String> newsListAdapter;
    private ImageView img;

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed, container, false);
        img = (ImageView) view.findViewById(R.id.newsIcon);

        newsListAdapter = new ArrayAdapter<String>(
                getActivity(), // The current context (this activity)
                R.layout.feed_list_item_layout, // The name of the layout ID.
                R.id.textNewsList, // The ID of the textview to populate.
                new ArrayList<String>()); // a collection of string entries

        ListView listView = (ListView) view.findViewById(R.id.feed_listView);
        listView.setAdapter(newsListAdapter);


        List<String> subFeeds = new Firebase().getSubscribedFeeds();
        new readFeed().execute(subFeeds);
        //new Image_Async().execute();
        return view;
    }


    public class readFeed extends AsyncTask<List<String>, Void, List<News>> {
        @Override
        protected List<News> doInBackground(List<String>... params) {
            List<News> n = new ArrayList<>();
            for (String url : params[0]) {
                n.addAll(FeedReader.getFeed(url));
            }
            return n;
        }

        @Override
        protected void onPostExecute(List<News> result) {
           /* for (News n : result) {
                Log.d("HEADLINE: ", n.getTitle());
            }*/
            if (result != null) {
                for (News n : result) {
                    newsListAdapter.add(n.getTitle());
                }
            }
        }
    }


    class Image_Async extends AsyncTask<Bitmap, Bitmap, Bitmap> {

        @Override
        protected Bitmap doInBackground(Bitmap... arg0) {
            Bitmap bmp = null;
            try {
                URL url = new URL("http://static.publico.pt/files/header/img/publico.png");
                try {
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // img.setImageBitmap(result);
            // super.onPostExecute(result);
        }

    }
}
