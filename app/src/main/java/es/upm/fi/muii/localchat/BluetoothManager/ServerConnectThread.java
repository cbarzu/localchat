/**
 * Localchat
 *
 * @author Ignacio Molina Cuquerella
 * @author Claudiu Barzu
 */

package es.upm.fi.muii.localchat.BluetoothManager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.util.UUID;
import android.os.Handler;

import es.upm.fi.muii.localchat.DeviceListActivity;
import es.upm.fi.muii.localchat.R;
import es.upm.fi.muii.localchat.chat.Conversation;

/**
 * Created by me on 6/5/2015.
 */
public class ServerConnectThread extends Thread {

    private BluetoothSocket bTSocket;
    private static final UUID MY_UUID_INSECURE =  UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private BluetoothServerSocket temp = null;
    private Handler mHandler;
    private boolean continuar = true;
    private Context context;

    public ServerConnectThread(BluetoothAdapter bTAdapter, Handler h, Context context){
        try {

            temp = bTAdapter.listenUsingRfcommWithServiceRecord("Service_Name", MY_UUID_INSECURE);
            mHandler = h;
            this.context = context;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptConnect() {

        while (continuar) {
            try {
                bTSocket = temp.accept();
                if ( ! DeviceListActivity.conversations.containsKey(bTSocket.getRemoteDevice().getAddress())){
                    DeviceListActivity.conversations.put(bTSocket.getRemoteDevice().getAddress(), new Conversation(context, R.layout.item_conversation));
                }
                ManageConnectThread m = new ManageConnectThread(bTSocket,mHandler);
                m.start();
            } catch (IOException e) {
                Log.d("SERVERCONNECT", "Could not accept an incoming connection.");
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