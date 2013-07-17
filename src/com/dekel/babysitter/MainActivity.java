package com.dekel.babysitter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private boolean isFirstTime = true; // TODO repo
    private enum State {
        SHOWING_TOS,
        TOS_APPROVED_SHOWING_INTRO,
    }

    private State state;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFirstTime) {
            setContentView(R.layout.terms_of_service);

            state = State.SHOWING_TOS;

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
                    switch (state) {
                        case SHOWING_TOS:
                            state = State.TOS_APPROVED_SHOWING_INTRO;
                            // Live switching
                            subtitleView.setText("על נוסעים לגן");
                            bodyView.setText("אפליקציית \"נוסעים לגן\" מבצעת מעקב אחרי נסיעותיכם ברכב ותזכיר לכם לוודא שלא שכחתם את ילדכם ברכב זמן קצר לאחר יציאתכם מהרכב.\n");
                            continueButton.setText("המשך");

                            break;

                        case TOS_APPROVED_SHOWING_INTRO:
                            subtitleView.setText("ניצחון!!!!!");
                            break;
                        default:
                            break;
                    }





                }
            });



        }


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



    }

}
