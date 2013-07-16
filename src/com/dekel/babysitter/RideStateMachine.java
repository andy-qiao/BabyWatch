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
    public static final int STOPPING_MIN_TIME = 3 * 60 * 1000;
    public static final int FALSE_POSITIVE_STOP = 5 * 60 * 1000;

    long serviceLoadTime = System.currentTimeMillis();

    private long stoppedSince = 0;
    private long movingSince = 0;

    private boolean rideInProgress = false;
    private boolean rideWithBaby = false;
    private boolean userDialogInProgress = false;

    private long user_havent_stopped_override = 0;

    private Context context = null;
    public RideStateMachine(Context context) {
        this.context = context;
    }

    private void signalRideStopped() {
        if (!rideInProgress) {
            return;
        }
        rideInProgress = false;

        Log.d(Config.MODULE_NAME , "stopped!!!");

        if (rideWithBaby) {
            rideWithBaby = false;

            if (userDialogInProgress) {
                return;
                // TODO what's the expected behaviour?
            }
            userDialogInProgress = true;

            Log.d(Config.MODULE_NAME , "stopped with baby!!!");
            Intent i = new Intent(context, FinishedRideActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(i);
        }
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
                    signalRideInProgress();
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
                    signalRideStopped();
                }
            }

        }
    }

    public void userHasntFinishedRide() {
        userDialogInProgress = false;
        rideInProgress = true;
        rideWithBaby = true;
        user_havent_stopped_override = System.currentTimeMillis();
    }

    public void userFinishedRide() {
        userDialogInProgress = false;
        rideInProgress = false;
        rideWithBaby = false;
    }

    public void UserRidingWithBaby() {
        userDialogInProgress = false;
        rideInProgress = true;
        rideWithBaby = true;
    }

    public void userRidingAlone() {
        userDialogInProgress = false;
        rideInProgress = true;
        rideWithBaby = false;
    }

    private void signalRideInProgress() {
        if (rideInProgress) {
            return;
        }
        if (userDialogInProgress) {
            return;
            // TODO what's the expected behaviour?
        }

        userDialogInProgress = true;
        rideInProgress = true;
        Log.d(Config.MODULE_NAME, "a ride in progress.");

        Intent i = new Intent(context, AlertActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(i);
    }



}
