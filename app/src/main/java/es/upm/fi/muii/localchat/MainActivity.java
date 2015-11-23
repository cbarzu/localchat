package es.upm.fi.muii.localchat;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
public class MainActivity extends FragmentActivity {

    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost= (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(),android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("settings_tab").setIndicator("Ajustes"), SettingsTab.class, null);
        tabHost.addTab(tabHost.newTabSpec("chat_tab").setIndicator("Chat"), ChatTab.class, null);
        tabHost.addTab(tabHost.newTabSpec("profile_tab").setIndicator("Perfil"), ProfileTab.class, null);
        tabHost.addTab(tabHost.newTabSpec("map_tab").setIndicator("Mapa"), MapTab.class, null);

    }
}