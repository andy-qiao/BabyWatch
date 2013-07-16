package com.dekel.babysitter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
        setContentView(R.layout.location_proposal);


        findViewById(R.id.aloneTextView).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                alone();
                onBackPressed();
            }
        });


        findViewById(R.id.babyTextView).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                withBaby();
                onBackPressed();
            }
        });
    }

    public void alone() {
        Log.d("bla","alone");
        Intent i = new Intent(this, BabyMonitorService.class);
        i.putExtra("alone", true);
        startService(i);

    }

    public void withBaby() {
        Log.d("bla","baby!!!");
        Intent i = new Intent(this, BabyMonitorService.class);
        i.putExtra("baby", true);
        startService(i);
    }
}