package com.ua.cm.project.unews.topics_fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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

public class LocalFragment extends Fragment implements LocationListener {
    LocationManager myManager;
    //MyLocationListener loc;
    TextView a;

    private LocationManager locationManager;
    private String provider;

    public static LocalFragment newInstance() {


        return new LocalFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.local, container, false);

        return view;
    }

    private String getCurrentCity(double latitude, double longitude) throws IOException {
        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
        if (addresses.size() > 0) {
            System.out.println(addresses.get(0).getLocality());
            return addresses.get(0).getLocality();
        }
        return "Not Found";
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            getCurrentCity(location.getLatitude(), location.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
