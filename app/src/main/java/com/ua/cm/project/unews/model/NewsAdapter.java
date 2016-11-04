package com.ua.cm.project.unews.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ua.cm.project.unews.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Created by rui on 10/28/16.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    private List<News> newsList = Collections.EMPTY_LIST;

    public NewsAdapter(Context context, List<News> list) {
        inflater = LayoutInflater.from(context);
        newsList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cards_layout, parent, false);
        MyViewHolder mViewHolder = new MyViewHolder(view);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        News news = newsList.get(position);

        holder.titleTextView.setText(news.getTitle());
        holder.descriptionTextView.setText(news.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView descriptionTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            titleTextView = (TextView) itemView.findViewById(R.id.textViewTitle);
            descriptionTextView = (TextView) itemView.findViewById(R.id.textViewDescription);
        }
    }
}
