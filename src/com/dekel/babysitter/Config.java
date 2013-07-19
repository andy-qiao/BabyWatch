package com.dekel.babysitter;

/**
 * Created with IntelliJ IDEA.
 * User: dekelna
 * Date: 7/16/13
 * Time: 9:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class Config {
    public static final String FIRST_RUN_COMPLETED_KEY = "first_run";
    public static final String MODULE_MAIN_PREFERENCE = "prefs";
    public static final String BABY_INTENT_EXTRA = "baby";
    public static final String ALONE_INTENT_EXTRA = "userChoiceAlone";
    public static final String USER_FINISHED_RIDE_INTENT = "userChoiceFinishedRide";
    public static final String USER_HAVENT_FINISHED_RIDE_INTENT = "userChoiceHaventFinishedRide";
    public static String MODULE_NAME = "babysitter_log";

    public static float SPEED_THRESHOLD = 8; // ~25Km/h / 3.6m/s
}
