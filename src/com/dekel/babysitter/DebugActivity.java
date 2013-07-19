package com.dekel.babysitter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: dekelna
 * Date: 7/19/13
 * Time: 10:46 PM
 */
public class DebugActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug);

        findViewById(R.id.stopbutton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startServiceWithSpeedIntent(1.0f);
            }
        });

        findViewById(R.id.gofastbutton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startServiceWithSpeedIntent(10.0f);
            }
        });
    }

    private void startServiceWithSpeedIntent(float speed) {
        Intent i = new Intent(this, BabyMonitorService.class);
        i.putExtra(Config.DEBUG_SPEED_INTENT_EXTRA, speed);
        startService(i);
    }
}