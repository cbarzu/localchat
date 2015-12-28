package es.upm.fi.muii.localchat.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
        ImageView imageView = (ImageView) row.findViewById(R.id.imageInMssage);
        LinearLayout contentForMsg = (LinearLayout) row.findViewById(R.id.contentWithBackground);

        ChatMessage chatMessage = getItem(position);
        boolean sent = (chatMessage.getWriter() > 0);


        itemLayout.setGravity((sent ? Gravity.START : Gravity.END));
        itemWrapper.setGravity((sent ? Gravity.START : Gravity.END));
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String date = format.format(new Date(chatMessage.getTimestamp()));
        itemOwner.setText((sent ? "Received at " : "Sent at ")  + date);

        if (chatMessage.messageType() == 0 ){ //is text message
            itemWrapper.setGravity((sent ? Gravity.START : Gravity.END));
            itemMessage.setText((String) chatMessage.getMessage());
            contentForMsg.setBackgroundResource(sent ? R.drawable.bubble_yellow : R.drawable.bubble_blue);
            imageView.setImageResource(android.R.color.transparent);

        }else if ( chatMessage.messageType() == 1) { // its a photo.
            itemWrapper.setBackgroundResource(android.R.color.transparent);
            Bitmap bmp=Bitmap.createScaledBitmap((Bitmap) chatMessage.getMessage(), 200,200, true);
            imageView.setImageBitmap(bmp);
        }
        return row;
    }
}
