//Bhargav Patel (N01373029) & Ripal Patel (N01354619) & Vidhi Kanhye (N01354573) & Nicholas Mohan (N01361663), Section-RNA
package ca.shalominc.it.smartbeats.ui.settings;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.Value;

import ca.shalominc.it.smartbeats.AboutUsActivity;
import ca.shalominc.it.smartbeats.PrivacyPolicyActivity;
import ca.shalominc.it.smartbeats.R;

public class SettingsFragment extends Fragment {

    int flag = 1;
    Button shalomAboutUsBtn, shalomPrivacyPolicyBtn;
    Switch shalomNightModeSwitch, shalomPortraitLockSwitch;
    FloatingActionButton shalomFloatBtn;
    TextView shalomTempTv, shalomTempTv2, shalomAmpTv;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        shalomAboutUsBtn = view.findViewById(R.id.shalom_settings_aboutus_btn);
        shalomPrivacyPolicyBtn = view.findViewById(R.id.shalom_settings_privacypolicy_btn);
        shalomNightModeSwitch = view.findViewById(R.id.shalom_settings_nightmode_switch);
        shalomPortraitLockSwitch = view.findViewById(R.id.shalom_settings_portrait_switch);
        shalomFloatBtn = view.findViewById(R.id.shalom_settings_floating_button);//Floating Point Button
        shalomTempTv = view.findViewById(R.id.shalom_settings_Temp_tv);
        shalomTempTv2 = view.findViewById(R.id.shalom_settings_Temp_tv2);
        shalomAmpTv = view.findViewById(R.id.shalom_settings_Amp_tv);

        createNightMode();
        shalomNightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                nightOnOff(isChecked);
            }
        });

        shalomPortraitLockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                portraitOnOff();
            }
        });

        shalomAboutUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getContext(), AboutUsActivity.class);
                startActivity(intent);
            }
        });

        shalomPrivacyPolicyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getContext(), PrivacyPolicyActivity.class);
                startActivity(intent);

            }
        });


        getCelsius();
        getFahrenheit();
        getAmps();

        //For the FAB
        shalomFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{"shalominc.smartbeats@gmail.com"});
                Email.putExtra(Intent.EXTRA_TEXT, "Dear SmartBeats," + "");
                startActivity(Intent.createChooser(Email, "Send Feedback:"));
            }
        });
    }



    public void getCelsius(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Fahrenheit-Celsius-Data/1-set/Celsius: ");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String value = snapshot.getValue(String.class) + " C";
                shalomTempTv.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getFahrenheit(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Fahrenheit-Celsius-Data/1-set/Fahrenheit: ");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String value = snapshot.getValue(String.class) + " F";
                shalomTempTv2.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAmps(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Amps-Data/1-set/Amperage Draw: ");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String value = snapshot.getValue(String.class);
                shalomAmpTv.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
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

    public void createNightMode(){
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
        {
            shalomNightModeSwitch.setChecked(true);
            getActivity().setTheme(R.style.SMARTBEATS_dark);
        } else {
            getActivity().setTheme(R.style.SMARTBEATS);
        }
    }

    public void nightOnOff(boolean isChecked){
        if(isChecked){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Toast.makeText(getContext(),R.string.night_enabled,Toast.LENGTH_LONG).show();
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Toast.makeText(getContext(),R.string.night_disabled,Toast.LENGTH_LONG).show();
        }
    }

    public void portraitOnOff(){
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
}