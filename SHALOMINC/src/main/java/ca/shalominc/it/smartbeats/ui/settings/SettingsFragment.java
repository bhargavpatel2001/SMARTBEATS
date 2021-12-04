//Bhargav Patel (N01373029) & Ripal Patel (N01354619) & Vidhi Kanhye (N01354573) & Nicholas Mohan (N01361663), Section-RNA
package ca.shalominc.it.smartbeats.ui.settings;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.shalominc.it.smartbeats.AboutUsActivity;
import ca.shalominc.it.smartbeats.PrivacyPolicyActivity;
import ca.shalominc.it.smartbeats.R;

public class SettingsFragment extends Fragment {
//extends PreferenceFragmentCompat {

    int flag = 1;
    Button shalomShowSettings;
    Button aboutUSBTN, privacyPolicyBTN;
    Switch nightMode, portraitLock;
    FloatingActionButton shalomFAB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        aboutUSBTN = view.findViewById(R.id.about_us_btn);
        privacyPolicyBTN = view.findViewById(R.id.shalom_privacy_policy_btn);
        nightMode = view.findViewById(R.id.shalom_night_mode_switch);
        portraitLock = view.findViewById(R.id.shalom_portrait_switch);
        shalomFAB = view.findViewById(R.id.shalom_floatingbutton);                                  //Floating Point Button

        aboutUSBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getContext(), AboutUsActivity.class);
                startActivity(intent);
            }
        });

        privacyPolicyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getContext(), PrivacyPolicyActivity.class);
                startActivity(intent);

            }
        });

        nightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


            }
        });

        portraitLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                switch (flag)
                {
                    case 1:
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        Toast.makeText(getContext(),R.string.lock_portrait,Toast.LENGTH_LONG).show();
                        flag++;

                        break;

                    case 2:
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                        Toast.makeText(getContext(),R.string.portrait_lock_disable,Toast.LENGTH_LONG).show();
                        flag=1;

                        break;
                }
            }
        });

        //For the FAB
        shalomFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/gmail/about/"));
                startActivity(intent);
            }
        });

    }

    //Sets Visibility to false in this fragment for power button In menu
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu)
    {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.musicBtn).setVisible(false);
        menu.findItem(R.id.lightsPwrBtn).setVisible(false);
        menu.findItem(R.id.bluetoothBtn).setVisible(false);
    }

}