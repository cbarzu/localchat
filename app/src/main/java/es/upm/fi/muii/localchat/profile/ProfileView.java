/**
 * Localchat
 *
 * @author Ignacio Molina Cuquerella
 * @author Claudiu Barzu
 */

package es.upm.fi.muii.localchat.profile;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import es.upm.fi.muii.localchat.DeviceListActivity;
import es.upm.fi.muii.localchat.R;
import es.upm.fi.muii.localchat.chat.Conversation;

public class ProfileView extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        Intent intent = getIntent();
        String user = (String) intent.getStringExtra("device_address");

        Conversation conversation = DeviceListActivity.conversations.get(user);

        if (conversation != null) {

            Profile profile = conversation.getProfile();

            if (profile != null) {

                TextView nickname = (TextView) findViewById(R.id.nickname);
                nickname.setText(profile.getNickname());
                TextView surname = (TextView) findViewById(R.id.surname);
                surname.setText(profile.getSurename());
                TextView givenname = (TextView) findViewById(R.id.givenname);
                givenname.setText(profile.getGivenname());
                TextView age = (TextView) findViewById(R.id.age);
                age.setText("(Edad: " + profile.getAge() + ")");
                TextView description = (TextView) findViewById(R.id.description);
                description.setText(profile.getDescription());
            }
        }
    }
}
