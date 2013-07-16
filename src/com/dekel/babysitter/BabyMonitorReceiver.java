package com.dekel.babysitter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: dekelna
 * Date: 7/15/13
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class BabyMonitorReceiver extends BroadcastReceiver {
    private Context context;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public void onReceive(Context context, Intent intent) {
        Log.d(Config.MODULE_NAME, "onReceive.");
        this.context = context;

//        SharedPreferences settings = context.getSharedPreferences(Config.PREFS_NAME, 0);
//
////        HashSet.
//        String s = settings.getString();
//        try {
//            new ByteArrayInputStream(s.getBytes("UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }


        detectBluetoothdevices();
    }

    Set<String> knownPairedDevicesNames = new HashSet<String>();

    public Set<String> detectBluetoothdevices() {

        Log.d(Config.MODULE_NAME, "detectBluetoothdevices.");

        if (bluetoothAdapter == null) {
            Log.d(Config.MODULE_NAME, "Device doesn't support BT!");
            return null;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Log.d(Config.MODULE_NAME, "BluetoothAdapter is OFF!");
            // TODO
//            final int REQUEST_ENABLE_BT = 1;
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            context.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        Log.d(Config.MODULE_NAME, "Looking for bound devices.");
        bluetoothAdapter.cancelDiscovery();
        Set<BluetoothDevice> currentPairedDevices = bluetoothAdapter.getBondedDevices();
        Set<String> currentPairedDeviceNames = new HashSet<String>();
        for (BluetoothDevice device : currentPairedDevices) {
            Log.d(Config.MODULE_NAME, "DEBUG " + device.getName());
            currentPairedDeviceNames.add(device.getName());
        }

        Log.d(Config.MODULE_NAME, "DEBUG current " + knownPairedDevicesNames.size());


        Log.d(Config.MODULE_NAME, "Found " + currentPairedDevices.size() + " devices.");

        if (knownPairedDevicesNames.equals(currentPairedDeviceNames)) {
            Log.d(Config.MODULE_NAME, "Nothing new under the sun.");
            return currentPairedDeviceNames;
        }

        Set<String> newlyConnectedDeviceNames = new HashSet<String> (currentPairedDeviceNames);
        newlyConnectedDeviceNames.removeAll(knownPairedDevicesNames);

        Set<String> newlyDisconnectedDeviceNames = new HashSet<String> (knownPairedDevicesNames);
        newlyDisconnectedDeviceNames.removeAll(currentPairedDeviceNames);

        Log.d(Config.MODULE_NAME, "Notifying StateMachine");
        //rsm.notifyBluetoothChanges(newlyConnectedDeviceNames, newlyDisconnectedDeviceNames);

        return currentPairedDeviceNames;
    }





}
