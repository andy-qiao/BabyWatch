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

    RideStateMachine ride;

    long serviceLoadTime = System.currentTimeMillis();

    private boolean rideInProgress = false;
    private boolean rideWithBaby = false;

    private long user_havent_stopped_override = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.hasExtra("alone")) {
            rideInProgress = true;
            rideWithBaby = false;
        }

        if (intent.hasExtra("baby")) {
            rideInProgress = true;
            rideWithBaby = true;
        }

        if (intent.hasExtra("userFinishedRide")) {
            rideInProgress = false;
            rideWithBaby = false;
        }

        if (intent.hasExtra("userHaventFinishedRide")) {
            rideInProgress = true;
            rideWithBaby = true;
            user_havent_stopped_override = System.currentTimeMillis();
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


    private void signalRideStopped() {
        if (!rideInProgress) {
            return;
        }
        rideInProgress = false;

        Log.d(Config.MODULE_NAME , "stopped!!!");

        if (rideWithBaby) {
            rideWithBaby = false;

            Log.d(Config.MODULE_NAME , "stopped with baby!!!");
            Intent i = new Intent(this, FinishedRideActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }
    }

    private void signalRideInProgress() {
        if (rideInProgress) {
            return;
        }
        rideInProgress = true;
        Log.d(Config.MODULE_NAME , "a ride in progress.");

        Intent i = new Intent(this, AlertActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

    private long stoppedSince = 0;
    private long movingSince = 0;

    @Override
    public void onLocationChanged(Location location) {
        Log.d(Config.MODULE_NAME, "onLocationChanged called! bla!");

        float speed = getSpeed(location);
        Log.d(Config.MODULE_NAME , "speed=" + speed);

        if (System.currentTimeMillis() - serviceLoadTime < 5000) {
            Log.d(Config.MODULE_NAME ,"ignoring, boot");
            return;
        }

        if (speed > 0.4) { // TODO m/s

            stoppedSince = 0;
            if (movingSince == 0) {
                movingSince = System.currentTimeMillis();
            } else {
                if (System.currentTimeMillis() - movingSince > 5000) {
                    signalRideInProgress();
                }
            }

        } else {
            movingSince = 0;
            if (stoppedSince == 0) {
                stoppedSince = System.currentTimeMillis();
            } else if (user_havent_stopped_override != 0) {
                if (System.currentTimeMillis() - user_havent_stopped_override > (1 * 60 * 1000)) {
                    user_havent_stopped_override = 0;
                }

            } else {
                if (System.currentTimeMillis() - stoppedSince > 5000) {
                    signalRideStopped();
                }
            }

        }
    }

    private float getSpeed(Location location) {
//        return location.getSpeed();
        // DEBUG
        return 20;
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
