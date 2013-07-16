package com.dekel.babysitter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: dekelna
 * Date: 7/15/13
 * Time: 11:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class BabyMonitorService extends Service implements LocationListener {

    LocationManager locationManager = null;
    RideStateMachine rsm = new RideStateMachine(this);

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.hasExtra("alone")) {
            rsm.userRidingAlone();
        }

        if (intent.hasExtra("baby")) {
            rsm.UserRidingWithBaby();
        }

        if (intent.hasExtra("userFinishedRide")) {
            rsm.userFinishedRide();
        }

        if (intent.hasExtra("userHaventFinishedRide")) {
            rsm.userHasntFinishedRide();
        }

        // Register location manager.
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.getLastKnownLocation("speed");
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, this);

        Log.d(Config.MODULE_NAME, "Registered location bla!");

        return super.onStartCommand(intent, flags, startId);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(Config.MODULE_NAME, "onLocationChanged called! bla!");

        float speed = getSpeed(location);
        Log.d(Config.MODULE_NAME , "speed=" + speed);

        rsm.notifySpeedChange(speed);
    }

    private float getSpeed(Location location) {
        // TODO average
        return location.getSpeed();
        // DEBUG

//        return Config.debug ? 20 : 0;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }
}
