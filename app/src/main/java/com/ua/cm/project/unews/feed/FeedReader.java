package com.ua.cm.project.unews.feed;

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
 * Created by rui on 11/8/16.
 */

public class FeedReader {
    public static List<News> getFeed(String link) {
        List<News> newsList = new ArrayList<>();
        try {
            URL url = new URL(link);
            News n = new News();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(url.openStream(), "UTF-8");

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
