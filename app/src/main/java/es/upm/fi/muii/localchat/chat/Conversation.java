package es.upm.fi.muii.localchat.chat;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.upm.fi.muii.localchat.R;

/**
 * Created by Titanium on 22/11/15.
 */
public class Conversation extends ArrayAdapter<ChatMessage> {

    private List<ChatMessage> chatMessages;


    public Conversation(Context contexto, int textViewId) {

        super(contexto, textViewId);
        this.chatMessages = new ArrayList<>();
    }

    @Override
    public void add(ChatMessage chatMessage) {

        this.chatMessages.add(chatMessage);
        super.add(chatMessage);
    }

    @Override
    public int getCount() {

        return this.chatMessages.size();
    }

    @Override
    public ChatMessage getItem(int index) {

        return this.chatMessages.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_conversation, parent, false);
        }

        LinearLayout itemLayout = (LinearLayout) row.findViewById(R.id.item);
        LinearLayout itemWrapper = (LinearLayout) row.findViewById(R.id.wrapper);
        TextView itemMessage = (TextView) row.findViewById(R.id.chatMessage);
        TextView itemOwner = (TextView) row.findViewById(R.id.owner);

        ChatMessage chatMessage = getItem(position);

        boolean sent = (chatMessage.getWriter() > 0);

        itemLayout.setGravity((sent ? Gravity.START : Gravity.END));
        itemWrapper.setBackgroundResource(sent ? R.drawable.bubble_yellow : R.drawable.bubble_blue);
        itemWrapper.setGravity((sent ? Gravity.START : Gravity.END));
        itemMessage.setText(chatMessage.getMessage());

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String date = format.format(new Date(chatMessage.getTimestamp()));
        itemOwner.setText((sent ? "Él" : "Yo" ) + " - " + date);

        return row;
    }
}
