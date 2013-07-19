package com.dekel.babysitter;

import android.content.Context;
import android.content.SharedPreferences;

public class BabyRepo {
    SharedPreferences sp = null;
    public BabyRepo(Context context) {
        sp = context.getSharedPreferences(Config.MODULE_MAIN_PREFERENCE, MainActivity.MODE_PRIVATE);
    }

    boolean isFirstTimeCompleted() {
        return sp.getBoolean(Config.FIRST_RUN_COMPLETED_KEY, false);
    }

    void setFirstTimeCompleted() {
        setBoolean(Config.FIRST_RUN_COMPLETED_KEY, true);
    }

    boolean isRideInProgress() {
        return sp.getBoolean(Config.RIDE_IN_PROGRESS_KEY, false);
    }

    void setRideInProgress(boolean v) {
        setBoolean(Config.RIDE_IN_PROGRESS_KEY, v);
    }

    boolean isBabyInCar() {
        return sp.getBoolean(Config.BABY_IN_CAR_KEY, false);
    }

    void setBabyInCar(boolean v) {
        setBoolean(Config.BABY_IN_CAR_KEY, v);
    }

    boolean isDialogPendingUser() {
        return sp.getBoolean(Config.DIALOG_PENDING_USER_KEY, false);
    }

    void setDialogPendingUser(boolean v) {
        setBoolean(Config.DIALOG_PENDING_USER_KEY, v);
    }

    private void setBoolean(String k, boolean v) {
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean(k, v);
        e.commit();
    }
}