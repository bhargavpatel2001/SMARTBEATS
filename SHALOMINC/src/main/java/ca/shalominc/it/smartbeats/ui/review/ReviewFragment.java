package ca.shalominc.it.smartbeats.ui.review;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.shalominc.it.smartbeats.R;


public class ReviewFragment extends Fragment
{
    String modelNum = Build.MODEL;
    String manufacturerName = Build.MANUFACTURER;
    TextView TESTING;
    String ModelNo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        TESTING = view.findViewById(R.id.TEST_WILL_BE_REMOVED_AFTER);

        ModelNo = getModelNo();

        TESTING.setText(ModelNo);
    }

    public String getModelNo()
    {
        manufacturerName = Build.MANUFACTURER;
        modelNum = Build.MODEL;

        if (modelNum.toLowerCase().startsWith(manufacturerName.toLowerCase()))
        {
            return modelNum.toUpperCase();
        }
        else
        {
            return modelNum.toUpperCase();
        }
    }

}