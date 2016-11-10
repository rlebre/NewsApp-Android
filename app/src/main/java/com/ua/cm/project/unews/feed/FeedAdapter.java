package com.ua.cm.project.unews.feed;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ua.cm.project.unews.R;

import java.util.List;

/**
 * Created by rui on 11/8/16.
 */

public class FeedAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final List<String> itemname;
    private final List<Bitmap> imgid;

    public FeedAdapter(Activity context, List<String> itemname, List<Bitmap> imgid) {
        super(context, R.layout.feed_list_item_layout, itemname);
        this.context = context;
        this.itemname = itemname;
        this.imgid = imgid;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.feed_list_item_layout, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.textNewsList);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        txtTitle.setText(itemname.get(position));
        imageView.setImageBitmap(imgid.get(position));
        return rowView;
    }
}
