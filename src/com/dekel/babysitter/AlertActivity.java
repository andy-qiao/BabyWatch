package com.dekel.babysitter;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
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
 */
public class AlertActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ride_started_alert);
        mp = MediaPlayer.create(this, R.raw.sounds_774_springy);


        TextView titleView =  (TextView) findViewById(R.id.titleView);
        titleView.setTypeface(FontUtils.getTypefaceBold(this));

        TextView noView = (TextView) findViewById(R.id.NoView);
        noView.setTypeface(FontUtils.getTypefaceBold(this));

        TextView yesView = (TextView) findViewById(R.id.yesView);
        yesView.setTypeface(FontUtils.getTypefaceBold(this));

        if (getIntent().getBooleanExtra(Config.SHOW_RIDE_STARTED_ALERT_INTENT_EXTRA, false)) {
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

        } else if (getIntent().getBooleanExtra(Config.SHOW_RIDE_FINISHED_ALERT_INTENT_EXTRA, false)) {
            titleView.setText(R.string.ride_finished_title);
            findViewById(R.id.barView3).setVisibility(View.VISIBLE);
            TextView subtitleView = (TextView) findViewById(R.id.subtitleView);
            subtitleView.setText(R.string.ride_finished_text);
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

            playAlertSound();

        } else if (getIntent().getBooleanExtra(Config.SHOW_NEW_BLUETOOTH_DEVICE_ALERT_INTENT_EXTRA, false)) {
            showBluetoothAlert(titleView);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    MediaPlayer mp = null;
    private void playAlertSound() {
        mp.setVolume(1,1);
        mp.setLooping(true);
        mp.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.stop();

        // TODO notify service user didnt choose anything
    }

    public void userChoiceAlone() {
        Log.d(Config.MODULE_NAME, "userChoiceAlone!");
        startBabyServiceWithIntent(Config.USER_CHOICE_ALONE_INTENT_EXTRA);
        Toast.makeText(this, "תודה! המערכת לא תתריע עבור נסיעה זו", Toast.LENGTH_LONG).show();
        finish();
    }

    public void userChoiceWithBaby() {
        Log.d(Config.MODULE_NAME,"userChoiceWithBaby");
        startBabyServiceWithIntent(Config.USER_CHOICE_BABY_INTENT_EXTRA);
        Toast.makeText(this, "תודה! בתום הנסיעה המערכת תתריע אוטומאטית", Toast.LENGTH_LONG).show();
        finish();
    }

    public void userChoiceFinishedRide() {
        Log.d(Config.MODULE_NAME, "userChoiceFinishedRide");
        startBabyServiceWithIntent(Config.USER_CHOICE_FINISHED_RIDE_INTENT_EXTRA);
        finish();
    }

    public void userChoiceHaventFinishedRide() {
        Log.d(Config.MODULE_NAME, "userChoiceHaventFinishedRide");
        startBabyServiceWithIntent(Config.USER_CHOICE_HAVENT_FINISHED_RIDE_INTENT_EXTRA);
        Toast.makeText(this, "תודה! המערכת תמשיך לנתר את המשך הנסיעה", Toast.LENGTH_LONG).show();
        finish();
    }

    private void showBluetoothAlert(TextView titleView) {
        titleView.setText(R.string.new_device);
        findViewById(R.id.barView3).setVisibility(View.VISIBLE);
        TextView subtitleView = (TextView) findViewById(R.id.subtitleView);

        subtitleView.setText(
            new String("המערכת זיהתה כי הרגע בוצע חיבור לדיבורית מדגם ") +
            getIntent().getStringExtra(Config.BLUETOOTH_DEVICE_NAME_INTENT_EXTRA) +
            new String(". האם זהו התקן דיבורית קבוע ברכבך?")
        );

        subtitleView.setVisibility(View.VISIBLE);
    }

    private void startBabyServiceWithIntent(String s) {
        Intent i = new Intent(this, BabyMonitorService.class);
        i.putExtra(s, true);
        startService(i);
    }
}