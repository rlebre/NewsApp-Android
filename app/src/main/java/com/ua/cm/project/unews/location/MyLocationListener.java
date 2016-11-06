package com.ua.cm.project.unews.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.ua.cm.project.unews.topics_fragments.LocalFragment;

/**
 * Created by rui on 11/6/16.
 */

public class MyLocationListener implements LocationListener {
    private double currentLatitude;
    private double currentLongitude;

    public MyLocationListener() {
        currentLatitude = 0;
        currentLongitude = 0;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.e("GPS", "Latitude: " + location.getLatitude());
            Log.e("GPS", "Longitude: " + location.getLongitude());

            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
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


    public double getLatitude() {
        return currentLatitude;
    }

    public double getLongitude() {
        return currentLongitude;
    }
}
