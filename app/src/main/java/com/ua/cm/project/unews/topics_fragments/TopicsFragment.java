package com.ua.cm.project.unews.topics_fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ua.cm.project.unews.R;
import com.ua.cm.project.unews.ShowNewsFragment;
import com.ua.cm.project.unews.model.News;
import com.ua.cm.project.unews.model.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rui on 10/25/16.
 */

public class TopicsFragment extends Fragment {
    private static final String TAG = TopicsFragment.class.getSimpleName();
    private List<News> data;

    private RecyclerView mRecyclerView;
    private NewsAdapter newsAdapter;

    public static View.OnClickListener myOnClickListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new ArrayList<>();
        myOnClickListener = new MyOnClickListener();
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

        /*
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                newsAdapter.notifyDataSetChanged();
            }
        });
        */
    }

    public List<News> getData() {

        DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = mFirebaseDatabaseReference.child("feed_top").orderByChild("pub_date");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String title = (String) messageSnapshot.child("title").getValue();
                    String category = (String) messageSnapshot.child("category").getValue();
                    String creator = (String) messageSnapshot.child("creator").getValue();
                    String description = (String) messageSnapshot.child("description").getValue();
                    String link = (String) messageSnapshot.child("link").getValue();
                    String pub_date = (String) messageSnapshot.child("pub_date").getValue();
                    String service = (String) messageSnapshot.child("service").getValue();

                    String descriptionShort = description.length() < 85 ? description : description.substring(0, 85) + "...";
                    News n = new News(title, descriptionShort, description, creator, service, pub_date, category, link);
                    data.add(n);
                }

                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ERROR", databaseError.getMessage());
            }
        });

        return data;
    }

    @Override
    public void onResume() {
        newsAdapter.notifyDataSetChanged();
        super.onResume();
    }


    private class MyOnClickListener implements View.OnClickListener {


        @Override
        public void onClick(View v) {
            int selectedItemPosition = mRecyclerView.getChildAdapterPosition(v);

            ShowNewsFragment frag = ShowNewsFragment.newInstance(data.get(selectedItemPosition));

            getFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.topics_layout, frag, "NewFragmentTag")
                    .addToBackStack(null)
                    .commit();
        }
    }
}