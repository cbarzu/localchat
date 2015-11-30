package es.upm.fi.muii.localchat.BluetoothManager;

/**
 * Created by claudiu on 29/11/2015.
 */
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.GregorianCalendar;
import java.util.UUID;
import java.util.Calendar;
import android.os.Handler;

import es.upm.fi.muii.localchat.Constants;
import es.upm.fi.muii.localchat.chat.ChatMessage;
import es.upm.fi.muii.localchat.chat.ChatView;

/**
 * Created by User on 6/5/2015.
 */
public class ServerConnectThread extends Thread {
    private BluetoothSocket bTSocket;
    private static final UUID MY_UUID_INSECURE =  UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private BluetoothServerSocket temp = null;
    private Handler mHandler;
    private boolean continuar = true;

    public ServerConnectThread() {
    }

    public ServerConnectThread(BluetoothAdapter bTAdapter, Handler h){
        try {
            temp = bTAdapter.listenUsingRfcommWithServiceRecord("Service_Name", MY_UUID_INSECURE);
            mHandler = h;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptConnect() {
        Calendar c = new GregorianCalendar();

        byte [] mensaje = new byte[2048];

        while (continuar) {
            try {
                bTSocket = temp.accept();
                InputStream io = bTSocket.getInputStream();
                int longitud = io.read(mensaje);
                String readMessage = new String(mensaje, 0, longitud);
                //ChatView.conversation.add( new ChatMessage(0L, readMessage, c.getTimeInMillis()));
                // Send the name of the connected device back to the UI Activity
                Message msg = mHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("mensaje_recibido", readMessage);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
                Log.d("SERVERCONNECT",readMessage);

            } catch (IOException e) {
                Log.d("SERVERCONNECT", "Could not accept an incoming connection.");
                break;
            }
            if (bTSocket != null) {
                try {
                    bTSocket.close();
                } catch (IOException e) {
                    Log.d("SERVERCONNECT", "Could not close ServerSocket:" + e.toString());
                }
                break;
            }
        }
    }

    public void setContinuarToFalse(){
        this.continuar = false;
    }

    @Override
    public void run(){
        while (continuar) {
            acceptConnect();
        }
    }

    public void closeConnect() {
        try {
            bTSocket.close();
        } catch (IOException e) {
            Log.d("SERVERCONNECT", "Could not close connection:" + e.toString());
        }
    }
}