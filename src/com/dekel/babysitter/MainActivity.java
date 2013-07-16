package com.dekel.babysitter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

public class MainActivity extends Activity {


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // TODO alarm service for BT
        // TODO when ride in progress - present it here - singleton?

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, BabyMonitorReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
        am.cancel(pi); // cancel any existing alarms

        am.setInexactRepeating(AlarmManager.RTC,
                System.currentTimeMillis(),
                //AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                5000,
                pi);
//        am.set();

        startService(new Intent(this, BabyMonitorService.class));



        Log.d("bla", "Init done!!");
    }

    @Override
    protected void onResume() {
        super.onResume();
//
//        sensorManager.registerListener(this,
//                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                //SensorManager.SENSOR_ACCELEROMETER);
//                SensorManager.SENSOR_DELAY_FASTEST);
    }

//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            getAccelerometer(event);
//        }
//
//    }
//
//    private void getAccelerometer(SensorEvent event) {
//        float[] values = event.values;
//        // Movement
//        float x = values[0];
//        float y = values[1];
//        float z = values[2];
//
//        float accelationSquareRoot = (x * x + y * y + z * z)
//                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
//        long actualTime = System.currentTimeMillis();
//        if (accelationSquareRoot >= 2) //
//        {
//            if (actualTime - lastUpdate < 200) {
//                return;
//            }
//            lastUpdate = actualTime;
//            Toast.makeText(this, "Device was shuffed", Toast.LENGTH_SHORT)
//                    .show();
//            if (color) {
//                view.setBackgroundColor(Color.GREEN);
//
//            } else {
//                view.setBackgroundColor(Color.RED);
//            }
//            color = !color;
//        }
//    }
}
