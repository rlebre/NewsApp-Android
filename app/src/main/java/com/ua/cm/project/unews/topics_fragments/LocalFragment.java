package com.ua.cm.project.unews.topics_fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ua.cm.project.unews.R;
import com.ua.cm.project.unews.model.News;
import com.ua.cm.project.unews.model.NewsAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by rui on 10/25/16.
 */

public class LocalFragment extends Fragment {
    TextView locationTextView;
    private List<News> data;

    private RecyclerView mRecyclerView;
    private NewsAdapter newsAdapter;

    public static View.OnClickListener myOnClickListener;

    private LocationManager locationManager;

    private String currentLocation;

    public static LocalFragment newInstance() {
        return new LocalFragment();
    }

    public LocalFragment() {
        data = new ArrayList<>();
        currentLocation = "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.local, container, false);

        locationTextView = (TextView) layout.findViewById(R.id.text_local);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.local_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        newsAdapter = new NewsAdapter(getActivity(), getData());
        mRecyclerView.setAdapter(newsAdapter);

        Location l = getLastKnownLocation();
        try {
            locationTextView.setVisibility(View.INVISIBLE);
            //locationTextView.setText(l.getLatitude() + ":" + l.getLongitude() + ", " + getCurrentCity(l.getLatitude(), l.getLongitude()));
            if (l != null) {
                currentLocation = getCurrentCity(l.getLatitude(), l.getLongitude());

                currentLocation = currentLocation.equals("Dalvik") ? "Aveiro" : currentLocation;
            } else {
                currentLocation = "Aveiro";
            }

            if (currentLocation.equals("") || currentLocation.equals("Dalvik")) {
                locationTextView.setVisibility(View.VISIBLE);
                locationTextView.setText("No news in current location");
                mRecyclerView.setVisibility(View.INVISIBLE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return layout;
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

                    String local = (String) messageSnapshot.child("local").getValue();
                    if (local != null) {
                        if (local.equals(currentLocation)) {
                            data.add(n);
                        }
                    }
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

    private Location getLastKnownLocation() {
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }

            locationManager.requestLocationUpdates(provider, 100000, 0,
                    new LocationListener() {

                        public void onLocationChanged(Location location) {
                        }

                        public void onProviderDisabled(String provider) {
                        }

                        public void onProviderEnabled(String provider) {
                        }

                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }
                    });

            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }

        return bestLocation;
    }

    private String getCurrentCity(double latitude, double longitude) throws IOException {
        if (getContext() != null) {
            Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                return addresses.get(0).getLocality();
            }
        }
        return "";
    }
}
