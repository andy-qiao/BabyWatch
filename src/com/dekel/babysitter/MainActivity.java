package com.dekel.babysitter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

public class MainActivity extends Activity {

    private enum SliderState {
        SHOWING_TOS,
        TOS_APPROVED_SHOWING_INTRO,
    }

    private boolean isRideInProgress = false; // TODO StateMachine.
    private boolean debugShowError = false;
    private boolean debugShowAlert = false;
    private SliderState sliderState = SliderState.SHOWING_TOS;
    private BabyRepo babyRepo = null;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        babyRepo = new BabyRepo(this);

        if (debugShowAlert) {
            Intent i = new Intent(this, AlertActivity.class);
            i.putExtra(Config.SHOW_RIDE_FINISHED_ALERT_INTENT_EXTRA, true);
            i.putExtra(Config.BLUETOOTH_DEVICE_NAME_INTENT_EXTRA, "Motorola HK-200");

            startActivity(i);
        }

        if (!babyRepo.isFirstTimeCompleted()) { // First time!
            showFirstTimeSlider();

        } else {
            showStatefulHome();
        }

        InitializeMonitoring();          // TODO maybe not alwys?

        Log.d(Config.MODULE_NAME, "MainActivity - Init done!");
    }

    private void InitializeMonitoring() {
        // TODO alarm service for BT
        // TODO when ride in progress - present it here - singleton?

        // TODO mabe initialize that with the service?

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, BabyMonitorReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
        am.cancel(pi);

        am.setInexactRepeating(AlarmManager.RTC,
                System.currentTimeMillis(),
                500, // AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                pi);

        startService(new Intent(this, BabyMonitorService.class));
    }

    private void showFirstTimeSlider() {
        setContentView(R.layout.terms_of_service);

        sliderState = SliderState.SHOWING_TOS;

        ((TextView) findViewById(R.id.mainTitleTextView)).setTypeface(FontUtils.getTypeface(this));

        final TextView bodyView = (TextView) findViewById(R.id.mainBodyTextView1);
        bodyView.setTypeface(FontUtils.getTypeface(this));
        final TextView subtitleView = (TextView) findViewById(R.id.mainSubtitleTextView);
        subtitleView.setTypeface(FontUtils.getTypefaceBold(this));

        final Button continueButton = (Button) findViewById(R.id.button);
        continueButton.setTypeface(FontUtils.getTypeface(this));
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
                    babyRepo.setFirstTimeCompleted();
                    showStatefulHome();
                    break;
            }
            }
        });
    }


    private void showStatefulHome() {
        setContentView(R.layout.main);

        ((TextView) findViewById(R.id.mainTitleTextView)).setTypeface(FontUtils.getTypeface(this));

        final TextView bodyView = (TextView) findViewById(R.id.mainBodyTextView1);
        bodyView.setTypeface(FontUtils.getTypeface(this));
        final TextView subtitleView = (TextView) findViewById(R.id.mainSubtitleTextView);
        subtitleView.setTypeface(FontUtils.getTypefaceBold(this));

        findViewById(R.id.mainImageReady).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                easterEggHandling();
            }
        });

        if (babyRepo.isRideInProgress()) {
            subtitleView.setText("יצאנו לדרך");
            bodyView.setText(babyRepo.isBabyInCar() ?
                new String("עם סיום הנסיעה הנוכחית המערכת תתריע בכדי שתוכלו לוודא כי אף אחת או אחד לא נותרו מאחור :)") :
                new String("שירות התרעה אינו מופעל עבור נסיעה זו"));
        }

        if (debugShowError) {
            subtitleView.setText("אופס!");
            bodyView.setText("המערכת אינה פעילה! אנא וודאו כי שירות ה-GPS וכן שירות ה-Bluetooth הינם במצב פעיל");
            ((ImageView) findViewById(R.id.mainImageReady)).setImageResource(R.drawable.icon_x);
        }
    }


    LinkedList<Long> easterEggTimestamps = new LinkedList<Long>();
    private void easterEggHandling() {
        Log.d(Config.MODULE_NAME, "easter_egg " + System.currentTimeMillis());
        easterEggTimestamps.add(System.currentTimeMillis());
        if (easterEggTimestamps.size() < 7) {
            return;
        }
        if (easterEggTimestamps.poll() > System.currentTimeMillis() - 2*1000) {
            Log.w(Config.MODULE_NAME, "easter_egg");
            startActivity(new Intent(this, DebugActivity.class));
        }
    }
}
