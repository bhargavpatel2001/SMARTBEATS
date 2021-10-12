package ca.shalominc.it.smartbeats.ui.lights;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.graphics.Color;

import ca.shalominc.it.smartbeats.ModeAdapter;
import ca.shalominc.it.smartbeats.ModeItem;
import top.defaults.colorpicker.ColorPickerPopup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initList();

        shalomTV = view.findViewById(R.id.shalom_heading);


        shalomColorPBtn = view.findViewById(R.id.shalom_pick_color_btn);
        shalomColorBtn = view.findViewById(R.id.shalom_set_color_btn);


        shalomPreview = view.findViewById(R.id.shalom_preview_color);

        spinnerMode = view.findViewById(R.id.shalom_spinner);

        mAdapter = new ModeAdapter(getContext(),mModeList);
        spinnerMode.setAdapter(mAdapter);

        spinnerMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ModeItem clickedItem = (ModeItem) parent.getItemAtPosition(position);
                clickedModeName = clickedItem.getModeName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        shalomDefault = 0;


        shalomColorPBtn.setOnClickListener(
                new View.OnClickListener()
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




        shalomColorBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        shalomTV.setTextColor(shalomDefault);
                    }
                });
    }

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