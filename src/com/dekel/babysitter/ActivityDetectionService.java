package com.dekel.babysitter;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: dekelna
 * Date: 8/3/13
 * Time: 3:12 AM
 */
public class ActivityDetectionService extends IntentService {

    public ActivityDetectionService() {
        super("BabyGPService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!ActivityRecognitionResult.hasResult(intent)) {
            return;
        }
        DetectedActivity mostProbableActivity = ActivityRecognitionResult.extractResult(intent).getMostProbableActivity();
        Log.d(Config.MODULE_NAME, "DetectedActivity=" + getNameFromType(mostProbableActivity.getType()) + ", confidence=" + mostProbableActivity.getConfidence());
        debugPrintResults(null, mostProbableActivity);

        // At the moment we're ignoring confidence, and using only the most probable activity.
        switch (mostProbableActivity.getType()) {
            case DetectedActivity.IN_VEHICLE:
                // TODO change frequency
                break;
            case DetectedActivity.ON_FOOT:
                break;
        }
    }



    /**
     * DEBUG
     */
    public void appendLog(String text)
    {
        File logFile = new File("sdcard/log.file");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void debugPrintResults(ActivityRecognitionResult result, DetectedActivity mostProbableActivity) {
//        appendLog(result.toString());
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.appicon)
                        .setContentTitle("Activity=" + getNameFromType(mostProbableActivity.getType()))
                        .setContentText("Confidence=" + mostProbableActivity.getConfidence());

        mBuilder.build();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1234, mBuilder.build());
    }

    private String getNameFromType(int activityType) {
        switch(activityType) {
            case DetectedActivity.IN_VEHICLE:
                return "in_vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "on_bicycle";
            case DetectedActivity.ON_FOOT:
                return "on_foot";
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.UNKNOWN:
                return "unknown";
            case DetectedActivity.TILTING:
                return "tilting";
        }
        return "unknown";
    }

}
