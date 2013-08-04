package com.dekel.babysitter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.ActivityRecognitionClient;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: dekelna
 * Date: 7/15/13
 * Time: 11:24 PM
 */
public class BabyMonitorService extends Service implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    RideStateMachine rsm = null;

    // GPS Based
    LocationManager locationManager = null;

    // ActivityDetection
    private ActivityRecognitionClient mActivityRecognitionClient = null;
    private PendingIntent mActivityRecognitionPendingIntent = null;

    @Override
    public void onCreate() {
        super.onCreate();
        rsm = RideStateMachine.getInstance(this); // "this" can be used only in onCreate

        // Register GPS location manager.
//        InitGPS();

        // Bluetooth
//        initBluetoothDetection();

        // ActivityDetection
        initActivityDetection();

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

    private void InitGPS() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.getLastKnownLocation("speed"); // TODO is it necessary?
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    private void initBluetoothDetection() {
        // TODO should i just be a receiver? without AM? (ICS ONLY)
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, BabyMonitorReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
        am.cancel(pi);

        am.setInexactRepeating(AlarmManager.RTC,
                System.currentTimeMillis(),
                500, // AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                pi);
    }

    private void initActivityDetection() {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS) {
            Log.e(Config.MODULE_NAME, "ERROR - GooglePlayServiceUnavailable " +
                    GooglePlayServicesUtil.getErrorString(GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)));
            return;
        }

        mActivityRecognitionClient = new ActivityRecognitionClient(this, this, this);
        mActivityRecognitionPendingIntent = PendingIntent.getService(
                this, 0, new Intent(this, ActivityDetectionService.class), PendingIntent.FLAG_UPDATE_CURRENT);

        mActivityRecognitionClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(Config.MODULE_NAME, "onLocationChanged called!");

        if (override_speed == -1.0f && !location.hasSpeed()) {
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

    // ActivityDetection.
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(Config.MODULE_NAME, "Connected successfully to GooglePlay services");

        mActivityRecognitionClient.requestActivityUpdates(
                Config.IDLE_DETECTION_INTERVAL_MILLISECONDS,
                mActivityRecognitionPendingIntent);

        mActivityRecognitionClient.disconnect();
    }

    @Override
    public void onDisconnected() {
        Log.i(Config.MODULE_NAME, "onDisconnected - Everything's cool.");
        mActivityRecognitionClient = null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(Config.MODULE_NAME, "onConnectionFailed - FAILED: " + connectionResult);
    }
}
