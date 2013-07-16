package com.dekel.babysitter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

//        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//        Intent intent = new Intent(this, BabyMonitorReceiver.class);
//        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
//        am.cancel(pi); // cancel any existing alarms
//
//        am.setInexactRepeating(AlarmManager.RTC,
//                System.currentTimeMillis(),
//                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
//                pi);

        startService(new Intent(this, BabyMonitorService.class));

        Log.d(Config.MODULE_NAME, "Init done!");

        findViewById(R.id.buttonDebug).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Config.debug = !Config.debug;
            }
        });
    }

}
