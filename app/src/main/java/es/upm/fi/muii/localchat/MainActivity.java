package es.upm.fi.muii.localchat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.DragEvent;

import java.util.Calendar;

import es.upm.fi.muii.localchat.BluetoothManager.ServerConnectThread;
import es.upm.fi.muii.localchat.chat.ChatMessage;
import es.upm.fi.muii.localchat.chat.ChatView;
import es.upm.fi.muii.localchat.chat.GlobalChatView;

/**
 * Created by Titanium on 24/11/15.
 */
public class MainActivity extends FragmentActivity {

    private FragmentTabHost tabHost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        tabHost= (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("settings_tab")
                        .setIndicator(getResources().getText(R.string.action_settings).toString()),
                SettingsTab.class, null);
        tabHost.addTab(tabHost.newTabSpec("chat_tab")
                        .setIndicator(getResources().getText(R.string.action_chat).toString()),
                DeviceListActivity.class, null);
        tabHost.addTab(tabHost.newTabSpec("profile_tab")
                              .setIndicator(getResources().getText(R.string.action_profile).toString()),
                                            ProfileTab.class, null);
        /*tabHost.addTab(tabHost.newTabSpec("map_tab")
                              .setIndicator(getResources().getText(R.string.action_map).toString()),
                                            MapTab.class, null);*/

        ft.commit();



    }

}
