package es.upm.fi.muii.localchat.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import es.upm.fi.muii.localchat.R;
import es.upm.fi.muii.localchat.utils.SerialBitmap;

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
        ImageButton btnAudio = (ImageButton) row.findViewById(R.id.controllerAudio);

        ChatMessage chatMessage = getItem(position);
        boolean sent = (!chatMessage.getWriter().isEmpty());


        itemLayout.setGravity((sent ? Gravity.START : Gravity.END));
        itemWrapper.setGravity((sent ? Gravity.START : Gravity.END));
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String date = format.format(new Date(chatMessage.getTimestamp()));
        itemOwner.setText((sent ? "Received at " : "Sent at ")  + date);

        if (chatMessage.messageType() == 0 ){ //is text message

            itemWrapper.setGravity((sent ? Gravity.START : Gravity.END));
            itemMessage.setText((String) chatMessage.getMessage());
            contentForMsg.setBackgroundResource(sent ? R.drawable.bubble_yellow : R.drawable.bubble_blue);

            imageView.setVisibility(View.GONE);
            btnAudio.setVisibility(View.GONE);

            itemMessage.setVisibility(View.VISIBLE);

        }else if ( chatMessage.messageType() == 1) { // its a byte [] photo.
            itemWrapper.setBackgroundResource(android.R.color.transparent);
            Bitmap bmp=Bitmap.createScaledBitmap(SerialBitmap.deserialize_bitmap((byte [])chatMessage.getMessage()), 200,200, true);
            imageView.setImageBitmap(bmp);

            itemMessage.setVisibility(View.GONE);
            btnAudio.setVisibility(View.GONE);

        } else if ( chatMessage.messageType() == 2) { // Audio

            itemWrapper.setBackgroundResource(android.R.color.transparent);
            btnAudio.setBackgroundResource(android.R.color.transparent);
            btnAudio.setVisibility(View.VISIBLE);
            btnAudio.setOnClickListener(audioListener);

            itemMessage.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);

            Map<String,byte []> msg = (Map<String,byte []>)chatMessage.getMessage();

            String filename = msg.keySet().iterator().next();
            btnAudio.setTag(filename);
        }
        return row;
    }

    private View.OnClickListener audioListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String filename = (String)v.getTag();
            try {
                FileInputStream fs = new FileInputStream(filename);
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(filename);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
}
