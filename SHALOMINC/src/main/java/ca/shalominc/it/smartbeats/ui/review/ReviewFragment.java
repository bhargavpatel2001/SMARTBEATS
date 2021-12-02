//Bhargav Patel (N01373029) & Ripal Patel (N01354619) & Vidhi Kanhye (N01354573) & Nicholas Mohan (N01361663), Section-RNA

package ca.shalominc.it.smartbeats.ui.review;

import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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
    TextView shalomReadName, shalomReadPhoneNo, shalomReadEmail, shalomReadComment, shalomReadRatings, shalomReadModelNo;
    DocumentReference shalomDocRef;
    boolean validation = false;
    ProgressDialog PD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
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
//        shalomReadName = view.findViewById(R.id.shalomReviverTV);                                   // Shows FireBaseStorage Name TextView
//        shalomReadPhoneNo = view.findViewById(R.id.shalomReviverTV2);                               // Shows FireBaseStorage PhoneNumber TextView
//        shalomReadEmail = view.findViewById(R.id.shalomReviverTV3);                                 // Shows FireBaseStorage Email TextView
//        shalomReadComment = view.findViewById(R.id.shalomReviverTV4);                               // Shows FireBaseStorage Comment TextView
//        shalomReadRatings = view.findViewById(R.id.shalomReviverTV5);                               // Shows FireBaseStorage Ratings Textview
        shalomRead = view.findViewById(R.id.read_review_form_btn);
//        shalomReadModelNo= view.findViewById(R.id.shalomReviverTV6);

        // Setting up firestore to folder userReview file sent_Review.
        FirebaseFirestore shalomData = FirebaseFirestore.getInstance();
        shalomDocRef = shalomData.collection(getString(R.string.userReview)).document(getString(R.string.sent_Review));

        //Gets Model Number
        ModelNo = getModelNo();

        //retrieving Shared Prefrences for all user values
        SharedPreferences shalomprefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String Number = shalomprefs.getString(getString(R.string.phoneNUm), getString(R.string.zero));      shalomPhone.setText(Number);
        String value1 = shalomprefs.getString(getString(R.string.userValue), getString(R.string.one));      shalomName.setText(value1);
        String value2 = shalomprefs.getString(getString(R.string.userValue2), getString(R.string.two));     shalomEmail.setText(value2);
        String value3 = shalomprefs.getString(getString(R.string.uservalue3), getString(R.string.three));   shalomComment.setText(value3);
        float value4 = shalomprefs.getFloat(getString(R.string.rateReading), 4);  shalomRateUs.setRating(value4);


//        validation = isEmailValid();
//        if (!validation)
//        {
//            //shalomEmail.setError("ERROR");
//            shalomSubmit.setEnabled(false);
//        }
//        else {
//            shalomSubmit.setEnabled(true);
//        }

        // Submit buttons functionality
        shalomSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                pNumber = shalomPhone.getText().toString();
                userValue = shalomName.getText().toString();
                userValue2 = shalomEmail.getText().toString();
                userValue3 = shalomComment.getText().toString();
                amountOfStars = shalomRateUs.getRating();
                rateReading = amountOfStars;
                rateOverallTV = getRate();
                shalomRateDisp.setText(rateOverallTV);

                //Saving the data using Shared Prefrences
                SharedPreferences shalomprefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor change = shalomprefs.edit();

                change.putString(getString(R.string.phoneNUm), pNumber);
                change.putString(getString(R.string.userValue), userValue);
                change.putString(getString(R.string.userValue2), userValue2);
                change.putString(getString(R.string.uservalue3), userValue3);
                change.putFloat(getString(R.string.rateReading), rateReading);
                change.apply();

                //Database Sender
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
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                            {
                                NotificationChannel channel = new NotificationChannel(getString(R.string.myNoti), getString(R.string.myNoti), NotificationManager.IMPORTANCE_DEFAULT);
                                NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
                                manager.createNotificationChannel(channel);

                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), getString(R.string.myNoti));
                                builder.setContentTitle(getString(R.string.review_submission));
                                builder.setContentText(getString(R.string.review_message));
                                builder.setSmallIcon(R.drawable.ic_baseline_chat_24);
                                builder.setAutoCancel(true);

                                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());
                                notificationManagerCompat.notify(1, builder.build());
                            }
                            Log.d(TAG, getString(R.string.sent_success));
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




                PD = new ProgressDialog(getContext());
                PD.setIcon(R.drawable.ic_baseline_rate_review_24);
                PD.setTitle("Submitting Review");
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

            }
        });


        //Notification for review!


//        shalomSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (shalomName.getText().toString().equals("") || shalomComment.getText().toString().equals("") || shalomEmail.getText().toString().equals("")
//                        || shalomPhone.length() == 0) {
//                    Toast.makeText(getContext(), R.string.field_not_empty, Toast.LENGTH_SHORT).show();
//                } else {
//
//
//                }
//            }
//        });

//        //Receiving data from the database
//        shalomRead.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//             shalomDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                    if (documentSnapshot.exists()) {
//                        String nameReceiver = documentSnapshot.getString(getString(R.string.user_name));
//                        String phoneReceiver = documentSnapshot.getString(getString(R.string.phone_number));
//                        String emailReceiver = documentSnapshot.getString(getString(R.string.email));
//                        String commentsReceiver = documentSnapshot.getString(getString(R.string.cmnts));
//                        String ratingReceiver = documentSnapshot.getString(getString(R.string.rateReading));
//                        String modelNoReceiver = documentSnapshot.getString(getString(R.string.ModelNum));
//
//                        shalomReadName.setText(nameReceiver);
//                        shalomReadPhoneNo.setText(phoneReceiver);
//                        shalomReadEmail.setText(emailReceiver);
//                        shalomReadComment.setText(commentsReceiver);
//                        shalomReadRatings.setText(ratingReceiver);
//                        shalomReadModelNo.setText(modelNoReceiver);
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.d(TAG, getString(R.string.error), e);
//                }
//            });
//                }
//            });

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

    public boolean isEmailValid() {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email = shalomEmail.getText().toString().trim();
        if (email.matches(emailPattern) && email.length() > 3)
        {
           return true;
        }
        else
        {
            shalomEmail.setError("Invalid Email! Must have more then 3 words");
            return false;
        }
    }

    public String getRate()
    {
        float starsSelected = shalomRateUs.getRating();
        int  totalStars = shalomRateUs.getNumStars();
        return rateOverall = getString(R.string.rating)+starsSelected+"/"+totalStars;
    }
    public String getModelNo()
    {
        manufacturerName = Build.MANUFACTURER;
        modelNum = Build.MODEL;

        return modelNum.toUpperCase();
    }
}