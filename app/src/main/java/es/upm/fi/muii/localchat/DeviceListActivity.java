/**
 * Localchat
 *
 * @author Ignacio Molina Cuquerella
 * @author Claudiu Barzu
 */

package es.upm.fi.muii.localchat;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.content.SharedPreferences;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import es.upm.fi.muii.localchat.BluetoothManager.ConnectThread;
import es.upm.fi.muii.localchat.BluetoothManager.ServerConnectThread;
import es.upm.fi.muii.localchat.BluetoothManager.NetworkMessage;
import es.upm.fi.muii.localchat.chat.ChatView;
import es.upm.fi.muii.localchat.chat.Conversation;
import es.upm.fi.muii.localchat.chat.GlobalChatView;
import es.upm.fi.muii.localchat.profile.Profile;

/**
 * This Activity appears as a dialog. It lists any paired devices and
 * devices detected in the area after discovery. When a device is chosen
 * by the user, the MAC address of the device is sent back to the parent
 * Activity in the result Intent.
 */

public class DeviceListActivity extends Fragment {

    /**
     * Tag for Log
     */
    private static final String TAG = "DeviceListActivity";

    /**
     * Return Intent extra
     */
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_CHATROOM_ID = "chat_global_id";

    public static BluetoothAdapter bManager;
    private ServerConnectThread servidor;
    private Calendar calendar;
    //private Conversation conversation;
    public static Map<String, Conversation> conversations;



    /**
     * Newly discovered devices
     */
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    private FragmentActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.getData().getString("chatroom").isEmpty()) {

                conversations.get(msg.getData().getString("user")).add((NetworkMessage) msg.getData().getSerializable("mensaje_recibido"));
            } else {

                conversations.get(msg.getData().getString("chatroom")).add((NetworkMessage) msg.getData().getSerializable("mensaje_recibido"));
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.chat_tab, container, false);


        mainActivity = getActivity();
        setupBluetooth();
        conversations = new HashMap<>();
        servidor = new ServerConnectThread(DeviceListActivity.bManager , mHandler, getContext());
        servidor.start();
        calendar = Calendar.getInstance();

        // Set result CANCELED in case the user backs out
        mainActivity.setResult(FragmentActivity.RESULT_CANCELED);

        ArrayAdapter<String> chatroomsArrayAdapter =
                new ArrayAdapter<>(mainActivity, R.layout.device_name);

        String chatroomName = getResources().getText(R.string.chatroom_name).toString();
        chatroomsArrayAdapter.add(chatroomName);

        // Find and set up the ListView for chatrooms
        ListView chatroomListView = (ListView) view.findViewById(R.id.chatrooms_list);
        chatroomListView.setAdapter(chatroomsArrayAdapter);
        chatroomListView.setOnItemClickListener(mChatroomClickListener);

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        // one for newly discovered devices
        ArrayAdapter<String> pairedDevicesArrayAdapter =
                new ArrayAdapter<>(mainActivity, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<>(mainActivity, R.layout.device_name);

        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) view.findViewById(R.id.paired_devices);
        pairedListView.setAdapter(pairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Find and set up the ListView for new devices
        ListView otherDevicesView = (ListView) view.findViewById(R.id.new_devices);
        otherDevicesView.setAdapter(mNewDevicesArrayAdapter);
        otherDevicesView.setOnItemClickListener(mDeviceClickListener);

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = bManager.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {

            view.findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {

                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }

        } else {

            String noDevices = getResources().getText(R.string.none_paired).toString();
            pairedDevicesArrayAdapter.add(noDevices);
        }

        doDiscovery();

        return view;
    }

    private void setupBluetooth() {

        bManager = BluetoothAdapter.getDefaultAdapter();
        if (!bManager.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 3);
        }

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mainActivity.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mainActivity.registerReceiver(mReceiver, filter);
    }


private void shareProfile() {

    Log.d("Chat", "Sharing profile");
    for (BluetoothDevice device : bManager.getBondedDevices()) {

        SharedPreferences myPrefs = getActivity().getSharedPreferences("localchat-profile", Context.MODE_PRIVATE);
        Profile profile = new Profile(myPrefs.getString("profile-nickname", bManager.getName()));
        profile.setSurename(myPrefs.getString("profile-surname", ""));
        profile.setGivenname(myPrefs.getString("profile-givenname", ""));
        profile.setAge(myPrefs.getString("profile-age", ""));
        profile.setDescription(myPrefs.getString("profile-description", ""));

        NetworkMessage msg = new NetworkMessage(profile, System.currentTimeMillis(), 3);
        ConnectThread client = new ConnectThread(msg, device);
        client.start();
    }
}

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {

        bManager.startDiscovery();
    }


    @Override
    public void onPause() {

        shareProfile();
        super.onPause();
    }

    @Override
    public void onResume() {

        super.onResume();
        shareProfile();
    }

    @Override
    public void onDestroy() {

        // Make sure we're not doing discovery anymore
        if (bManager != null) {

            bManager.cancelDiscovery();
        }

        // Unregister broadcast listeners
        mainActivity.unregisterReceiver(mReceiver);
        servidor.closeConnect();
        servidor.setContinuarToFalse();
        super.onDestroy();
    }

    /**
     * The on-click listener for all devices in the ListViews
     */
    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            // Cancel discovery because it's costly and we're about to connect
            bManager.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Create the result Intent and include the MAC address
            Intent intent = new Intent(mainActivity, ChatView.class);

            // Set chat parameters key:value (MAC address, etc)
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            startActivity(intent);
        }
    };

    /**
     * The on-click listener for all chatrooms in the ListViews
     */
    private AdapterView.OnItemClickListener mChatroomClickListener
            = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            // Cancel discovery because it's costly and we're about to connect
            bManager.cancelDiscovery();


            // Create the result Intent and include the MAC address
            Intent intent = new Intent(mainActivity, GlobalChatView.class);

            // Set global chat id
            intent.putExtra(EXTRA_CHATROOM_ID, "chat1");
            startActivity(intent);
        }
    };

    /**
     * The BroadcastReceiver that listens for discovered devices and changes the title when
     * discovery is finished
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    Log.e(TAG, "Find " + device.getName() + "\n" + device.getAddress());
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                mainActivity.setProgressBarIndeterminateVisibility(false);
                if (mNewDevicesArrayAdapter.getCount() == 0) {

                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };

}
