package es.upm.fi.muii.localchat.chat;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.os.Message;

import java.util.Calendar;

import es.upm.fi.muii.localchat.BluetoothManager.ConnectThread;
import es.upm.fi.muii.localchat.BluetoothManager.ServerConnectThread;
import es.upm.fi.muii.localchat.DeviceListActivity;
import es.upm.fi.muii.localchat.R;

public class ChatView extends FragmentActivity {

    private ListView listView;
    public static Conversation conversation;
    private EditText editText;
    private Calendar calendar;
    private ConnectThread cliente;
    private ServerConnectThread servidor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);

        calendar = Calendar.getInstance();

        ListView listView = (ListView) findViewById(R.id.messageList);
        conversation = new Conversation(getApplicationContext(), R.layout.item_conversation);

        listView.setAdapter(conversation);

        servidor = new ServerConnectThread(DeviceListActivity.bManager , mHandler);
        servidor.start();
        editText = (EditText) findViewById(R.id.editText);

        Button sendButton = (Button) findViewById(R.id.button_send);

        setupWatcher(editText, sendButton);

    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            conversation.add( new ChatMessage(1L, msg.getData().getString("mensaje_recibido"), calendar.getTimeInMillis()));
        }
    };



    private void setupWatcher(final EditText message, final Button send) {

        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                send.setEnabled(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void sendMessage(View button) {

        if (editText.getText().length() > 0) {
            conversation.add(new ChatMessage(0L, editText.getText().toString(), calendar.getTimeInMillis()));

            Intent intent = getIntent();
            String address =  intent.getStringExtra("device_address");
            cliente = new ConnectThread(editText.getText().toString(), DeviceListActivity.bManager.getRemoteDevice(address));
            cliente.start();
        }
        editText.setText("");
    }

    public void onDestroy(){
        super.onDestroy();
        servidor.closeConnect();
        servidor.setContinuarToFalse();
    }



}
