package es.upm.fi.muii.localchat.BluetoothManager;


// Unique UUID for this application

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

import es.upm.fi.muii.localchat.chat.ChatMessage;


public class ConnectThread extends Thread{

    private static final UUID MY_UUID_INSECURE =  UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private ChatMessage data;
    BluetoothDevice bTDevice;
    private BluetoothSocket bTSocket;

    public ConnectThread(ChatMessage data, BluetoothDevice aDevice){
        this.data = data;
        bTDevice = aDevice;
    }


    public boolean connect() {

        try {
            bTSocket = bTDevice.createRfcommSocketToServiceRecord(MY_UUID_INSECURE);
        } catch (IOException e) {
            Log.d("CONNECTTHREAD", "Could not create RFCOMM socket:" + e.toString());
            return false;
        }
        try {
            bTSocket.connect();
        } catch(IOException e) {
            Log.d("CONNECTTHREAD", "Could not connect: " + e.toString());
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
        OutputStream outputStream = bTSocket.getOutputStream();
        if (bTSocket.isConnected()) {
            byte [] msg = ChatMessage.serialize(data);
            ByteBuffer b = ByteBuffer.allocate(4);
            b.putInt(msg.length);
            outputStream.write(b.array());
            outputStream.flush();
            outputStream.write(msg);
            outputStream.flush();
        }
    }

    public void run(){
        try {
            connect();
            sendData();
            cancel();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                bTSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
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