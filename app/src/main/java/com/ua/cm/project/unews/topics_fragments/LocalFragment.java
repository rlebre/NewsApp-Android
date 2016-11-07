package com.ua.cm.project.unews.topics_fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ua.cm.project.unews.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by rui on 10/25/16.
 */

public class LocalFragment extends Fragment {
    LocationManager myManager;
    TextView a;

    private LocationManager locationManager;
    private String provider;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.local, container, false);

        a = (TextView) layout.findViewById(R.id.text_local);

        myManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location l = locationManager.getLastKnownLocation(provider);
        try {
            a.setText(l.getLatitude() + ":" + l.getLongitude() + ", " + getCurrentCity(l.getLatitude(), l.getLongitude()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return layout;
    }


    private String getCurrentCity(double latitude, double longitude) throws IOException {
        if (getContext() != null) {
            Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                return addresses.get(0).getLocality();
            }
        }
        return "Not Found";
    }
}
