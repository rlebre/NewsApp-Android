package com.ua.cm.project.unews.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ua.cm.project.unews.R;
import com.ua.cm.project.unews.topics_fragments.TopicsFragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by rui on 10/28/16.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    private List<News> newsList = Collections.EMPTY_LIST;
    private ImageView imageView;
    private TextView titleTextView;
    private TextView descriptionTextView;

    public NewsAdapter(Context context, List<News> list) {
        inflater = LayoutInflater.from(context);
        newsList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cards_layout, parent, false);
        view.setOnClickListener(TopicsFragment.myOnClickListener);

        MyViewHolder mViewHolder = new MyViewHolder(view);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        News news = newsList.get(position);

        if (news.getImageLink() != null) {
            FetchImagesTask a = new FetchImagesTask();
            a.execute(news.getImageLink());
            try {
                Bitmap bitmap = ((BitmapDrawable) a.get()).getBitmap();
                Drawable d = new BitmapDrawable(Bitmap.createScaledBitmap(bitmap, 200, 200, true));

                imageView.setImageDrawable(d);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        titleTextView.setText(news.getTitle());
        descriptionTextView.setText(news.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            titleTextView = (TextView) itemView.findViewById(R.id.textViewTitle);
            descriptionTextView = (TextView) itemView.findViewById(R.id.textViewDescription);
        }
    }

    class FetchImagesTask extends AsyncTask<String, Void, Drawable> {

        @Override
        protected Drawable doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                InputStream content = (InputStream) url.getContent();
                Drawable d = Drawable.createFromStream(content, "src");
                return d;
            } catch (IOException e) {
                Log.d("IMAGE RECEIVER", "Error retrieving image from " + params[0]);
            }

            return null;
        }

    }
}
