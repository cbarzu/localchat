package es.upm.fi.muii.localchat.chat;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Calendar;

import es.upm.fi.muii.localchat.R;

public class ChatView extends FragmentActivity {

    private ListView listView;
    private Conversation conversation;
    private EditText editText;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);

        calendar = Calendar.getInstance();

        listView = (ListView) findViewById(R.id.messageList);
        conversation = new Conversation(getApplicationContext(), R.layout.item_conversation);

        listView.setAdapter(conversation);

        editText = (EditText) findViewById(R.id.editText);
    }

    public void sendMessage(View button) {

        if (editText.getText().length() > 0) {
            conversation.add(new Message(0L, editText.getText().toString(), calendar.getTimeInMillis()));
            editText.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
