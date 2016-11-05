package com.ua.cm.project.unews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.ua.cm.project.unews.model.News;

public class ShowNewsActivity extends AppCompatActivity {
    private ImageView image;
    private TextView title;
    private TextView author;
    private TextView description;
    private TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);

        image = (ImageView) findViewById(R.id.imageViewShowNewsDetails);
        title = (TextView) findViewById(R.id.titleShowNewsDetails);
        author = (TextView) findViewById(R.id.authorShowNewsDetails);
        description = (TextView) findViewById(R.id.descriptionShowNewsDetails);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            News news = (News) b.get("param1");

            title.setText(news.getTitle());
            author.setText(news.getAuthor());
            description.setText(news.getDescription());
        }
    }
}
