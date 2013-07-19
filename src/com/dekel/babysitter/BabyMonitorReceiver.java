package com.dekel.babysitter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: dekelna
 * Date: 7/15/13
 * Time: 8:37 PM
 */
public class BabyMonitorReceiver extends BroadcastReceiver {
    private Context context;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private static final String ACTION_BT_HEADSET_STATE_CHANGED  = "android.bluetooth.headset.action.STATE_CHANGED";
    private static final int STATE_CONNECTED = 0x00000002;
    private static final int STATE_DISCONNECTED  = 0x00000000;
    private static final String EXTRA_STATE = "android.bluetooth.headset.extra.STATE";

    public void onReceive(Context context, Intent intent) {
        Log.d(Config.MODULE_NAME, "onReceive.");
        this.context = context;

        Log.d(Config.MODULE_NAME, "DEBUG INTENT " + intent.getAction());
        Log.d(Config.MODULE_NAME, "device " + intent.getStringExtra("android.bluetooth.device.extra.DEVICE"));
        if(ACTION_BT_HEADSET_STATE_CHANGED.equals(intent.getAction())){
            int extraData = intent.getIntExtra(EXTRA_STATE  , STATE_DISCONNECTED);
            if (extraData == STATE_CONNECTED ){

                Log.d(Config.MODULE_NAME, "CONNECTION!");


            }else if (extraData == STATE_DISCONNECTED){

                Log.d(Config.MODULE_NAME, "DISCONNECTION!");
            }


        }





        //detectBluetoothdevices();
    }

    Set<String> knownConnectedDevices = new HashSet<String>();

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
        //BluetoothHeadset.getConnectedDevices();
        // TODO Headset API 11

        Set<String> currentConnectedDevices = new HashSet<String>();
        for (BluetoothDevice device : currentPairedDevices) {
            // TODO bonding isn't good enough
            if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                currentConnectedDevices.add(device.getName());
            }
        }

        Log.d(Config.MODULE_NAME, "DEBUG known " + knownConnectedDevices.size());
        Log.d(Config.MODULE_NAME, "Found " + currentConnectedDevices.size() + " connected devices.");

        if (knownConnectedDevices.equals(currentConnectedDevices)) {
            Log.d(Config.MODULE_NAME, "Nothing new under the sun.");
            return currentConnectedDevices;
        }

        Set<String> newlyConnectedDeviceNames = new HashSet<String> (currentConnectedDevices);
        newlyConnectedDeviceNames.removeAll(knownConnectedDevices);

        Set<String> newlyDisconnectedDeviceNames = new HashSet<String> (knownConnectedDevices);
        newlyDisconnectedDeviceNames.removeAll(currentConnectedDevices);

        Log.d(Config.MODULE_NAME, "Notifying StateMachine");
        //rsm.notifyBluetoothChanges(newlyConnectedDeviceNames, newlyDisconnectedDeviceNames);

        return currentConnectedDevices;
    }





}
