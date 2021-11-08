package ca.shalominc.it.smartbeats.ui.music;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ca.shalominc.it.smartbeats.R;

import static android.content.ContentValues.TAG;
import static java.sql.Types.NULL;

public class MusicFragment extends Fragment
{

    TextView shalomPosition, shalomDuration;
    SeekBar shalomSeekBar;
    ImageView shalomRew, shalomPlay, shalomPause, shalomFastForward;

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;

    Animation rotateAnimation;
    ImageView shalomVinyl;
    Spinner shalomSongSpinner;
    String DBSongUrl;
    String DBSongUrlChoice;
    String spinnerString;


    private EditText shalomEditTextInput;
    private TextView shalomTextViewCountDown;
    private Button shalomButtonSet;
    private Button shalomButtonStartPause;
    private Button shalomButtonReset;

    private CountDownTimer shalomCountDownTimer;

    private boolean shalomTimerRunning;

    private long shalomStartTimeInMillis;
    private long shalomTimeLeftInMillis;
    private long shalomEndTime;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_music, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mediaPlayer = new MediaPlayer();
        shalomPosition = view.findViewById(R.id.shalom_timer);
        shalomDuration = view.findViewById(R.id.shalom_timer_duration);
        shalomSeekBar = view.findViewById(R.id.shalom_seekbar);
        shalomRew = view.findViewById(R.id.shalom_rewind);
        shalomPlay = view.findViewById(R.id.bt_play);
        shalomPause = view.findViewById(R.id.bt_pause);
        shalomFastForward = view.findViewById(R.id.bt_ff);
        shalomVinyl = view.findViewById(R.id.shalom_IV);
        shalomSongSpinner = view.findViewById(R.id.shalom_music_spinner);


        ArrayAdapter<CharSequence> sAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Songs, android.R.layout.simple_spinner_item);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shalomSongSpinner.setAdapter(sAdapter);
        shalomSongSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l)
            {
                spinnerString = shalomSongSpinner.getItemAtPosition(position).toString();
                Context context = getContext();
                switch (spinnerString)
                {
                    case "Select Your Song":

                        Toast.makeText(context, "Select a Song Below", Toast.LENGTH_LONG).show();

                        break;

                    case "ATC - All Around The World":
                        Toast.makeText(context, "ATC - All Around The World", Toast.LENGTH_LONG).show();
                        DBSongUrlChoice = "https://firebasestorage.googleapis.com/v0/b/shalominc-smartbeats.appspot.com/o/ATC%20-%20All%20Around%20The%20World%20(la%20la%20la%20la%20la%20la%20la%20la).mp3?alt=media&token=a145dfdc-75c6-4da7-a5a0-52ce1fa8c4e6";

                        mediaPlayer.reset();

                        try
                        {
                            mediaPlayer.setDataSource(DBSongUrlChoice);
                            mediaPlayer.prepare();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                        break;

                    case "Dynoro - In My Mind":

                        Toast.makeText(context, "Dynoro - In My Mind", Toast.LENGTH_LONG).show();
                        DBSongUrlChoice = "https://firebasestorage.googleapis.com/v0/b/shalominc-smartbeats.appspot.com/o/Dynoro%20%C2%B7%20Gigi%20D'Agostino%20-%20In%20My%20Mind.mp3?alt=media&token=b5ea0fa4-e46f-4a8a-8cad-87a4c71f32f2";
                        mediaPlayer.reset();

                        try
                        {
                            mediaPlayer.setDataSource(DBSongUrlChoice);
                            mediaPlayer.prepare();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                        break;

                    case "MEDUZA - Lose Control":
                        Toast.makeText(context, "MEDUZA - Lose Control", Toast.LENGTH_LONG).show();
                        DBSongUrlChoice = "https://firebasestorage.googleapis.com/v0/b/shalominc-smartbeats.appspot.com/o/MEDUZA%20%C2%B7%20Becky%20Hill%20%C2%B7%20Goodboys%20-%20Lose%20Control.mp3?alt=media&token=2bc00ee9-3eef-4961-a691-40b75d817451";
                        mediaPlayer.reset();

                        try
                        {
                            mediaPlayer.setDataSource(DBSongUrlChoice);
                            mediaPlayer.prepare();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                        break;

                    case "Regard - Ride It":
                        Toast.makeText(context, "Regard - Ride It", Toast.LENGTH_LONG).show();
                        DBSongUrlChoice = "https://firebasestorage.googleapis.com/v0/b/shalominc-smartbeats.appspot.com/o/Regard%20-%20Ride%20It.mp3?alt=media&token=5d9e3f2f-a952-484e-8314-580293780e12";
                        mediaPlayer.reset();

                        try
                        {
                            mediaPlayer.setDataSource(DBSongUrlChoice);
                            mediaPlayer.prepare();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                        break;

                    case "SAINt Jhn - Roses":
                        Toast.makeText(context, "SAINt Jhn - Roses", Toast.LENGTH_LONG).show();
                        DBSongUrlChoice = "https://firebasestorage.googleapis.com/v0/b/shalominc-smartbeats.appspot.com/o/SAINt%20Jhn%20%26%20J.%20Balvin%20-%20%E2%80%9CRoses%E2%80%9D%20%5B(Imanbek%20Remix)%5D%20(Latino%20Gang).mp3?alt=media&token=0b97c748-f23e-4de4-bd68-513124093fdb";
                        mediaPlayer.reset();

                        try
                        {
                            mediaPlayer.setDataSource(DBSongUrlChoice);
                            mediaPlayer.prepare();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });




        //Setting Attributes
        mediaPlayer.setAudioAttributes
                (new AudioAttributes
                        .Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                );

         rotateAnimation();

          //mediaPlayer = MediaPlayer.create(getContext(), R.raw.music);


        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                shalomSeekBar.setProgress(mediaPlayer.getCurrentPosition());

                handler.postDelayed(this, 500);

            }
        };
// --------------------------------------------------*-
        int duration = mediaPlayer.getDuration();

        String sDuration = convertFormat(duration);

        shalomDuration.setText(sDuration);


   // _______________________Default Track_________________________________________\\

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        DBSongUrl = "https://firebasestorage.googleapis.com/v0/b/shalominc-smartbeats.appspot.com/o/BLR%20x%20Rave%20%26%20Crave%20-%20Taj.mp3?alt=media&token=7db7f980-8834-469b-9f71-bb830c1af99a";

        try
        {
            mediaPlayer.setDataSource(DBSongUrl);
            mediaPlayer.prepare();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        shalomPlay.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                shalomPlay.setVisibility(View.GONE);
                shalomPause.setVisibility(View.VISIBLE);

                mediaPlayer.start();

                    shalomSeekBar.setMax(mediaPlayer.getDuration());

                    handler.postDelayed(runnable, 0);
            }

        });


        shalomPause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                shalomPause.setVisibility(View.GONE);

                shalomPlay.setVisibility(View.VISIBLE);

                mediaPlayer.pause();

                handler.removeCallbacks(runnable);

            }
        });

        shalomFastForward.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                int currentPosition = mediaPlayer.getCurrentPosition();

                int duration = mediaPlayer.getDuration();

                if (mediaPlayer.isPlaying() && duration != currentPosition) {
                    currentPosition = currentPosition + 5000;
                    shalomPosition.setText(convertFormat(currentPosition));
                    mediaPlayer.seekTo(currentPosition);

                }
            }
        });

        shalomRew.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                if (mediaPlayer.isPlaying() && currentPosition > 5000) {
                    currentPosition = currentPosition - 5000;
                    shalomPosition.setText(convertFormat(currentPosition));
                    mediaPlayer.seekTo(currentPosition);

                }
            }
        });


        shalomSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
                shalomPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });





        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                shalomPause.setVisibility(View.GONE);
                shalomPlay.setVisibility(View.VISIBLE);
                //mediaPlayer.seekTo(0);
            }
        });


