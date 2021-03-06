package com.ua.cm.project.unews.topics_fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ua.cm.project.unews.R;
import com.ua.cm.project.unews.ShowNewsActivity;
import com.ua.cm.project.unews.feed.FeedReader;
import com.ua.cm.project.unews.firebase.Firebase;
import com.ua.cm.project.unews.model.News;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rui on 10/25/16.
 */

public class FeedFragment extends Fragment {
    private ArrayAdapter<String> newsListAdapter;
    private ImageView img;
    private Firebase firebase;
    private List<News> newsList;
    private ListView listView;
    private static AdapterView.OnItemClickListener myOnClickListener;
    private String service_title;
    private String service_link;
   private FloatingActionButton fab;

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed, container, false);
        img = (ImageView) view.findViewById(R.id.newsIcon);
        firebase = new Firebase();
        newsList = new ArrayList<>();
        myOnClickListener = new MyOnClickListener();
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        setFloatingButton();

        newsListAdapter = new ArrayAdapter<String>(
                getActivity(), // The current context (this activity)
                R.layout.feed_list_item_layout, // The name of the layout ID.
                R.id.textNewsList, // The ID of the textview to populate.
                new ArrayList<String>()); // a collection of string entries

        listView = (ListView) view.findViewById(R.id.feed_listView);
        listView.setAdapter(newsListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ShowNewsActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("param1", newsList.get(position));
                intent.putExtras(b);

                startActivity(intent);
            }
        });


        //String[] urls = {"http://www.jornaldenegocios.pt/rss", "http://feeds.feedburner.com/PublicoRSS", "http://www.rtp.pt/noticias/rss", "http://feeds.feedburner.com/expresso-geral"};

        getFeedList();

        //new Image_Async().execute();
        return view;
    }

    private void getFeedList(){
        Query query = firebase.getDatabaseReference().child("users").child(firebase.getUserID()).child("subscribed_feeds").orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> feed_list = new ArrayList<>();
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        feed_list.add((String) s.getValue());
                    }
                    new readFeed().execute(feed_list);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setFloatingButton(){

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("New Service");

                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText service = new EditText(getContext());
                service.setHint("Name of service");
                service.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
                layout.addView(service);
                final EditText link = new EditText(getContext());
                link.setHint("Link to RSS");
                link.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
                layout.addView(link);
                builder.setView(layout);


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        service_title = service.getText().toString();
                        service_link = link.getText().toString();

                        String uid = firebase.getUserID();
                        if(!service_link.startsWith("http://")){
                            service_link = "http://" + service_link;
                        }
                        firebase.getDatabaseReference().child("users").child(uid).child("subscribed_feeds").child(service_title).setValue(service_link);

                        getFeedList();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
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
            if (result != null) {
                for (News n : result) {
                    newsListAdapter.add(n.getTitle());
                    newsList.add(n);
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
        }

    }

    private class MyOnClickListener implements AdapterView.OnItemClickListener {


        public void onClick(View v) {

        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int selectedItemPosition = listView.getSelectedItemPosition();

            /*ShowNewsFragment frag = ShowNewsFragment.newInstance(data.get(selectedItemPosition));

            getFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.topics_layout, frag, "NewFragmentTag")
                    .addToBackStack(null)
                    .commit();
                    */
            Intent intent = new Intent(getContext(), ShowNewsActivity.class);
            Bundle b = new Bundle();
            b.putSerializable("param1", newsList.get(selectedItemPosition));
            intent.putExtras(b);

            startActivity(intent);
        }
    }
}
