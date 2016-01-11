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
import android.widget.EditText;

public class ProfileTab extends Fragment {

    private EditText nickname;
    private EditText surname;
    private EditText givenname;
    private EditText age;
    private EditText description;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_tab, container, false);

        nickname = (EditText) view.findViewById(R.id.nickname);
        surname = (EditText) view.findViewById(R.id.surname);
        givenname = (EditText) view.findViewById(R.id.givenname);
        age = (EditText) view.findViewById(R.id.age);
        description = (EditText) view.findViewById(R.id.description);

        SharedPreferences myPrefs = getActivity().getSharedPreferences("localchat-profile", Context.MODE_PRIVATE);
        nickname.setText(myPrefs.getString("profile-nickname", ""));
        surname.setText(myPrefs.getString("profile-surname", ""));
        givenname.setText(myPrefs.getString("profile-givenname", ""));
        age.setText(myPrefs.getString("profile-age", ""));
        description.setText(myPrefs.getString("profile-description", ""));

        return view;
    }

    @Override
    public void onPause() {

        SharedPreferences myPrefs = getActivity().getSharedPreferences("localchat-profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("profile-nickname", nickname.getText().toString());
        editor.putString("profile-surname", surname.getText().toString());
        editor.putString("profile-givenname", givenname.getText().toString());
        editor.putString("profile-age", age.getText().toString());
        editor.putString("profile-description", description.getText().toString());

        editor.commit();
        super.onPause();
    }
}