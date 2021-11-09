package ca.shalominc.it.smartbeats.ui.review;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import ca.shalominc.it.smartbeats.R;

import static android.content.ContentValues.TAG;


public class ReviewFragment extends Fragment
{
    String modelNum = Build.MODEL, manufacturerName = Build.MANUFACTURER, ModelNo;
    TextView shalomModelNo,shalomRateDisp;
    EditText shalomName, shalomPhone, shalomEmail, shalomComment;
    RatingBar shalomRateUs;
    String pNumber,userValue, userValue2, userValue3, rateOverall, rateOverallTV;
    float  rateReading, amountOfStars;
    Button shalomSubmit, shalomReset;




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
        shalomReset = view.findViewById(R.id.reset_review_form_btn);

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

                //Database Sender
                FirebaseFirestore shalomData = FirebaseFirestore.getInstance();
                Map<String, Object> data = new HashMap<>();
                data.put("Phone Number",pNumber);
                data.put("User Name",userValue);
                data.put("Email",userValue2);;
                data.put("Comments",userValue3);

                Map<RatingBar, Object> data1 = new HashMap<>();
                data.put("rateReading",rateReading);

                shalomData.collection("user_review").document("reviewSent").set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Succesfully sent!");
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Error Failed",e);
                            }
                        });
            }
        });

        // Reset button's functionality
        shalomReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shalomPhone.setText("");
                shalomName.setText("");
                shalomEmail.setText("");
                shalomComment.setText("");
                shalomRateUs.setRating(0);


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