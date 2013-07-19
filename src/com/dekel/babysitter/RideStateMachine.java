package com.dekel.babysitter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: dekelna
 * Date: 7/16/13
 * Time: 9:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class RideStateMachine {
    public static final int BOOT_DELAY = 5 * 1000;
    public static final int MOVING_MIN_TIME = 30 * 1000;
    public static final int STOPPING_MIN_TIME = 5 * 60 * 1000;
    public static final int FALSE_POSITIVE_STOP = 5 * 60 * 1000;

    long serviceLoadTime = System.currentTimeMillis();
    BabyRepo babyRepo = null;

    private long stoppedSince = 0;
    private long movingSince = 0;

//    private boolean rideInProgress = false;
    private boolean rideWithBaby = false;
    private boolean userDialogInProgress = false;

    private long user_havent_stopped_override = 0;

    private Context context = null;
    public RideStateMachine(Context context) {
        this.context = context;
        babyRepo = new BabyRepo(context);
    }

    public void notifySpeedChange(float speed) {

        if (System.currentTimeMillis() - serviceLoadTime < BOOT_DELAY) {
            Log.d(Config.MODULE_NAME ,"ignoring, boot");
            return;
        }

        if (speed > Config.SPEED_THRESHOLD) {

            stoppedSince = 0;
            if (movingSince == 0) {
                movingSince = System.currentTimeMillis();
            } else {
                if (System.currentTimeMillis() - movingSince > MOVING_MIN_TIME) {
                    handleRideStarted();
                }
            }

        } else {
            movingSince = 0;
            if (stoppedSince == 0) {
                stoppedSince = System.currentTimeMillis();
            } else if (user_havent_stopped_override != 0) {
                if (System.currentTimeMillis() - user_havent_stopped_override > FALSE_POSITIVE_STOP) {
                    user_havent_stopped_override = 0;
                }

            } else {
                if (System.currentTimeMillis() - stoppedSince > STOPPING_MIN_TIME) {
                    handleRideStopped();
                }
            }

        }
    }

    public void handleRideStarted() {
        if (userDialogInProgress) {
            return;
            // TODO what's the expected behaviour?
        }

        if (babyRepo.isRideInProgress()) {
//            return; // TODO ignore?
//            throw new IllegalStateException();
            // TODO just for testing?
        }

        babyRepo.setRideInProgress(true);
        userDialogInProgress = true;
//        rideInProgress = true;
        Log.d(Config.MODULE_NAME, "a ride in progress.");

        startAlertActivityWithIntent(Config.SHOW_RIDE_STARTED_ALERT_INTENT_EXTRA);
    }

    private void handleRideStopped() {
        if (userDialogInProgress) {
            return;
            // TODO what's the expected behaviour?
        }

        if (!babyRepo.isRideInProgress()) {
            throw new IllegalStateException();
        }
        babyRepo.setRideInProgress(false);

        Log.d(Config.MODULE_NAME, "stopped!!!");

        if (rideWithBaby) {
            rideWithBaby = false;

            userDialogInProgress = true;

            Log.d(Config.MODULE_NAME , "stopped with baby!!!");
            startAlertActivityWithIntent(Config.SHOW_RIDE_FINISHED_ALERT_INTENT_EXTRA);
        }
    }

    public void userChoiceHasntFinishedRide() {
        userDialogInProgress = false;
//        rideInProgress = true;
        babyRepo.setRideInProgress(true);
        rideWithBaby = true;
        user_havent_stopped_override = System.currentTimeMillis();
    }

    public void userChoiseFinishedRide() {
        userDialogInProgress = false;
//        rideInProgress = false;
        babyRepo.setRideInProgress(false);
        rideWithBaby = false;
    }

    public void UserChoiceRidingWithBaby() {
        userDialogInProgress = false;
//        rideInProgress = true;
        babyRepo.setRideInProgress(true);
        rideWithBaby = true;
    }

    public void userChoiceRidingAlone() {
        userDialogInProgress = false;
//        rideInProgress = true;
        babyRepo.setRideInProgress(true);
        rideWithBaby = false;
    }

    private void startAlertActivityWithIntent(String s) {
        Intent i = new Intent(context, AlertActivity.class);
        i.putExtra(s, true);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP); // TODO
        context.startActivity(i);
    }
}
