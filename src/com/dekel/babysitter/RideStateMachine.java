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
    long serviceLoadTime = System.currentTimeMillis();

    private long stoppedSince = 0;
    private long movingSince = 0;

    private boolean rideInProgress = false;
    private boolean rideWithBaby = false;

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

            Log.d(Config.MODULE_NAME , "stopped with baby!!!");
            Intent i = new Intent(context, FinishedRideActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(i);
        }
    }

    public void notifySpeedChange(float speed) {

        if (System.currentTimeMillis() - serviceLoadTime < 5000) {
            Log.d(Config.MODULE_NAME ,"ignoring, boot");
            return;
        }

        if (speed > 0.4) { // TODO m/s

            stoppedSince = 0;
            if (movingSince == 0) {
                movingSince = System.currentTimeMillis();
            } else {
                if (System.currentTimeMillis() - movingSince > 5000) {
                    signalRideInProgress();
                }
            }

        } else {
            movingSince = 0;
            if (stoppedSince == 0) {
                stoppedSince = System.currentTimeMillis();
            } else if (user_havent_stopped_override != 0) {
                if (System.currentTimeMillis() - user_havent_stopped_override > (1 * 60 * 1000)) {
                    user_havent_stopped_override = 0;
                }

            } else {
                if (System.currentTimeMillis() - stoppedSince > 5000) {
                    signalRideStopped();
                }
            }

        }
    }

    public void userHasntFinishedRide() {
        rideInProgress = true;
        rideWithBaby = true;
        user_havent_stopped_override = System.currentTimeMillis();
    }

    public void userFinishedRide() {
        rideInProgress = false;
        rideWithBaby = false;
    }

    public void UserRidingWithBaby() {
        rideInProgress = true;
        rideWithBaby = true;
    }

    public void userRidingAlone() {
        rideInProgress = true;
        rideWithBaby = false;
    }

    private void signalRideInProgress() {
        if (rideInProgress) {
            return;
        }
        rideInProgress = true;
        Log.d(Config.MODULE_NAME, "a ride in progress.");

        Intent i = new Intent(context, AlertActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(i);
    }



}
