package com.ua.cm.project.unews;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ua.cm.project.unews.model.News;


public class ShowNewsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private ImageView image;
    private TextView title;
    private TextView author;
    private TextView description;
    private TextView date;

    public ShowNewsFragment() {
        // Required empty public constructor
    }

    public static ShowNewsFragment newInstance(News news) {
        ShowNewsFragment fragment = new ShowNewsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, news);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_news, container, false);
        if (container != null) {
            container.removeAllViews();
        }

        image = (ImageView) view.findViewById(R.id.imageViewShowNewsDetails);
        title = (TextView) view.findViewById(R.id.titleShowNewsDetails);
        author = (TextView) view.findViewById(R.id.authorShowNewsDetails);
        description = (TextView) view.findViewById(R.id.descriptionShowNewsDetails);

        if (getArguments() != null) {
            News news = (News) getArguments().get(ARG_PARAM1);
            title.setText(news.getTitle());
            author.setText(news.getAuthor());
            description.setText(news.getDescription());
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
