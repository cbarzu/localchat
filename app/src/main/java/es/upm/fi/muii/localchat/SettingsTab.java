/**
 * Localchat
 *
 * @author Ignacio Molina Cuquerella
 * @author Claudiu Barzu
 */

package es.upm.fi.muii.localchat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsTab extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_tab, container, false);

        Switch switch1 = (Switch) view.findViewById(R.id.switch1);
        SharedPreferences myPrefs = getActivity().getSharedPreferences("localchat-settings", Context.MODE_PRIVATE);
        switch1.setChecked(myPrefs.getBoolean("switch-share-profile", true));

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences myPrefs = getActivity().getSharedPreferences("localchat-settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.putBoolean("switch-share-profile", buttonView.isChecked());
                editor.commit();
            }
        });


        return view;
    }
}