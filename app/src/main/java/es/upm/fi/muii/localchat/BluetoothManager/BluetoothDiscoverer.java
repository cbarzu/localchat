package es.upm.fi.muii.localchat.BluetoothManager;

/**
 * Created by claudiu on 17/11/2015.
 */


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.Set;


public class BluetoothDiscoverer {
    /**
     * Member fields
     */
    private BluetoothAdapter mBtAdapter;



    public BluetoothDiscoverer(){
        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    }


    public Set<BluetoothDevice> getPairedDevices(){
        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        return pairedDevices;
    }

    public boolean doDiscovery() {

        return mBtAdapter.startDiscovery();
    }

    public boolean cancelDiscovery() {

       return mBtAdapter.cancelDiscovery();
    }

}
