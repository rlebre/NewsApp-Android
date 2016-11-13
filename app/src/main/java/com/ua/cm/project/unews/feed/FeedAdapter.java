package com.ua.cm.project.unews.feed;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ua.cm.project.unews.R;

import java.util.List;

/**
 * Created by rui on 11/8/16.
 */

public class FeedAdapter extends ArrayAdapter<String> {

    public FeedAdapter(Activity context, int layoutId, int textviewId, List<String> entries) {
        super(context, R.layout.feed_list_item_layout, textviewId, entries);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        if (position % 2 == 1) {
            view.setBackgroundColor(Color.BLUE);
        } else {
            view.setBackgroundColor(Color.CYAN);
        }

        return view;
    }
}
