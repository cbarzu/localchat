package es.upm.fi.muii.localchat.chat;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.upm.fi.muii.localchat.R;

/**
 * Created by Titanium on 22/11/15.
 */
public class Conversation extends ArrayAdapter<Message> {

    private List<Message> messages;
    private LinearLayout itemLayout;
    private TextView itemMessage;
    private TextView itemOwner;

    public Conversation(Context contexto, int textViewId) {

        super(contexto, textViewId);
        this.messages = new ArrayList<>();
    }

    @Override
    public void add(Message message) {

        this.messages.add(message);
        super.add(message);
    }

    @Override
    public int getCount() {

        return this.messages.size();
    }

    @Override
    public Message getItem(int index) {

        return this.messages.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_conversation, parent, false);
        }

        itemLayout = (LinearLayout) row.findViewById(R.id.wrapper);
        itemMessage = (TextView) row.findViewById(R.id.message);
        itemOwner = (TextView) row.findViewById(R.id.owner);

        Message message = getItem(position);

        boolean sent = (message.getWriter() > 0);

        itemLayout.setGravity(!sent ? Gravity.LEFT : Gravity.RIGHT);
        itemLayout.setBackgroundResource(sent ? R.drawable.bubble_yellow : R.drawable.bubble_blue);
        itemMessage.setText(message.getMessage());
        itemMessage.setGravity(sent ? Gravity.LEFT : Gravity.RIGHT);
        itemOwner.setText(sent ? "Él" : "Yo");
        itemOwner.setGravity(sent ? Gravity.LEFT : Gravity.RIGHT);

        return row;
    }
}
