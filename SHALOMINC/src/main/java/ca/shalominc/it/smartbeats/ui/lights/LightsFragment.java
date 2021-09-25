package ca.shalominc.it.smartbeats.ui.lights;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ca.shalominc.it.smartbeats.R;

public class LightsFragment extends Fragment {

    private LightsViewModel lightsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        lightsViewModel =
                new ViewModelProvider(this).get(LightsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_lights, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        lightsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}