package com.dekel.babysitter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: dekelna
 * Date: 7/16/13
 * Time: 1:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class FinishedRideActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ride_finished_alert);

        findViewById(R.id.finishedTextView).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                userFinishedRide();
                onBackPressed();
            }
        });

        findViewById(R.id.stillInProgressTextView).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                userHaventFinishedRide();
                onBackPressed();
            }
        });
    }

    public void userFinishedRide() {
        Log.d("bla", "userChoiceFinishedRide");
        Intent i = new Intent(this, BabyMonitorService.class);
        i.putExtra("userChoiceFinishedRide", true);
        startService(i);
    }

    public void userHaventFinishedRide() {
        Log.d("bla", "userChoiceHaventFinishedRide");
        Intent i = new Intent(this, BabyMonitorService.class);
        i.putExtra("userChoiceHaventFinishedRide", true);
        startService(i);
    }
}
