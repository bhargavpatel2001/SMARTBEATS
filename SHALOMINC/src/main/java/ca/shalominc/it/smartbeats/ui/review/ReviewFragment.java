package ca.shalominc.it.smartbeats.ui.review;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ca.shalominc.it.smartbeats.MainActivity;
import ca.shalominc.it.smartbeats.R;


public class ReviewFragment extends Fragment
{
    String modelNum = Build.MODEL;
    String manufacturerName = Build.MANUFACTURER;
    TextView TESTING;
    String ModelNo;
    Button shalomSubmit;
    EditText shalomName, shalomPhone, shalomEmail, shalomComment;
    int num;
    String userValue, userValue2, userValue3;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TESTING = view.findViewById(R.id.TEST_WILL_BE_REMOVED_AFTER);
        shalomSubmit = view.findViewById(R.id.submit_review_form_btn);
        shalomPhone = view.findViewById(R.id.shalom_EditText_Phone);
        shalomName = view.findViewById(R.id.shalom_EditText_PersonName);
        shalomEmail = view.findViewById(R.id.shalom_EditText_EmailAddress);
        shalomComment = view.findViewById(R.id.shalom_EditText_Comment);

        //
        ModelNo = getModelNo();
        TESTING.setText(ModelNo);

        //retriving
        SharedPreferences shalomprefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        int num1 = shalomprefs.getInt("num",0);
        shalomPhone.setText(""+ num1);

        String value1 = shalomprefs.getString("userValue","1");
        shalomName.setText(value1);

        String value2 = shalomprefs.getString("userValue2","2");
        shalomEmail.setText(value2);

        String value3 = shalomprefs.getString("userValue3","3");
        shalomComment.setText(value3);

        // Submit buttons functionality
        shalomSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                num = Integer.parseInt(shalomPhone.getText().toString());
                userValue = shalomName.getText().toString();
                userValue2 = shalomEmail.getText().toString();
                userValue3 = shalomComment.getText().toString();

                //Saving the data
                SharedPreferences shalomprefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor change = shalomprefs.edit();

                change.putInt("num",num);
                change.putString("userValue",userValue);
                change.putString("userValue2",userValue2);
                change.putString("userValue3",userValue3);
                change.apply();

            }
        });
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