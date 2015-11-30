package es.upm.fi.muii.localchat.chat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.Calendar;

import es.upm.fi.muii.localchat.BluetoothManager.ConnectThread;
import es.upm.fi.muii.localchat.BluetoothManager.ServerConnectThread;
import es.upm.fi.muii.localchat.R;

public class ChatView extends FragmentActivity {

    private ListView listView;
    private Conversation conversation;
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

        servidor = new ServerConnectThread(BluetoothAdapter.getDefaultAdapter());
        servidor.start();
        editText = (EditText) findViewById(R.id.editText);

        Button sendButton = (Button) findViewById(R.id.button_send);

        setupWatcher(editText, sendButton);
        if (editText.getText().length() > 0) {
            conversation.add(new Message(0L, editText.getText().toString(), calendar.getTimeInMillis()));

            Intent intent = getIntent();
            BluetoothDevice device = (BluetoothDevice) intent.getExtras().getParcelable("device_address");
            cliente = new ConnectThread(editText.getText().toString(),device);
            cliente.start();
    }

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
            conversation.add(new Message(0L, editText.getText().toString(), calendar.getTimeInMillis()));
            editText.setText("");
        }
    }

    public void onDestroy(){
        super.onDestroy();
        servidor.closeConnect();
        servidor.destroy();
    }
}
