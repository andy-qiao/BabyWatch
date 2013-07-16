package com.dekel.babysitter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: dekelna
 * Date: 7/15/13
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class BabyMonitorReceiver extends BroadcastReceiver {
    private Context context;
    public void onReceive(Context context, Intent intent) {
        //Log.d("bla", "XXXX!!");
        this.context = context;

        //LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);



    }

    private void checkMotion() {
//        SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
//        Sensor s = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        // TODO TYPE_LINEAR_ACCELERATION
//
//        sensorManager.registerListener(this,
//                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                SensorManager.SENSOR_DELAY_NORMAL);
//
//        sm.registerListener(linAcc,
//                s,
//                SensorManager.SENSOR_DELAY_GAME);

    }


}
