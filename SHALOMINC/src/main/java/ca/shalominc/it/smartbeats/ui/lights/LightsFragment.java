package ca.shalominc.it.smartbeats.ui.lights;

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

import ca.shalominc.it.smartbeats.R;

public class LightsFragment extends Fragment {


    private TextView shalomTV;

    // two buttons to open color picker dialog and one to
    // set the color for GFG text
    private Button shalomColorBtn, shalomColorPBtn;

    // view box to preview the selected color
    private View shalomPreview;

    // this is the default color of the preview box
    private int shalomDefault;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lights, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // register the GFG text with appropriate ID
        shalomTV = view.findViewById(R.id.shalom_heading);

        // register two of the buttons with their
        // appropriate IDs
        shalomColorPBtn = view.findViewById(R.id.shalom_pick_color_btn);
        shalomColorBtn = view.findViewById(R.id.shalom_set_color_btn);

        // and also register the view which shows the
        // preview of the color chosen by the user
        shalomPreview = view.findViewById(R.id.shalom_preview_color);

        // set the default color to 0 as it is black
        shalomDefault = 0;

        // handling the Pick Color Button to open color
        // picker dialog
        shalomColorPBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        new ColorPickerPopup.Builder(getActivity())
                                .initialColor(Color.RED)
                                .enableBrightness(true)
                                .enableAlpha(true)
                                .okTitle("Choose")
                                .cancelTitle("Cancel")
                                .showIndicator(true)
                                .showValue(true)
                                .build()
                                .show(v, new ColorPickerPopup.ColorPickerObserver() {
                                            @Override
                                            public void
                                            onColorPicked(int color) {
                                                // set the color
                                                // which is returned
                                                // by the color
                                                // picker
                                                shalomDefault = color;

                                                // now as soon as
                                                // the dialog closes
                                                // set the preview
                                                // box to returned
                                                // color
                                                shalomPreview.setBackgroundColor(shalomDefault);
                                            }
                                        });
                    }
                });

        // handling the Set Color button to set the selected
        // color for the GFG text.
        shalomColorBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // now change the value of the GFG text
                        // as well.
                        shalomTV.setTextColor(shalomDefault);
                    }
                });
    }
}