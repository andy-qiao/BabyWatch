package com.dekel.babysitter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private enum SliderState {
        SHOWING_TOS,
        TOS_APPROVED_SHOWING_INTRO,
    }

    private boolean isRideInProgress = false; // TODO StateMachine.
    private boolean demoError = true;
    private boolean demoAlert = false;
    private SliderState sliderState = SliderState.SHOWING_TOS;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (demoAlert) { // TODO debug remove me
            startActivity(new Intent(this, AlertActivity.class));
        }

        if (!isFirstTimeCompleted()) { // First time!
            showFirstTimeSlider();

        } else {
            showSystemReady();
        }

        InitializeMonitoring();

        Log.d(Config.MODULE_NAME, "Init done!");
    }

    private void InitializeMonitoring() {
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
    }

    private void showFirstTimeSlider() {
        setContentView(R.layout.terms_of_service);

        sliderState = SliderState.SHOWING_TOS;

        Typeface typeFace = Typeface.createFromAsset(getAssets(),"Alef-Regular.ttf");
        Typeface typeFaceBold = Typeface.createFromAsset(getAssets(),"Alef-Bold.ttf");
        ((TextView) findViewById(R.id.mainTitleTextView)).setTypeface(typeFace);

        final TextView bodyView = (TextView) findViewById(R.id.mainBodyTextView1);
        bodyView.setTypeface(typeFace);
        final TextView subtitleView = (TextView) findViewById(R.id.mainSubtitleTextView);
        subtitleView.setTypeface(typeFaceBold);

        final Button continueButton = (Button) findViewById(R.id.button);
        continueButton.setTypeface(typeFace);
        continueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            switch (sliderState) {
                case SHOWING_TOS:
                    sliderState = SliderState.TOS_APPROVED_SHOWING_INTRO;
                    // Live switching
                    subtitleView.setText("על \"נוסעים לגן\"");
                    bodyView.setText("אפליקציית \"נוסעים לגן\" מבצעת מעקב אחרי נסיעותיכם ברכב. זמן קצר לאחר סיום הנסיעה המערכת תזכיר לכם לוודא שלא שכחתם את ילדכם\n");
                    continueButton.setText("המשך");
                    break;

                case TOS_APPROVED_SHOWING_INTRO:
                    setFirstTimeCompleted();
                    showSystemReady();
                    break;
            }
            }
        });
    }

    private boolean isFirstTimeCompleted() {
        return getSharedPreferences(Config.MODULE_MAIN_PREFERENCE, MODE_PRIVATE).getBoolean(Config.FIRST_RUN_COMPLETED_KEY, false);
    }

    private void setFirstTimeCompleted() {
        SharedPreferences.Editor e = getSharedPreferences(Config.MODULE_MAIN_PREFERENCE, MODE_PRIVATE).edit();
        e.putBoolean(Config.FIRST_RUN_COMPLETED_KEY, true);
        e.commit();
    }

    private void showSystemReady() {
        setContentView(R.layout.main);

        Typeface typeFace = Typeface.createFromAsset(getAssets(),"Alef-Regular.ttf");
        Typeface typeFaceBold = Typeface.createFromAsset(getAssets(),"Alef-Bold.ttf");
        ((TextView) findViewById(R.id.mainTitleTextView)).setTypeface(typeFace);

        final TextView bodyView = (TextView) findViewById(R.id.mainBodyTextView1);
        bodyView.setTypeface(typeFace);
        final TextView subtitleView = (TextView) findViewById(R.id.mainSubtitleTextView);
        subtitleView.setTypeface(typeFaceBold);

        if (isRideInProgress) {
            subtitleView.setText("יצאנו לדרך");
            bodyView.setText("עם סיום הנסיעה הנוכחית המערכת תתריע בכדי שתוכלו לוודא כי אף אחת או אחד לא נותרו מאחור :)");
        }

        if (demoError) {
            subtitleView.setText("אופס!");
            bodyView.setText("המערכת אינה פעילה! אנא וודאו כי שירות ה-GPS וכן שירות ה-Bluetooth הינם במצב פעיל");
            ((ImageView) findViewById(R.id.mainImageReady)).setImageResource(R.drawable.icon_x);
        }
    }

}
