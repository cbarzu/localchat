package es.upm.fi.muii.localchat;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.DragEvent;

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
        tabHost.addTab(tabHost.newTabSpec("settings_tab").setIndicator("Ajustes"), SettingsTab.class, null);
        tabHost.addTab(tabHost.newTabSpec("chat_tab").setIndicator("Chat"), DeviceListActivity.class, null);
        tabHost.addTab(tabHost.newTabSpec("profile_tab").setIndicator("Perfil"), ProfileTab.class, null);
        tabHost.addTab(tabHost.newTabSpec("map_tab").setIndicator("Mapa"), MapTab.class, null);

        ft.commit();
    }
}
