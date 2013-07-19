package com.dekel.babysitter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

        if (false) { // kind of alert TODO
            noView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    userChoiceAlone();
                }
            });
            yesView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    userChoiceWithBaby();
                }
            });

        } else {
            titleView.setText("צריך טקסט חדש!");
            findViewById(R.id.barView3).setVisibility(View.VISIBLE);
            TextView subtitleView = (TextView) findViewById(R.id.subtitleView);
            subtitleView.setText("האם הנסיעה הסתיימה וזכרת לקחת את תינוקך?");
            subtitleView.setVisibility(View.VISIBLE);

            noView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    userChoiceHaventFinishedRide();
                }
            });
            yesView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    userChoiceFinishedRide();
                }
            });
        }
    }

    public void userChoiceAlone() {
        Log.d(Config.MODULE_NAME,"userChoiceAlone!");
        startBabyServiceWithIntent(Config.ALONE_INTENT_EXTRA);
        Toast.makeText(this, "תודה! המערכת לא תתריע עבור נסיעה זו", Toast.LENGTH_LONG).show();
        finish();
        //                onBackPressed(); TODO
    }

    public void userChoiceWithBaby() {
        Log.d(Config.MODULE_NAME,"userChoiceWithBaby");
        startBabyServiceWithIntent(Config.BABY_INTENT_EXTRA);
        Toast.makeText(this, "תודה! בתום הנסיעה המערכת תתריע אוטומאטית", Toast.LENGTH_LONG).show();
        finish();
    }

    public void userChoiceFinishedRide() {
        Log.d(Config.MODULE_NAME, "userChoiceFinishedRide");
        startBabyServiceWithIntent(Config.USER_FINISHED_RIDE_INTENT);
        finish();
    }

    public void userChoiceHaventFinishedRide() {
        Log.d(Config.MODULE_NAME, "userChoiceHaventFinishedRide");
        startBabyServiceWithIntent(Config.USER_HAVENT_FINISHED_RIDE_INTENT);
        Toast.makeText(this, "תודה! המערכת תמשיך לנתר את המשך הנסיעה", Toast.LENGTH_LONG).show();
        finish();
    }




















    private void showBluetoothAlert(TextView titleView) {
        titleView.setText("זוהתה דיבורית חדשה!");
        findViewById(R.id.barView3).setVisibility(View.VISIBLE);
        TextView subtitleView = (TextView) findViewById(R.id.subtitleView);
        subtitleView.setVisibility(View.VISIBLE);
//            String s = subtitleView.getText().toString();
//            subtitleView.setText(s.replace(getIntent().getStringExtra("device_name"), "MARKER"));
    }

    private void startBabyServiceWithIntent(String s) {
        Intent i = new Intent(this, BabyMonitorService.class);
        i.putExtra(s, true);
        startService(i);
    }
}