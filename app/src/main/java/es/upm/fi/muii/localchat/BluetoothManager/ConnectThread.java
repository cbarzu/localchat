package es.upm.fi.muii.localchat.BluetoothManager;


// Unique UUID for this application

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;



public class ConnectThread extends Thread{
    private BluetoothSocket bTSocket;
    private static final UUID MY_UUID_INSECURE =  UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    String data;
    BluetoothDevice bTDevice;
    public ConnectThread(String data, BluetoothDevice aDevice){
        this.data = data;
        bTDevice = aDevice;
    }

    BluetoothSocket temp = null;
    public boolean connect() {

        try {
            temp = bTDevice.createRfcommSocketToServiceRecord(MY_UUID_INSECURE);
        } catch (IOException e) {
            Log.d("CONNECTTHREAD", "Could not create RFCOMM socket:" + e.toString());
            return false;
        }
        try {
            bTSocket.connect();
        } catch(IOException e) {
            Log.d("CONNECTTHREAD","Could not connect: " + e.toString());
            try {
                bTSocket.close();
            } catch(IOException close) {
                Log.d("CONNECTTHREAD", "Could not close connection:" + e.toString());
                return false;
            }
        }
        return true;
    }

    public void sendData( ) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream(data.getBytes().length);
        output.write(data.getBytes());
        OutputStream outputStream = temp.getOutputStream();
        outputStream.write(output.toByteArray());
    }

    public void run(){
        try {
            sendData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean cancel() {
        try {
            bTSocket.close();
        } catch(IOException e) {
            Log.d("CONNECTTHREAD","Could not close connection:" + e.toString());
            return false;
        }
        return true;
    }
}