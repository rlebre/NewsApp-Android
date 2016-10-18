package com.ua.cm.project.unews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TopicsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
}
