package ca.shalominc.it.smartbeats.ui.lights;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;
import top.defaults.colorpicker.ColorPickerPopup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import ca.shalominc.it.smartbeats.R;
import top.defaults.colorpicker.ColorPickerView;

public class LightsFragment extends Fragment {


    private TextView shalomTV;


    private Button shalomColorBtn, shalomColorPBtn;

    private View shalomPreview;

    private int shalomDefault;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lights, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shalomTV = view.findViewById(R.id.shalom_heading);


        shalomColorPBtn = view.findViewById(R.id.shalom_pick_color_btn);
        shalomColorBtn = view.findViewById(R.id.shalom_set_color_btn);


        shalomPreview = view.findViewById(R.id.shalom_preview_color);

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
}