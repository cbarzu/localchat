
package es.upm.fi.muii.localchat.BluetoothManager;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import es.upm.fi.muii.localchat.chat.ChatMessage;
import es.upm.fi.muii.localchat.utils.AudioRecorder;

/**
 * Created by claudiu on 15/12/2015.
 */

public class ManageConnectThread extends Thread {

    private BluetoothSocket socket;
    private Handler mHandler;

    public ManageConnectThread(BluetoothSocket aSocket,Handler aHandler ) {
        this.socket = aSocket;
        mHandler = aHandler;
    }


    public void run () {
        byte [] mensaje = new byte[64000];
        InputStream io = null;
        try {
            io = socket.getInputStream();
            int longitud = io.read(mensaje);
            byte [] msgRec = new byte[longitud];
            for (int i = 0; i < longitud ; i++) {
                msgRec[i] = mensaje[i];
            }
            ChatMessage readMessage= (ChatMessage)ChatMessage.deserialize(msgRec);
            readMessage.setWriter(socket.getRemoteDevice().getAddress());
            if (readMessage.messageType() == 2) { //is an audio chat
                Map<String,byte []> audio = (Map<String,byte []>)readMessage.getMessage();
                String filename = AudioRecorder.writeAudioToFile(audio.get(audio.keySet().iterator().next()));
                readMessage.setMessage(filename);
            }


            // Send the name of the connected device back to the UI Activity
            Message msg = mHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putSerializable("mensaje_recibido", readMessage);
            bundle.putString("user", socket.getRemoteDevice().getAddress());
            bundle.putString("chatroom", readMessage.getTarget());
            Log.d("MessageTarget", readMessage.getTarget());
            msg.setData(bundle);
            mHandler.sendMessage(msg);
            Log.d("ManageConnectThread", readMessage.toString());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}