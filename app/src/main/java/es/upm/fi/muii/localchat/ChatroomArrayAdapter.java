/**
 * Localchat
 *
 * @author Ignacio Molina Cuquerella
 * @author Claudiu Barzu
 */

package es.upm.fi.muii.localchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Map;

import es.upm.fi.muii.localchat.chat.Conversation;

public class ChatroomArrayAdapter extends ArrayAdapter<String> {

    Map<String, Conversation> conversations;
    Context contexto;

    public ChatroomArrayAdapter(Context contexto, int viewId, String [] chatrooms) {

        super(contexto, viewId, chatrooms);

        this.contexto = contexto;
    }


    public void setConversations(Map<String, Conversation> conversations) {

        this.conversations = conversations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.item_devicelist, null);
        }

        TextView name = (TextView) row.findViewById(R.id.device_name);
        TextView unread = (TextView) row.findViewById(R.id.unread_messages);

        String chat = this.getItem(position);

        name.setText(chat);

        if (conversations != null) {

            Conversation conv = conversations.get(chat);

            if (conv != null) {

                if (conv.hasChanged()) {

                    row.findViewById(R.id.unread_bubble).setVisibility(View.VISIBLE);
                    unread.setText(String.valueOf(conv.unreadCount()));
                } else {
                    row.findViewById(R.id.unread_bubble).setVisibility(View.GONE);
                }
            }
        }

        return row;
    }
}
