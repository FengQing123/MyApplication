package com.example.fengq.myapplication.listeners;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by fengq on 2017/5/3.
 */

public class BlueToothReceiver extends BroadcastReceiver {

    private BluetoothAdapter bluetoothAdapter;

    public BlueToothReceiver(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (bluetoothAdapter != null) {

        }
    }
}
