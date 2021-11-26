package ca.shalominc.it.smartbeats.ui.review;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    Button shalomSubmit, shalomReset, shalomRead;
    TextView shalomReadName, shalomReadPhoneNo, shalomReadEmail, shalomReadComment, shalomReadRatings;
    DocumentReference shalomDocRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    //Sets Visibility to false in this fragment for music button In menu
    //Sets Visibility to false in this fragment for power button In menu
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu)
    {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.musicBtn).setVisible(false);
        menu.findItem(R.id.lightsPwrBtn).setVisible(false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        shalomModelNo = view.findViewById(R.id.shalom_model_no);                                    // Phones Model Number TextView
        shalomRateDisp = view.findViewById(R.id.shalomRateTV);                                      // Shows Rating in TextView
        shalomSubmit = view.findViewById(R.id.submit_review_form_btn);                              // Submit Button
        shalomReset = view.findViewById(R.id.reset_review_form_btn);                                // Reset Button
        shalomPhone = view.findViewById(R.id.shalom_EditText_Phone);                                // UserPhone EditText
        shalomName = view.findViewById(R.id.shalom_EditText_PersonName);                            // UserName EditText
        shalomEmail = view.findViewById(R.id.shalom_EditText_EmailAddress);                         // UserEmail EditText
        shalomComment = view.findViewById(R.id.shalom_EditText_Comment);                            // UserComment Edittext
        shalomRateUs = view.findViewById(R.id.shalom_ratingBar);                                    // UserRating RatingBar
        shalomReadName = view.findViewById(R.id.shalomReviverTV);                                   // Shows FireBaseStorage Name TextView
        shalomReadPhoneNo = view.findViewById(R.id.shalomReviverTV2);                               // Shows FireBaseStorage PhoneNumber TextView
        shalomReadEmail = view.findViewById(R.id.shalomReviverTV3);                                 // Shows FireBaseStorage Email TextView
        shalomReadComment = view.findViewById(R.id.shalomReviverTV4);                               // Shows FireBaseStorage Comment TextView
        shalomReadRatings = view.findViewById(R.id.shalomReviverTV5);                               // Shows FireBaseStorage Ratings Textview
        shalomRead = view.findViewById(R.id.read_review_form_btn);

        FirebaseFirestore shalomData = FirebaseFirestore.getInstance();
        shalomDocRef = shalomData.collection(getString(R.string.userReview)).document(getString(R.string.sent_Review));

        //Gets Model Number
        ModelNo = getModelNo();
        shalomModelNo.setText(ModelNo);

        //retriving
        SharedPreferences shalomprefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        String Number = shalomprefs.getString(getString(R.string.phoneNUm), getString(R.string.zero));
        shalomPhone.setText(Number);

        String value1 = shalomprefs.getString(getString(R.string.userValue), getString(R.string.one));
        shalomName.setText(value1);

        String value2 = shalomprefs.getString(getString(R.string.userValue2), getString(R.string.two));
        shalomEmail.setText(value2);

        String value3 = shalomprefs.getString(getString(R.string.uservalue3), getString(R.string.three));
        shalomComment.setText(value3);

        float value4 = shalomprefs.getFloat(getString(R.string.rateReading), 4);
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

                Map<RatingBar, Object> data1 = new HashMap<>();
                data.put(getString(R.string.rateReading), rateReading);

                shalomDocRef.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, getString(R.string.sent_success));
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, getString(R.string.error), e);
                            }
                        });
            }
        });

        //Notification for review!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getString(R.string.myNoti), getString(R.string.myNoti), NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

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

                change.putString("pNumber", pNumber);
                change.putString("userValue", userValue);
                change.putString("userValue2", userValue2);
                change.putString("userValue3", userValue3);
                change.putFloat("rateReading", rateReading);
                change.apply();

                //Database Sender
                Map<String, Object> data = new HashMap<>();
                data.put("Phone Number", pNumber);
                data.put("User Name", userValue);
                data.put("Email", userValue2);
                data.put("Comments", userValue3);

                Map<RatingBar, Object> data1 = new HashMap<>();
                data.put("rateReading", rateReading);

                shalomDocRef.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Succesfully sent!");
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Error Failed", e);
                            }
                        });

                if (shalomName.getText().toString().equals("") || shalomComment.getText().toString().equals("") || shalomEmail.getText().toString().equals("")
                        || shalomPhone.length() == 0) {
                    Toast.makeText(getContext(), R.string.field_not_empty, Toast.LENGTH_SHORT).show();
                } else {

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), getString(R.string.myNoti));
                    builder.setContentTitle(getString(R.string.review_submission));
                    builder.setContentText(getString(R.string.review_message));
                    builder.setSmallIcon(R.drawable.ic_baseline_chat_24);
                    builder.setAutoCancel(true);

                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());
                    notificationManagerCompat.notify(1, builder.build());
                }
            }
        });


        //Receving data from the database
        shalomRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        shalomDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String nameRecevier = documentSnapshot.getString(getString(R.string.user_name));
                    String phoneReceiver = documentSnapshot.getString(getString(R.string.phone_number));
                    String emailRecevier = documentSnapshot.getString(getString(R.string.email));
                    String commentsRecevier = documentSnapshot.getString(getString(R.string.cmnts));
                    //    String ratingRecevier = documentSnapshot.getString("rateReading");

                    shalomReadName.setText(nameRecevier);
                    shalomReadPhoneNo.setText("" + phoneReceiver);
                    shalomReadEmail.setText(emailRecevier);
                    shalomReadComment.setText(commentsRecevier);
                    //     shalomReadRatings.setText("" + ratingRecevier);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, getString(R.string.error), e);
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
        return rateOverall = getString(R.string.rating)+starsSelected+"/"+totalStars;
    }
    public String getModelNo()
    {
        manufacturerName = Build.MANUFACTURER;
        modelNum = Build.MODEL;

        return modelNum.toUpperCase();
    }
}