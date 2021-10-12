package ca.shalominc.it.smartbeats.ui.settings;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import ca.shalominc.it.smartbeats.R;

public class SettingsFragment extends Fragment {

    Switch portraitSwitch;
    int flag = 1;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        portraitSwitch = view.findViewById(R.id.shalom_switch1);

        portraitSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                switch (flag)
                {
                    case 1:
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        Toast.makeText(getContext(),"Portrait Lock",Toast.LENGTH_LONG).show();
                        flag++;

                        break;

                    case 2:
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                        Toast.makeText(getContext(),"Portrait Lock Disabled",Toast.LENGTH_LONG).show();
                        flag=1;

                        break;
                }
            }
        });
    }



}