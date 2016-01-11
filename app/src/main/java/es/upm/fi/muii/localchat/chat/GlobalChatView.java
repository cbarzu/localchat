/**
 * Localchat
 *
 * @author Ignacio Molina Cuquerella
 * @author Claudiu Barzu
 */

package es.upm.fi.muii.localchat.chat;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.upm.fi.muii.localchat.BluetoothManager.ConnectThread;
import es.upm.fi.muii.localchat.BluetoothManager.NetworkMessage;
import es.upm.fi.muii.localchat.DeviceListActivity;
import es.upm.fi.muii.localchat.R;
import es.upm.fi.muii.localchat.utils.AudioRecorder;
import es.upm.fi.muii.localchat.utils.SerialBitmap;

public class GlobalChatView extends FragmentActivity {

    private static final int RESULT_LOAD_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    private String chatroomId;
    private EditText editText;
    private ConnectThread cliente;
    private Conversation conversation;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);

        Intent intent = getIntent();
        chatroomId = (String)intent.getStringExtra("chat_global_id");
        ListView listView = (ListView) findViewById(R.id.messageList);
        conversation = DeviceListActivity.conversations.get(chatroomId);
        calendar = Calendar.getInstance();

        if (conversation == null) {

            conversation = new Conversation(getApplicationContext(), R.layout.item_conversation);
            DeviceListActivity.conversations.put(chatroomId, conversation);
        }

        listView.setAdapter(conversation);

        editText = (EditText) findViewById(R.id.editText);

        ImageButton sendButton = (ImageButton) findViewById(R.id.button_send);
        ImageButton audioButton = (ImageButton) findViewById(R.id.button_audio);
        audioButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    AudioRecorder aud = AudioRecorder.getInstance("down");
                    try {
                        Toast toast = Toast.makeText(getApplicationContext(), "Speak now",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP,0,0);
                        toast.show();
                        aud.record();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    AudioRecorder aud = AudioRecorder.getInstance("up");
                    aud.stop();
                    String filename = aud.getFilename();
                    byte b [] = aud.getAudioBytes();
                    if (b != null){
                        Map<String, byte []> audio = new HashMap<>(1);
                        audio.put(filename, b);
                        NetworkMessage msg =  new NetworkMessage(audio  , calendar.getTimeInMillis(),2);
                        conversation.add(msg);
                        sendMessageOverbluetooth(msg);
                    }
                }
                return true;
            }
        });

        setupWatcher(editText, sendButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_global_menu, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_global_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_send_photo_from_gallery:
                Intent i = new Intent(
                        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                return true;

            case R.id.action_take_photo:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        NetworkMessage msg = null;
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            msg =  new NetworkMessage(SerialBitmap.serialize_bitmap(BitmapFactory.decodeFile(picturePath)), calendar.getTimeInMillis(), 1);
            conversation.add(msg);

        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && null != data){

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            msg = new NetworkMessage(SerialBitmap.serialize_bitmap(imageBitmap), calendar.getTimeInMillis(), 1);
            conversation.add(msg);

        }
        sendMessageOverbluetooth(msg);

    }


    private void setupWatcher(final EditText message, final ImageButton send) {

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
            NetworkMessage msg =  new NetworkMessage(editText.getText().toString(), calendar.getTimeInMillis(),0);
            conversation.add(msg);

            sendMessageOverbluetooth(msg);
        }
        editText.setText("");
    }

    protected void onPause() {

        conversation.pause();
        super.onPause();
    }

    public void onDestroy(){
        super.onDestroy();
    }

    public void sendMessageOverbluetooth(NetworkMessage msg){

        for (BluetoothDevice device : DeviceListActivity.bManager.getBondedDevices()) {

            msg.setTarget(chatroomId);
            cliente = new ConnectThread(msg, device);
            cliente.start();
        }
    }
}
