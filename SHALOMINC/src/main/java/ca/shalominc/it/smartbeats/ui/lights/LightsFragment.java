//Bhargav Patel (N01373029) & Ripal Patel (N01354619) & Vidhi Kanhye (N01354573) & Nicholas Mohan (N01361663), Section-RNA

package ca.shalominc.it.smartbeats.ui.lights;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;

import ca.shalominc.it.smartbeats.ModeAdapter;
import ca.shalominc.it.smartbeats.ModeItem;
import top.defaults.colorpicker.ColorPickerPopup;

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
import top.defaults.colorpicker.ColorPickerView;

public class LightsFragment extends Fragment {

    private TextView shalomTV;

    private Button shalomColorBtn, shalomColorPBtn;

    private View shalomPreview;

    private int shalomDefault;

    private ArrayList<ModeItem> mModeList;

    private ModeAdapter mAdapter;

    private Spinner spinnerMode;

    String clickedModeName = "";

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

        shalomTV = view.findViewById(R.id.shalom_heading);                                          //Heading SMARTBEATS Text View

        shalomColorPBtn = view.findViewById(R.id.shalom_pick_color_btn);                            //Color Picker Button
        shalomColorBtn = view.findViewById(R.id.shalom_set_color_btn);                              //Color Set Button

        shalomPreview = view.findViewById(R.id.shalom_preview_color);                               //Shows Pre view

        //Spinner Code For Light Mode
        spinnerMode = view.findViewById(R.id.shalom_spinner);                                       //Light mode Spinner
        mAdapter = new ModeAdapter(getContext(),mModeList);
        spinnerMode.setAdapter(mAdapter);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        int spinnerValue = sharedPref.getInt("userChoiceSpinner",-1);
        if (spinnerValue != -1){
            spinnerMode.setSelection(spinnerValue);
        }
        spinnerMode.setSelection(spinnerValue);

        spinnerMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ModeItem clickedItem = (ModeItem) parent.getItemAtPosition(position);
                clickedModeName = clickedItem.getModeName();

                int userChoice = spinnerMode.getSelectedItemPosition();
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
        shalomColorPBtn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(final View v)
                    {
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

                                        shalomPreview.setBackgroundColor(shalomDefault);
                                    }
                                })
                                .setPositiveButton(getString(R.string.colour_picker_dialog_ok_btn), new ColorPickerClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors)
                                    {
                                        int r = (selectedColor >> 16) & 0xff;
                                        int g = (selectedColor >> 8) & 0xff;
                                        int b = (selectedColor >> 0) & 0xff;
                                        //log.d("rgb","r [" + r + "] - g [" + g + "] - b [" + b + "]");
                                        sendColor(r,g,b);

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
                });


        //Color Set Button.
        shalomColorBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        shalomTV.setTextColor(shalomDefault);
                    }
                });
    }
//Function to send the data to Firestore
    private void sendColor(int r, int g, int b){

        //Creating a HashMap to send the info of the selected color and mode
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("r",r);
        hashMap.put("g",g);
        hashMap.put("b",b);
        hashMap.put("mode",clickedModeName);

        FirebaseFirestore.getInstance().collection("user_lights")
                .document("rgb_controller_values")
                .set(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Didn't Work",Toast.LENGTH_SHORT).show();
                    }
                });

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