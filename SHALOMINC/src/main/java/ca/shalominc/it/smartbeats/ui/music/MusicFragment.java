package ca.shalominc.it.smartbeats.ui.music;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
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

public class MusicFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    TextView shalomPosition, shalomDuration;
    SeekBar shalomSeekBar;
    ImageView shalomRew, shalomPlay, shalomPause, shalomFastForward;

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;

    Animation rotateAnimation;
    ImageView shalomVinyl;
    Spinner shalomSongSpinner;

    private EditText mEditTextInput;
    private TextView mTextViewCountDown;
    private Button mButtonSet;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;


    String spinnerString;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.Songs, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shalomSongSpinner.setAdapter(adapter);
        shalomSongSpinner.setOnItemSelectedListener(this);

        mEditTextInput = view.findViewById(R.id.edit_text_input);
        mTextViewCountDown = view.findViewById(R.id.text_view_countdown);
        mButtonSet = view.findViewById(R.id.button_set);
        mButtonStartPause = view.findViewById(R.id.button_start_pause);
        mButtonReset = view.findViewById(R.id.button_reset);




         rotateAnimation();

          //mediaPlayer = MediaPlayer.create(getContext(), R.raw.music);

        /*
        runnable = new Runnable() {
            @Override
            public void run() {
                shalomSeekBar.setProgress(mediaPlayer.getCurrentPosition());

                handler.postDelayed(this, 500);

            }
        };

        int duration = mediaPlayer.getDuration();

        String sDuration = convertFormat(duration);

        shalomDuration.setText(sDuration);
        */


        shalomPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                shalomPlay.setVisibility(View.GONE);

                shalomPause.setVisibility(View.VISIBLE);


                try
                {
                    mediaPlayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/shalominc-smartbeats.appspot.com/o/BLR%20x%20Rave%20%26%20Crave%20-%20Taj.mp3?alt=media&token=7db7f980-8834-469b-9f71-bb830c1af99a");
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });

                    mediaPlayer.prepare();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }

                //mediaPlayer.start();

                //shalomSeekBar.setMax(mediaPlayer.getDuration());

                //handler.postDelayed(runnable, 0);

            }
        });


        shalomPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shalomPause.setVisibility(View.GONE);

                shalomPlay.setVisibility(View.VISIBLE);

                mediaPlayer.pause();

                //handler.removeCallbacks(runnable);

            }
        });
        /*
        shalomFastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mediaPlayer.getCurrentPosition();

                int duration = mediaPlayer.getDuration();

                if (mediaPlayer.isPlaying() && duration != currentPosition) {
                    currentPosition = currentPosition + 5000;
                    shalomPosition.setText(convertFormat(currentPosition));
                    mediaPlayer.seekTo(currentPosition);
                    Context context = getActivity();
                    Toast.makeText(context, "Song Fast Forwarded 5 seconds", Toast.LENGTH_LONG).show();
                }
            }
        });

        shalomRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                if (mediaPlayer.isPlaying() && currentPosition > 5000) {
                    currentPosition = currentPosition - 5000;
                    shalomPosition.setText(convertFormat(currentPosition));
                    mediaPlayer.seekTo(currentPosition);
                    Context context = getActivity();
                    Toast.makeText(context, "Song Rewinded by 5 seconds", Toast.LENGTH_LONG).show();

                }
            }
        });

        shalomSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
                shalomPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

         */

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                shalomPause.setVisibility(View.GONE);
                shalomPlay.setVisibility(View.VISIBLE);
                //mediaPlayer.seekTo(0);
            }
        });




        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mEditTextInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(getContext(), "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0) {
                    Toast.makeText(getContext(), "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }

                setTime(millisInput);
                mEditTextInput.setText("");
            }
        });

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }

        });

    }

    private void rotateAnimation() {

        rotateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.spinimage);
        shalomVinyl.startAnimation(rotateAnimation);

    }

    private String convertFormat(int duration) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        shalomSongSpinner = view.findViewById(R.id.shalom_music_spinner);
//        spinnerString = shalomSongSpinner.getItemAtPosition(position).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }



    //Setting the CountDown Timer
    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        resetTimer();
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();
    }


    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }

    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }

    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateWatchInterface() {
        if (mTimerRunning) {
            mEditTextInput.setVisibility(View.INVISIBLE);
            mButtonSet.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
        } else {
            mEditTextInput.setVisibility(View.VISIBLE);
            mButtonSet.setVisibility(View.VISIBLE);
            mButtonStartPause.setText("Start");

            if (mTimeLeftInMillis < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftInMillis < mStartTimeInMillis) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }



}