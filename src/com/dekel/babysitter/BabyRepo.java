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





    private void setBoolean(String k, boolean v) {
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean(k, v);
        e.commit();
    }
}