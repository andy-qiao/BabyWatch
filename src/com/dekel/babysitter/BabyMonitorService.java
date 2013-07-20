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
    RideStateMachine rsm = null;

    @Override
    public void onCreate() {
        super.onCreate();
        rsm = new RideStateMachine(this);

        // Register location manager.
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.getLastKnownLocation("speed"); // TODO is it necessary?
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, this);

        Log.d(Config.MODULE_NAME, "Registered location service!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null) return START_STICKY;

        if (intent.hasExtra("DEBUG")) {
            if (override_speed == -1.0f) override_speed = 0.0f;
            onLocationChanged(null);
        }

        if (intent.hasExtra(Config.DEBUG_SPEED_INTENT_EXTRA)) {
            override_speed = intent.getFloatExtra(Config.DEBUG_SPEED_INTENT_EXTRA, -1);
        }

        if (intent.hasExtra(Config.USER_CHOICE_ALONE_INTENT_EXTRA)) {
            rsm.userChoiceRidingAlone();
        }

        if (intent.hasExtra(Config.USER_CHOICE_BABY_INTENT_EXTRA)) {
            rsm.UserChoiceRidingWithBaby();
        }

        if (intent.hasExtra(Config.USER_CHOICE_FINISHED_RIDE_INTENT_EXTRA)) {
            rsm.userChoiseFinishedRide();
        }

        if (intent.hasExtra(Config.USER_CHOICE_HAVENT_FINISHED_RIDE_INTENT_EXTRA)) {
            rsm.userChoiceHasntFinishedRide();
        }

        if (intent.hasExtra(Config.USER_CHOICE_HAVENT_FINISHED_RIDE_INTENT_EXTRA)) {
            rsm.userChoiceHasntFinishedRide();
        }

        return START_STICKY;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(Config.MODULE_NAME, "onLocationChanged called!");
        if (!location.hasSpeed() || override_speed != -1.0f) {
            return;
        }

        float speed = getSpeed(location);
        Log.d(Config.MODULE_NAME , "speed=" + speed);

        rsm.notifySpeedChange(speed);
    }

    private float override_speed = -1.0f;
    private float getSpeed(Location location) {
        if (override_speed != -1.0f) {
            return override_speed;
        }

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
