package com.dekel.babysitter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: dekelna
 * Date: 7/15/13
 * Time: 11:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class AlertActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ride_started_alert);
        Typeface typeFace = Typeface.createFromAsset(getAssets(),"Alef-Regular.ttf");
        Typeface typeFaceBold = Typeface.createFromAsset(getAssets(),"Alef-Bold.ttf");

        TextView titleView =  (TextView) findViewById(R.id.titleView);
        titleView.setTypeface(typeFaceBold);

        TextView noView = (TextView) findViewById(R.id.NoView);
        noView.setTypeface(typeFaceBold);

        TextView yesView = (TextView) findViewById(R.id.yesView);
        yesView.setTypeface(typeFaceBold);

        if (true) { // TODO
            titleView.setText("זוהתה דיבורית חדשה!");
            findViewById(R.id.barView3).setVisibility(View.VISIBLE);
            TextView subtitleView = (TextView) findViewById(R.id.subtitleView);
            subtitleView.setVisibility(View.VISIBLE);
//            String s = subtitleView.getText().toString();
//            subtitleView.setText(s.replace(getIntent().getStringExtra("device_name"), "MARKER"));
            // TODO


        }

        noView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                alone();
                onBackPressed();
            }
        });


        yesView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                withBaby();
                onBackPressed();
            }
        });
    }

    public void alone() {
        Log.d(Config.MODULE_NAME,"alone!");
        Intent i = new Intent(this, BabyMonitorService.class);
        i.putExtra(Config.ALONE_INTENT_EXTRA, true);
        startService(i);

    }

    public void withBaby() {
        Log.d(Config.MODULE_NAME,"baby!!!");
        Intent i = new Intent(this, BabyMonitorService.class);
        i.putExtra(Config.BABY_INTENT_EXTRA, true);
        startService(i);
    }
}