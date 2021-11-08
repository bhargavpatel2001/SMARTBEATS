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
import android.widget.RatingBar;
import android.widget.TextView;

import ca.shalominc.it.smartbeats.R;


public class ReviewFragment extends Fragment
{
    String modelNum = Build.MODEL, manufacturerName = Build.MANUFACTURER, ModelNo;
    TextView shalomModelNo,shalomRateDisp;
    EditText shalomName, shalomPhone, shalomEmail, shalomComment;
    RatingBar shalomRateUs;
    String pNumber,userValue, userValue2, userValue3, rateOverall, rateOverallTV;
    float  rateReading, amountOfStars;
    Button shalomSubmit;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shalomModelNo = view.findViewById(R.id.shalom_model_no);
        shalomRateDisp = view.findViewById(R.id.shalomRateTV);
        shalomSubmit = view.findViewById(R.id.submit_review_form_btn);
        shalomPhone = view.findViewById(R.id.shalom_EditText_Phone);
        shalomName = view.findViewById(R.id.shalom_EditText_PersonName);
        shalomEmail = view.findViewById(R.id.shalom_EditText_EmailAddress);
        shalomComment = view.findViewById(R.id.shalom_EditText_Comment);
        shalomRateUs = view.findViewById(R.id.shalom_ratingBar);

        //Gets Model Number
        ModelNo = getModelNo();
        shalomModelNo.setText(ModelNo);

        //retriving
        SharedPreferences shalomprefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        String Number = shalomprefs.getString("pNumber","0");
        shalomPhone.setText(Number);

        String value1 = shalomprefs.getString("userValue","1");
        shalomName.setText(value1);

        String value2 = shalomprefs.getString("userValue2","2");
        shalomEmail.setText(value2);

        String value3 = shalomprefs.getString("userValue3","3");
        shalomComment.setText(value3);

        float value4 = shalomprefs.getFloat("rateReading",4);
        shalomRateUs.setRating(value4);

        // Submit buttons functionality
        shalomSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pNumber = shalomPhone.getText().toString();
                userValue = shalomName.getText().toString();
                userValue2 = shalomEmail.getText().toString();
                userValue3 = shalomComment.getText().toString();
                amountOfStars = shalomRateUs.getRating();
                rateReading = amountOfStars;
                rateOverallTV = getRate();
                shalomRateDisp.setText(rateOverallTV);



                //Saving the data
                SharedPreferences shalomprefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor change = shalomprefs.edit();

                change.putString("pNumber",pNumber);
                change.putString("userValue",userValue);
                change.putString("userValue2",userValue2);
                change.putString("userValue3",userValue3);
                change.putFloat("rateReading",rateReading);
                change.apply();

            }
        });
    }

    public String getRate()
    {
        float starsSelected = shalomRateUs.getRating();
        int  totalStars = shalomRateUs.getNumStars();
        return rateOverall = "Rating: "+starsSelected+"/"+totalStars;
    }
    public String getModelNo()
    {
        manufacturerName = Build.MANUFACTURER;
        modelNum = Build.MODEL;

        return modelNum.toUpperCase();
    }

}