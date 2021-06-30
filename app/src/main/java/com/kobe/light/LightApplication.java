package com.kobe.light;

import androidx.multidex.MultiDexApplication;

import com.kobe.light.location.LocationService;

public class LightApplication extends MultiDexApplication {

    public LocationService mLocationService;
    @Override
    public void onCreate() {
        super.onCreate();
        mLocationService = new LocationService(getApplicationContext());
    }
}
