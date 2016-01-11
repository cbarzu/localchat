/**
 * Localchat
 *
 * @author Ignacio Molina Cuquerella
 * @author Claudiu Barzu
 */

package es.upm.fi.muii.localchat;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import es.upm.fi.muii.localchat.chat.Conversation;

public class DevicesArrayAdapter extends ArrayAdapter<BluetoothDevice> {

    Map<String, Conversation> conversations;

    public DevicesArrayAdapter(Context contexto, int viewId, ArrayList<BluetoothDevice> devices) {

        super(contexto, viewId, devices);
    }

    public void setConversations(Map<String, Conversation> conversations) {

        this.conversations = conversations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_devicelist, parent, false);
        }

        TextView name = (TextView) row.findViewById(R.id.device_name);
        TextView address = (TextView) row.findViewById(R.id.device_address);
        TextView unread = (TextView) row.findViewById(R.id.unread_messages);

        BluetoothDevice device = this.getItem(position);

        name.setText(device.getName());
        address.setText(device.getAddress());

        if (conversations != null) {

            Conversation conv = conversations.get(device.getAddress());

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
