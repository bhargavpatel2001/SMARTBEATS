//Bhargav Patel (N01373029) & Ripal Patel (N01354619) & Vidhi Kanhye (N01354573) & Nicholas Mohan (N01361663), Section-RNA

package ca.shalominc.it.smartbeats.ui.review;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    TextView shalomRateDisp;
    EditText shalomName;
    EditText shalomPhone;
    EditText shalomEmail;
    EditText shalomComment;
    RatingBar shalomRateUs;
    String pNumber,userValue, userValue2, userValue3, rateOverall, rateOverallTV;
    float  rateReading, amountOfStars;
    Button shalomSubmit, shalomReset, shalomRead;
    DocumentReference shalomDocRef;
    boolean validation = false;
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

        shalomRateDisp = view.findViewById(R.id.shalomRateTV);                                      // Shows Rating in TextView
        shalomSubmit = view.findViewById(R.id.submit_review_form_btn);                              // Submit Button
        shalomReset = view.findViewById(R.id.reset_review_form_btn);                                // Reset Button
        shalomPhone = view.findViewById(R.id.shalom_EditText_Phone);                                // UserPhone EditText
        shalomName = view.findViewById(R.id.shalom_EditText_PersonName);                            // UserName EditText
        shalomEmail = view.findViewById(R.id.shalom_EditText_EmailAddress);                         // UserEmail EditText
        shalomComment = view.findViewById(R.id.shalom_EditText_Comment);                            // UserComment Edittext
        shalomRateUs = view.findViewById(R.id.shalom_ratingBar);                                    // UserRating RatingBar
        //shalomRead = view.findViewById(R.id.read_review_form_btn);


        // Setting up firestore to folder userReview file sent_Review.
        createDataBase();

        //Gets Model Number
        ModelNo = getModelNo();

        //retrieving Shared Prefrences for all user values
        saveSetPref();

        // Submit buttons functionality
        shalomSubmit.setOnClickListener(new View.OnClickListener()
        {
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

                //Saving the data using Shared Prefrences
                createSetPref();

                    // Action can be performed only when email and other parameters are present.

                    PD = new ProgressDialog(getContext());
                    PD.setIcon(R.drawable.ic_baseline_rate_review_24);
                    PD.setTitle("Evaluating Your Review");
                    PD.setMessage("Give us a few moments");
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
        String email = shalomEmail.getText().toString().trim();
        if (email.matches(emailPattern) && email.length() != 0)
        {
           return true;
        }
        else
        {

            shalomEmail.setError("Invalid Email!");
            return false;
        }
    }

    public boolean isNameValid() {
        String name = shalomName.getText().toString().trim();
        if ((name.length() != 0))
        {
            return true;
        }
        else
        {
            shalomName.setError("Invalid Name! Field Empty");
            return false;
        }
    }

    public boolean isCommentValid() {

        String comment = shalomComment.getText().toString().trim();
        if (comment.length() != 0)
        {
            return true;
        }

        else
        {
            shalomComment.setError("Invalid Comment! Empty Field");
            return false;
        }
    }

    public boolean isPhoneNoValid() {

        String phoneNo = shalomPhone.getText().toString().trim();
        if (phoneNo.length() == 10)
        {
            return true;
        }
        else
        {
            shalomPhone.setError("Invalid Phone Number");
            return false;
        }
    }

    public String getRate() {
        float starsSelected = shalomRateUs.getRating();
        int  totalStars = shalomRateUs.getNumStars();
        return rateOverall = getString(R.string.rating)+starsSelected+"/"+totalStars;
    }

    public String getModelNo() {
        manufacturerName = Build.MANUFACTURER;
        modelNum = Build.MODEL;

        return modelNum.toUpperCase();
    }

    public void saveSetPref(){
        SharedPreferences shalomprefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String Number = shalomprefs.getString(getString(R.string.phoneNUm),"4169671111");      shalomPhone.setText(Number);
        String value1 = shalomprefs.getString(getString(R.string.userValue),"Alice");      shalomName.setText(value1);
        String value2 = shalomprefs.getString(getString(R.string.userValue2), "bob@alice.ca");     shalomEmail.setText(value2);
        String value3 = shalomprefs.getString(getString(R.string.uservalue3), "App Rocks!");   shalomComment.setText(value3);
        float value4 = shalomprefs.getFloat(getString(R.string.rateReading), 4);  shalomRateUs.setRating(value4);
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