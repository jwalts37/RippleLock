package com.mobclick.android;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class e {
    private LocationManager a;
    private Context b;

    public e(Context context) {
        this.b = context;
    }

    public Location a() {
        Location lastKnownLocation;
        Location lastKnownLocation2;
        try {
            this.a = (LocationManager) this.b.getSystemService("location");
            if (!l.a(this.b, "android.permission.ACCESS_FINE_LOCATION") || (lastKnownLocation2 = this.a.getLastKnownLocation("gps")) == null) {
                if (!l.a(this.b, "android.permission.ACCESS_COARSE_LOCATION") || (lastKnownLocation = this.a.getLastKnownLocation("network")) == null) {
                    if (UmengConstants.testMode) {
                        Log.i(UmengConstants.LOG_TAG, "Could not get location from GPS or Cell-id, lack ACCESS_COARSE_LOCATION or ACCESS_COARSE_LOCATION permission?");
                    }
                    return null;
                } else if (!UmengConstants.testMode) {
                    return lastKnownLocation;
                } else {
                    Log.i(UmengConstants.LOG_TAG, "get location from network:" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude());
                    return lastKnownLocation;
                }
            } else if (!UmengConstants.testMode) {
                return lastKnownLocation2;
            } else {
                Log.i(UmengConstants.LOG_TAG, "get location from gps:" + lastKnownLocation2.getLatitude() + "," + lastKnownLocation2.getLongitude());
                return lastKnownLocation2;
            }
        } catch (Exception e) {
            if (UmengConstants.testMode) {
                Log.e(UmengConstants.LOG_TAG, "getLocation error" + e.getMessage());
            }
            return null;
        }
    }
}
