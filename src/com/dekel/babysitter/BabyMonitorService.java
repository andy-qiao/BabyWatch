package com.dekel.babysitter;

import android.app.AlarmManager;
import android.app.IntentService;
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
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

/**
 * Created with IntelliJ IDEA.
 * User: dekelna
 * Date: 7/15/13
 * Time: 11:24 PM
 */
public class BabyMonitorService extends Service implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    LocationManager locationManager = null;
    RideStateMachine rsm = null;

    @Override
    public void onCreate() {
        super.onCreate();
        rsm = new RideStateMachine(this);

        // Register location manager.
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.getLastKnownLocation("speed"); // TODO is it necessary?
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        // initializeAlarmManager(); // TODO for BT
        registerGooglePlay();


        Log.d(Config.MODULE_NAME, "Registered location service!");

    }

    // Constants that define the activity detection interval
    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int DETECTION_INTERVAL_SECONDS = 20;
    public static final int DETECTION_INTERVAL_MILLISECONDS =
            MILLISECONDS_PER_SECOND * DETECTION_INTERVAL_SECONDS;
    private PendingIntent mActivityRecognitionPendingIntent;
    // Store the current activity recognition client
    private ActivityRecognitionClient mActivityRecognitionClient;

    private void registerGooglePlay() {
        mActivityRecognitionClient =
                new ActivityRecognitionClient(this, this, this);
        /*
         * Create the PendingIntent that Location Services uses
         * to send activity recognition updates back to this app.
         */
        Intent intent = new Intent(
                this, GPService.class);
        /*
         * Return a PendingIntent that starts the IntentService.
         */
        mActivityRecognitionPendingIntent =
                PendingIntent.getService(this, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        mActivityRecognitionClient.connect();
    }

    private void initializeAlarmManager() {
        // TODO should i just be a receiver? without AM?
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, BabyMonitorReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
        am.cancel(pi);

        am.setInexactRepeating(AlarmManager.RTC,
                System.currentTimeMillis(),
                500, // AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                pi);
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

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(Config.MODULE_NAME, "onConnected" + bundle);

        mActivityRecognitionClient.requestActivityUpdates(
                0,
                mActivityRecognitionPendingIntent);

        startService(new Intent(this, BabyMonitorService.class).putExtra("bla","bla"));
        try {
            mActivityRecognitionPendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        mActivityRecognitionClient.disconnect();
    }

    @Override
    public void onDisconnected() {
        Log.i(Config.MODULE_NAME, "onDisconnected");
        mActivityRecognitionClient = null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(Config.MODULE_NAME, "onConnectionFailed - FAILED." + connectionResult);
    }
}