//        ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


        shalomEditTextInput = view.findViewById(R.id.shalom_edit_text_input);
        shalomTextViewCountDown = view.findViewById(R.id.shalom_text_view_countdown);
        shalomButtonSet = view.findViewById(R.id.shalom_button_set);
        shalomButtonStartPause = view.findViewById(R.id.shalom_button_start_pause);
        shalomButtonReset = view.findViewById(R.id.shalom_button_reset);

        shalomButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = shalomEditTextInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(getContext(),R.string.field_not_empty, Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0) {
                    Toast.makeText(getContext(),R.string.enter_positive_number, Toast.LENGTH_SHORT).show();
                    return;
                }

                setTime(millisInput);
                shalomEditTextInput.setText("");
            }
        });

        shalomButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shalomTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        shalomButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }

        });



    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void rotateAnimation()
    {

        rotateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.spinimage);
        shalomVinyl.startAnimation(rotateAnimation);

    }


    private String convertFormat(int duration)
    {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

   //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //Setting the CountDown Timer
    private void setTime(long milliseconds) {
        shalomStartTimeInMillis = milliseconds;
        resetTimer();
    }

    private void startTimer() {
        shalomEndTime = System.currentTimeMillis() + shalomTimeLeftInMillis;
        shalomCountDownTimer = new CountDownTimer(shalomTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                shalomTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                shalomTimerRunning = false;
                updateWatchInterface();
            }
        }.start();

        shalomTimerRunning = true;
        updateWatchInterface();
    }

    private void pauseTimer() {
        shalomCountDownTimer.cancel();
        shalomTimerRunning = false;
        updateWatchInterface();
    }

    private void resetTimer() {
        shalomTimeLeftInMillis = shalomStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }

    private void updateCountDownText() {
        int hours = (int) (shalomTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((shalomTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (shalomTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        shalomTextViewCountDown.setText(timeLeftFormatted);
    }

    @SuppressLint("SetTextI18n")
    private void updateWatchInterface() {
        if (shalomTimerRunning) {
            shalomEditTextInput.setVisibility(View.INVISIBLE);
            shalomButtonSet.setVisibility(View.INVISIBLE);
            shalomButtonReset.setVisibility(View.INVISIBLE);
            shalomButtonStartPause.setText(R.string.pause);
        } else {
            shalomEditTextInput.setVisibility(View.VISIBLE);
            shalomButtonSet.setVisibility(View.VISIBLE);
            shalomButtonStartPause.setText(R.string.start);

            if (shalomTimeLeftInMillis < 1000) {
                shalomButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                shalomButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (shalomTimeLeftInMillis < shalomStartTimeInMillis) {
                shalomButtonReset.setVisibility(View.VISIBLE);
            } else {
                shalomButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }


}