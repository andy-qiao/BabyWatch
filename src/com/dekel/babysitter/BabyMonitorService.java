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
 */
public class BabyMonitorService extends Service implements LocationListener {

    LocationManager locationManager = null;
    RideStateMachine rsm = new RideStateMachine(this); // TODO here?

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.hasExtra(Config.USER_CHOICE_ALONE_INTENT_EXTRA)) {
            rsm.userRidingAlone();
        }

        if (intent.hasExtra(Config.USER_CHOICE_BABY_INTENT_EXTRA)) {
            rsm.UserRidingWithBaby();
        }

        if (intent.hasExtra(Config.USER_CHOICE_FINISHED_RIDE_INTENT_EXTRA)) {
            rsm.userFinishedRide();
        }

        if (intent.hasExtra(Config.USER_CHOICE_HAVENT_FINISHED_RIDE_INTENT_EXTRA)) {
            rsm.userHasntFinishedRide();
        }

        // Register location manager.
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.getLastKnownLocation("speed"); // TODO is it necessary?
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, this);

        Log.d(Config.MODULE_NAME, "Registered location service!");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(Config.MODULE_NAME, "onLocationChanged called!");
        if (!location.hasSpeed()) {
            return;
        }

        float speed = getSpeed(location);
        Log.d(Config.MODULE_NAME , "speed=" + speed);

        rsm.notifySpeedChange(speed);
    }

    private float getSpeed(Location location) {
//        return Config.debug ? 20 : 0; // TODO DEBUG

        // TODO average

        return location.getSpeed();
    }

    public IBinder onBind(Intent intent) {
        return null;
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
