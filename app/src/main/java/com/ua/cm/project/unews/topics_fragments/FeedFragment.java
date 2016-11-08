package com.ua.cm.project.unews.topics_fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ua.cm.project.unews.R;
import com.ua.cm.project.unews.model.News;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

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

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed, container, false);

        newsListAdapter = new ArrayAdapter<String>(
                getActivity(), // The current context (this activity)
                R.layout.feed_list_item_layout, // The name of the layout ID.
                R.id.textNewsList, // The ID of the textview to populate.
                new ArrayList<String>()); // a collection of string entries

        ListView listView = (ListView) view.findViewById(R.id.feed_listView);
        listView.setAdapter(newsListAdapter);

        new readFeed().execute("http://feeds.feedburner.com/PublicoRSS");

        return view;
    }


    public class readFeed extends AsyncTask<String, Void, List<News>> {
        @Override
        protected List<News> doInBackground(String... params) {
            List<News> n = new ArrayList<>();
            for (String url : params) {
                n.addAll(getFeed(url));
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

        private List<News> getFeed(String link) {
            List<News> newsList = new ArrayList<>();
            try {
                URL url = new URL(link);
                News n = new News();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(url.openStream(), "UTF_8");

                boolean insideItem = false;

                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        switch (xpp.getName().toLowerCase()) {
                            case "title":
                                if (insideItem == false) {
                                    n.setService(xpp.nextText());
                                } else {
                                    n.setTitle(xpp.nextText());
                                }
                                break;
                            case "item":
                                insideItem = true;
                                break;
                            case "link":
                                if (insideItem) {
                                    n.setLink(xpp.nextText());
                                }
                                break;
                            case "category":
                                if (insideItem) {
                                    n.setCategory(xpp.nextText());
                                }
                                break;
                            case "creator":
                            case "dc:creator":
                            case "author":
                                if (insideItem) {
                                    n.setAuthor(xpp.nextText());
                                }
                                break;
                            case "description":
                                if (insideItem) {
                                    String description = xpp.nextText();
                                    n.setDescription(description);
                                    n.setShortDescription(description.length() < 85 ? description : description.substring(0, 85) + "...");
                                }
                                break;
                            case "pubdate":
                                if (insideItem) {
                                    n.setPub_date(xpp.nextText());
                                }
                                break;
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = false;
                        n.removeTags();
                        newsList.add(new News(n));
                        n.clear();
                    }

                    eventType = xpp.next(); //move to next element
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return newsList;
        }
    }

}
