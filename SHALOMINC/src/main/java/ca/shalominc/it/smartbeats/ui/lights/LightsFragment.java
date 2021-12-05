//Bhargav Patel (N01373029) & Ripal Patel (N01354619) & Vidhi Kanhye (N01354573) & Nicholas Mohan (N01361663), Section-RNA

package ca.shalominc.it.smartbeats.ui.lights;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;

import ca.shalominc.it.smartbeats.ModeAdapter;
import ca.shalominc.it.smartbeats.ModeItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import ca.shalominc.it.smartbeats.R;

public class LightsFragment extends Fragment {

    private TextView shalomHeadingTV;

    private Button shalomSetBtn, shalomChooseColorBtn;

    private View shalomPreView;

    private int shalomDefault;

    private ArrayList<ModeItem> mModeList;

    private ModeAdapter shalomAdapter;

    private Spinner shalomLightsSpinner;

    private SeekBar shalomSeekbar;

    String clickedModeName = "";

    int r,g,b,brightness;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lights, container, false);
    }

    //Sets Visibilty to false in this fragment for Music note In menu
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu)
    {
        setHasOptionsMenu(true);
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.musicBtn).setVisible(false);
        menu.findItem(R.id.lightsPwrBtn).setVisible(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        initList();

        shalomHeadingTV = view.findViewById(R.id.shalom_color_heading_tv);                          //Heading SMARTBEATS Text View
        shalomChooseColorBtn = view.findViewById(R.id.shalom_color_choose_btn);                     //Color Picker Button
        shalomSetBtn = view.findViewById(R.id.shalom_color_set_btn);                                //Color Set Button
        shalomPreView = view.findViewById(R.id.shalom_color_preview_view);                          //Shows Pre view

        //Spinner Code For Light Mode
        shalomLightsSpinner = view.findViewById(R.id.shalom_color_lights_spinner);                   //Light mode Spinner

        shalomAdapter = new ModeAdapter(getContext(),mModeList);
        shalomLightsSpinner.setAdapter(shalomAdapter);

        //Brightness Seekbar
        shalomSeekbar = view.findViewById(R.id.shalom_color_brightness_seekBar);                                     //Brightness SeekBar

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        int spinnerValue = sharedPref.getInt("userChoiceSpinner",-1);
        if (spinnerValue != -1){
            shalomLightsSpinner.setSelection(spinnerValue);
        }
        shalomLightsSpinner.setSelection(spinnerValue);

        // Spinner Items Selections Configuration
        shalomLightsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ModeItem clickedItem = (ModeItem) parent.getItemAtPosition(position);
                clickedModeName = clickedItem.getModeName();

                int userChoice = shalomLightsSpinner.getSelectedItemPosition();
                SharedPreferences.Editor prefEditor = sharedPref.edit();
                prefEditor.putInt("userChoiceSpinner", userChoice);
                prefEditor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        shalomDefault = 0;

        //Color Picker button
        shalomChooseColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                createColorPicker();
            }
        });

        //Value sender for brightness to the database
        shalomSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightness = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Color Set Button.
        shalomSetBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        sendColor(r,g,b);
                    }
                });
    }
    //Function to send the data to Firestore
    private void sendColor(int r, int g, int b){

        //Calling BluetoothAdapter
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Creating a HashMap to send the info of the selected color and mode
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("r",r);
        hashMap.put("g",g);
        hashMap.put("b",b);
        hashMap.put("mode",clickedModeName);
        hashMap.put("brightness",brightness);

        if(bluetoothAdapter.isEnabled()) {

            FirebaseFirestore.getInstance().collection("user_lights")
                    .document("rgb_controller_values")
                    .set(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            shalomHeadingTV.setTextColor(shalomDefault);
                            Toast.makeText(getContext(),R.string.successMsg, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),R.string.FireStoreOff, Toast.LENGTH_SHORT).show();
                        }
                    });
        } else
        {
            Toast.makeText(getContext(),R.string.bluetoothOn,Toast.LENGTH_SHORT).show();
        }

    }

    //Creating Color Picker and sending color value to database;
    public void createColorPicker(){
        ColorPickerDialogBuilder
                .with(getActivity(),R.style.ColourPickerDialogTheme)
                .setTitle(getString(R.string.colour_picker_dialog_title))
                .initialColor(Color.RED)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener()
                {
                    @Override
                    public void onColorSelected(int selectedColor)
                    {
                        shalomDefault = selectedColor;

                        shalomPreView.setBackgroundColor(shalomDefault);
                    }
                })
                .setPositiveButton(getString(R.string.colour_picker_dialog_ok_btn), new ColorPickerClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors)
                    {
                        r = (selectedColor >> 16) & 0xff;
                        g = (selectedColor >> 8) & 0xff;
                        b = (selectedColor >> 0) & 0xff;

                    }
                })
                .setNegativeButton(getString(R.string.colour_picker_dialog_cancel_btn), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                })
                .build()
                .show();
    }

    // Function for Providing images to spinner
    private void initList(){
        mModeList = new ArrayList<>();
        mModeList.add(new ModeItem(getString(R.string.select_light_mode),R.mipmap.speak));
        mModeList.add(new ModeItem(getString(R.string.party_mode),R.drawable.party));
        mModeList.add(new ModeItem(getString(R.string.zen_mode),R.drawable.zen));
        mModeList.add(new ModeItem(getString(R.string.workout_mode),R.drawable.workout));
        mModeList.add(new ModeItem(getString(R.string.focus_mode),R.drawable.focus1));
        mModeList.add(new ModeItem(getString(R.string.sleep_mode),R.drawable.sleep));
    }
}