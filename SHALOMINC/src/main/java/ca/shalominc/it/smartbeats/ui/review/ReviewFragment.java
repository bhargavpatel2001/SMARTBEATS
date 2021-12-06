//Bhargav Patel (N01373029) & Ripal Patel (N01354619) & Vidhi Kanhye (N01354573) & Nicholas Mohan (N01361663), Section-RNA

package ca.shalominc.it.smartbeats.ui.review;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.shalominc.it.smartbeats.Notifi;
import ca.shalominc.it.smartbeats.R;

import static android.content.ContentValues.TAG;
import static java.lang.Thread.sleep;


public class ReviewFragment extends Fragment
{

    String modelNum, manufacturerName, ModelNo;
    TextView shalomRateDispTV;
    EditText shalomNameET;
    EditText shalomPhoneET;
    EditText shalomEmailET;
    EditText shalomCommentET;
    RatingBar shalomRateUsRB;
    String pNumber,userValue, userValue2, userValue3, rateOverall, rateOverallTV;
    float  rateReading, amountOfStars;
    Button shalomSubmitBtn, shalomResetBtn;
    DocumentReference shalomDocRef;
    ProgressDialog PD;
    NotificationManagerCompat notificationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        notificationManager = NotificationManagerCompat.from(getContext());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    //Sets Visibility to false in this fragment for power button In menu
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu)
    {
        setHasOptionsMenu(true);
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.musicBtn).setVisible(false);
        menu.findItem(R.id.lightsPwrBtn).setVisible(false);
        menu.findItem(R.id.bluetoothBtn).setVisible(false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);


        shalomRateDispTV = view.findViewById(R.id.shalom_review_ratings_tv);                        // Shows Rating in TextView
        shalomSubmitBtn = view.findViewById(R.id.shalom_review_sumbit_btn);                         // Submit Button
        shalomResetBtn = view.findViewById(R.id.shalom_review_reset_btn);                           // Reset Button
        shalomPhoneET = view.findViewById(R.id.shalom_review_phone_et);                             // UserPhone EditText
        shalomNameET = view.findViewById(R.id.shalom_review_name_et);                               // UserName EditText
        shalomEmailET = view.findViewById(R.id.shalom_review_email_et);                             // UserEmail EditText
        shalomCommentET = view.findViewById(R.id.shalom_review_comment_et);                         // UserComment Edittext
        shalomRateUsRB = view.findViewById(R.id.shalom_review_stars_ratingbar);                     // UserRating RatingBar



        // Setting up firestore to folder userReview file sent_Review.
        createDataBase();

        //Gets Model Number
        ModelNo = getModelNo();

        //retrieving Shared Preferences for all user values
        saveSetPref();

        // Submit buttons functionality
        shalomSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pNumber = shalomPhoneET.getText().toString();
                userValue = shalomNameET.getText().toString();
                userValue2 = shalomEmailET.getText().toString();
                userValue3 = shalomCommentET.getText().toString();
                amountOfStars = shalomRateUsRB.getRating();
                rateReading = amountOfStars;
                rateOverallTV = getRate();
                shalomRateDispTV.setText(rateOverallTV);

                //Saving the data using Shared Prefrences
                createSetPref();

                    // Action can be performed only when email and other parameters are present.
                    PD = new ProgressDialog(getContext());
                    PD.setIcon(R.drawable.ic_baseline_rate_review_24);
                    PD.setTitle(getString(R.string.Eval_review));
                    PD.setMessage(getString(R.string.moment));
                    PD.setIndeterminate(false);
                    PD.setCancelable(false);
                    PD.show();

                    new CountDownTimer(2000, 1000) {
                        public void onTick(long millisUntilFinished)
                        {

                        }

                        public void onFinish()
                        {
                            PD.hide();
                        }
                    }.start();

                new CountDownTimer(3000, 1000) {
                    public void onTick(long millisUntilFinished)
                    {

                    }

                    public void onFinish()
                    {
                        //Database Sender
                        dataBaseSender();
                    }
                }.start();
                }

        });

        // Reset button's functionality
        shalomResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shalomPhoneET.setText("");
                shalomNameET.setText("");
                shalomEmailET.setText("");
                shalomCommentET.setText("");
                shalomRateUsRB.setRating(0);
            }
        });
    }

    public boolean isEmailTestCase(String email) {

        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
        Matcher matcher = emailPattern.matcher(email);
        if (matcher.matches())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isPhoneTestCase(String email) {

        Pattern phoneNoPattern = Pattern.compile("[0123456789]+");
        Matcher matcher = phoneNoPattern.matcher(email);
        if (matcher.matches() && email.length() == 10)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isEmailValid() {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email = shalomEmailET.getText().toString().trim();
        if (email.matches(emailPattern) && email.length() != 0)
        {
           return true;
        }
        else
        {


            shalomEmailET.setError("Invalid Email!");

            shalomEmailET.setError(getString(R.string.Invalid_email));

            return false;
        }
    }

    public boolean isNameValid() {
        String name = shalomNameET.getText().toString().trim();
        if ((name.length() != 0))
        {
            return true;
        }
        else
        {

            shalomNameET.setError("Invalid Name! Field Empty");

            shalomNameET.setError(getString(R.string.empty_field));

            return false;
        }
    }

    public boolean isCommentValid() {

        String comment = shalomCommentET.getText().toString().trim();
        if (comment.length() != 0)
        {
            return true;
        }

        else
        {

            shalomCommentET.setError("Invalid Comment! Empty Field");

            shalomCommentET.setError(getString(R.string.Invalid_comment));

            return false;
        }
    }

    public boolean isPhoneNoValid() {

        String phoneNo = shalomPhoneET.getText().toString().trim();
        if (phoneNo.length() == 10)
        {
            return true;
        }
        else
        {

            shalomPhoneET.setError("Invalid Phone Number");

            shalomPhoneET.setError(getString(R.string.Invalid_phone_number));

            return false;
        }
    }

    public String getRate() {
        float starsSelected = shalomRateUsRB.getRating();
        int  totalStars = shalomRateUsRB.getNumStars();
        return rateOverall = getString(R.string.rating)+starsSelected+"/"+totalStars;
    }

    public String getModelNo() {
        manufacturerName = Build.MANUFACTURER;
        modelNum = Build.MODEL;

        return modelNum.toUpperCase();
    }

    public void saveSetPref(){
        SharedPreferences shalomprefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String Number = shalomprefs.getString(getString(R.string.phoneNUm),"4169671111");      shalomPhoneET.setText(Number);
        String value1 = shalomprefs.getString(getString(R.string.userValue),"Alice");      shalomNameET.setText(value1);
        String value2 = shalomprefs.getString(getString(R.string.userValue2), "bob@alice.ca");     shalomEmailET.setText(value2);
        String value3 = shalomprefs.getString(getString(R.string.uservalue3), "App Rocks!");   shalomCommentET.setText(value3);
        float value4 = shalomprefs.getFloat(getString(R.string.rateReading), 4);  shalomRateUsRB.setRating(value4);
    }

    public void createSetPref(){
        SharedPreferences shalomprefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor change = shalomprefs.edit();

        change.putString(getString(R.string.phoneNUm), pNumber);
        change.putString(getString(R.string.userValue), userValue);
        change.putString(getString(R.string.userValue2), userValue2);
        change.putString(getString(R.string.uservalue3), userValue3);
        change.putFloat(getString(R.string.rateReading), rateReading);
        change.apply();
    }

    public void createDataBase(){
        FirebaseFirestore shalomData = FirebaseFirestore.getInstance();
        shalomDocRef = shalomData.collection(getString(R.string.userReview)).document(getString(R.string.sent_Review));
    }

    public void dataBaseSender(){
        Map<String, Object> data = new HashMap<>();
        data.put(getString(R.string.phone_number), pNumber);
        data.put(getString(R.string.user_name), userValue);
        data.put(getString(R.string.email), userValue2);
        data.put(getString(R.string.cmnts), userValue3);
        data.put(getString(R.string.rateReading), rateOverallTV);
        data.put(getString(R.string.ModelNum),ModelNo);

        shalomDocRef.set(data).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                    createNotificaion();
            }
        })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Log.d(TAG, getString(R.string.error), e);
                    }
                });
    }

    public void createNotificaion() {
        if (ReviewFragment.this.isEmailValid() && ReviewFragment.this.isNameValid() && ReviewFragment.this.isPhoneNoValid() && ReviewFragment.this.isCommentValid()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), Notifi.CHANNEL_1_ID);
                builder.setSmallIcon(R.drawable.ic_baseline_chat_24);
                builder.setContentTitle(getString(R.string.review_submission));
                builder.setContentText(getString(R.string.review_message));
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());
                notificationManagerCompat.notify(1, builder.build());
            }
        }
    }
}