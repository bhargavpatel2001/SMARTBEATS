//Bhargav Patel (N01373029) & Ripal Patel (N01354619) & Vidhi Kanhye (N01354573) & Nicholas Mohan (N01361663), Section-RNA

package ca.shalominc.it.smartbeats.ui.music;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ca.shalominc.it.smartbeats.R;

// Refactored the whole fragment into methods/functionality to make it more readable
public class MusicFragment extends Fragment
{
    //Media Player
    MediaPlayer shalomMediaPlayer;
    TextView shalomMediaStartTimerTV, shalomMediaEndTimerTV;
    SeekBar shalomMediaSeekBar;
    ImageView shalomRewindIvBtn, shalomPlayIvBtn, shalomPauseIvBtn, shalomFastForwardIvBtn;
    ImageView shalomMusicDiscIV;
    Animation rotateAnimation;
    Handler shalomHandler = new Handler();
    Runnable shalomRunnable;

    //Audio Manager
    AudioManager ShalomAM;
    SeekBar shalomVolumeSeekbar;

    //Spinner for user to select songs.
    Spinner shalomSongSpinner;
    String spinnerString;

    //Song selector
    String DBSongUrlChoice, DBSongName, DBSongExtension, TextChanger;



    // Timer countdown till the song auto stops.
    EditText shalomEditTextInput;
    TextView shalomTextViewCountDown;
    Button shalomButtonSet;
    Button shalomButtonStartPause;
    Button shalomButtonReset;
    CountDownTimer shalomCountDownTimer;
    boolean shalomTimerRunning;
    long shalomStartTimeInMillis;
    long shalomTimeLeftInMillis;
    long shalomEndTime;
    private DownloadManager mgr = null;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        if (menu != null)
        {
            setHasOptionsMenu(true);
            menu.findItem(R.id.lightsPwrBtn).setVisible(false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


        //Media Player and the options
        shalomMediaPlayer = new MediaPlayer();                                                            // Creates New MediaPlayer
        shalomMediaStartTimerTV = view.findViewById(R.id.shalom_music_startimer_tv);                                      // Increment Counter TextView
        shalomMediaEndTimerTV = view.findViewById(R.id.shalom_music_endtimer_tv);                             // Decrement Counter TextView
        shalomMediaSeekBar = view.findViewById(R.id.shalom_music_player_seekbar);                                     // Music Seekbar
        shalomRewindIvBtn = view.findViewById(R.id.shalom_music_rewind_ivbtn);                                          // Rewind Button ImageView
        shalomPlayIvBtn = view.findViewById(R.id.shalom_music_play_ivbtn);                                               // Play Button ImageView
        shalomPauseIvBtn = view.findViewById(R.id.shalom_music_pause_ivbtn);                                             // Pause Button ImageView
        shalomFastForwardIvBtn = view.findViewById(R.id.shalom_music_fastforward_ivbtn);                                          // Fast Forward Button ImageView
        shalomMusicDiscIV = view.findViewById(R.id.shalom_music_disc_iv);                                            // Disc Display Rotating ImageView

        //Adjust Volumes.
        shalomVolumeSeekbar = view.findViewById(R.id.shalom_music_volume_seekbar);                                       // Volume Seekbar
        ShalomAM = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);            // Audio Manager
        int maxVol = ShalomAM.getStreamMaxVolume(ShalomAM.STREAM_MUSIC);
        int curVol = ShalomAM.getStreamVolume(ShalomAM.STREAM_MUSIC);
        shalomVolumeSeekbar.setMax(maxVol);
        shalomVolumeSeekbar.setProgress(curVol);

        // Timer Count down
        shalomEditTextInput = view.findViewById(R.id.shalom_music_minutes_et);                       // User Input for EditText
        shalomTextViewCountDown = view.findViewById(R.id.shalom_muic_countdown_tv);               // Decrementing TextView
        shalomButtonSet = view.findViewById(R.id.shalom_music_set_btn);                                // Set Button
        shalomButtonStartPause = view.findViewById(R.id.shalom_music_start_btn);                 // Pause Button
        shalomButtonReset = view.findViewById(R.id.shalom_music_reset_btn);                            // Reset Button

        //Spinner for user to select their songs
        shalomSongSpinner = view.findViewById(R.id.shalom_music_songs_spinner);                           // Music Selector Spinner
        ArrayAdapter<CharSequence> sAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Songs, android.R.layout.simple_spinner_item);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shalomSongSpinner.setAdapter(sAdapter);

        // Setting the starting value for the timer to 00:00
        SharedPreferences shalomprefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String decrementTV = shalomprefs.getString("TimeChanger", "00:00");
        shalomTextViewCountDown.setText(decrementTV);

        // Counter for the start text and end text of the music duration.
        int duration = shalomMediaPlayer.getDuration();
        String sDuration = convertFormat(duration);
        shalomMediaEndTimerTV.setText(sDuration);

        // Calling function setUpMediaPlayer();
        setUpMediaPlayer();

        //Setting button to invisible
        shalomButtonStartPause.setVisibility(View.INVISIBLE);

        // Spinner Item selector
        shalomSongSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l)
            {
                createMediaPlayerCases(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Volume changer for music
        shalomVolumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ShalomAM.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Play button click listener
        shalomPlayIvBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                shalomPlayIvBtn.setVisibility(View.GONE);
                shalomPauseIvBtn.setVisibility(View.VISIBLE);

                shalomMediaPlayer.start();

                // Calling function rotateAnimation();
                startRotateAnimation();

                shalomMediaSeekBar.setMax(shalomMediaPlayer.getDuration());
                shalomHandler.postDelayed(shalomRunnable, 0);
            }

        });

        // Music Pause button click listener
        shalomPauseIvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shalomPauseIvBtn.setVisibility(View.GONE);
                shalomPlayIvBtn.setVisibility(View.VISIBLE);

                shalomMediaPlayer.pause();

                // Calling function endRotateAnimation();
                endRotateAnimation();

                shalomHandler.removeCallbacks(shalomRunnable);
            }
        });

        //FastForward button click Listener
        shalomFastForwardIvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = shalomMediaPlayer.getCurrentPosition();

                int duration = shalomMediaPlayer.getDuration();

                if (shalomMediaPlayer.isPlaying() && duration != currentPosition) {
                    currentPosition = currentPosition + 5000;
                    shalomMediaStartTimerTV.setText(convertFormat(currentPosition));
                    shalomMediaPlayer.seekTo(currentPosition);

                }

            }
        });

        //Rewind button click listener
        shalomRewindIvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = shalomMediaPlayer.getCurrentPosition();
                if (shalomMediaPlayer.isPlaying() && currentPosition > 5000) {
                    currentPosition = currentPosition - 5000;
                    shalomMediaStartTimerTV.setText(convertFormat(currentPosition));
                    shalomMediaPlayer.seekTo(currentPosition);

                }
            }
        });

        //SeekBar change listener
        shalomMediaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    shalomMediaPlayer.seekTo(progress);
                }
                shalomMediaStartTimerTV.setText(convertFormat(shalomMediaPlayer.getCurrentPosition()));
                shalomMediaEndTimerTV.setText(convertFormat(shalomMediaPlayer.getDuration()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }});

        //Pause and play visibility
        shalomMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                shalomPauseIvBtn.setVisibility(View.GONE);
                shalomPlayIvBtn.setVisibility(View.VISIBLE);
            }
        });

        //Timer Set Button
        shalomButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = shalomEditTextInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(getContext(), R.string.field_not_empty, Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInput = Long.parseLong(input) * 60000;  // Changes user Input to Minutes

                if (millisInput == 0) {
                    Toast.makeText(getContext(), R.string.enter_positive_number, Toast.LENGTH_SHORT).show();
                    return;
                }

                setTime(millisInput);
                shalomEditTextInput.setText("");
            }
        });

        //Timer Start Pause Button
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

        //Timer reset Button
        shalomButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }

        });
    }

    // Async Task for Downloading music to Downloads folder
    private class DownloadMP3 extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Log.w(getString(R.string.mp3_pre_download),getString(R.string.one));
        }

        @Override
        protected String doInBackground(String... urlParams)
        {
            try
            {
                Uri uri=Uri.parse(DBSongUrlChoice);
                mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                        mgr.enqueue(new DownloadManager.Request(uri)
                                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                        DownloadManager.Request.NETWORK_MOBILE)
                                .setAllowedOverRoaming(false)
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                                        DBSongName+DBSongExtension));
            }
            catch (Exception ignored)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            new CountDownTimer(2000, 1000) {
                public void onTick(long millisUntilFinished)
                {

                }

                public void onFinish()
                {
                    Log.w(getString(R.string.mp3_downloaded),getString(R.string.two) + s);
                }
            }.start();
        }
    }

    //Setting the CountDown Timer
    private void setTime(long milliseconds) {
        shalomStartTimeInMillis = milliseconds;
        resetTimer();
    }

    // Starting the CountDown Timer
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

    // Pausing the CountDown Timer
    private void pauseTimer() {
        shalomCountDownTimer.cancel();
        shalomTimerRunning = false;
        updateWatchInterface();
    }

    // Resetting the CountDown Timer
    private void resetTimer() {
        shalomTimeLeftInMillis = shalomStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }

    // CountDown Updates and decrements the timer
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
        SharedPreferences shalomprefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor change = shalomprefs.edit();
        change.putString("TimeChanger", timeLeftFormatted);
        change.apply();
    }

    // Visibility settings for buttons
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

    //sets up the Mediaplayer and the seekbar and provides a default track
    public void setUpMediaPlayer(){

        //Setting up Media Player
        shalomMediaPlayer.setAudioAttributes
                (new AudioAttributes
                        .Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                );

        //Seekbar progress Duration check for the song.
        shalomRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                shalomMediaSeekBar.setProgress(shalomMediaPlayer.getCurrentPosition());

                shalomHandler.postDelayed(this, 500);

            }
        };

        //Setting Audio Stream / Default Track
        shalomMediaPlayer = MediaPlayer.create(getContext(),R.raw.taj);
    }

    // Creates cases to setup songs for each spinner position
    public void createMediaPlayerCases(int position){
        spinnerString = shalomSongSpinner.getItemAtPosition(position).toString();
        Context context = getContext();

        switch (spinnerString)
        {
            case "Select Your Song":
                Toast.makeText(context,R.string.select_song_below, Toast.LENGTH_LONG).show();

                break;

            case "ATC - All Around The World":
                Toast.makeText(context,R.string.atc, Toast.LENGTH_LONG).show();

                DBSongUrlChoice = "https://firebasestorage.googleapis.com/v0/b/shalominc-smartbeats.appspot.com/o/ATC%20-%20All%20Around%20The%20World.mp3?alt=media&token=41077a29-12e9-4371-b8a0-af1c7179a0d4";
                DBSongName = getString(R.string.atc);
                DBSongExtension = getString(R.string.mp3);
                TextChanger = getString(R.string.downloading_atc);

                downloadingSong();
                databaseToMediaPlayer();

                break;

            case "Dynoro - In My Mind":

                Toast.makeText(context,R.string.dynoro, Toast.LENGTH_LONG).show();

                DBSongUrlChoice = "https://firebasestorage.googleapis.com/v0/b/shalominc-smartbeats.appspot.com/o/Dynoro%20-%20In%20My%20Mind.mp3?alt=media&token=8600afad-31fb-4f7f-97b4-92e2968ff851";
                DBSongName = getString(R.string.dynoro);
                DBSongExtension = getString(R.string.mp3);
                TextChanger = getString(R.string.downloading_dynoro);

                downloadingSong();
                databaseToMediaPlayer();

                break;

            case "MEDUZA - Lose Control":
                Toast.makeText(context,R.string.meduza, Toast.LENGTH_LONG).show();

                DBSongUrlChoice = "https://firebasestorage.googleapis.com/v0/b/shalominc-smartbeats.appspot.com/o/MEDUZA%20-%20Lose%20Control.mp3?alt=media&token=92253d10-47c6-455b-897b-14bec7e1b923";
                DBSongName = getString(R.string.meduza);
                DBSongExtension = getString(R.string.mp3);
                TextChanger = getString(R.string.downloading_meduza);

                downloadingSong();
                databaseToMediaPlayer();

                break;

            case "Regard - Ride It":
                Toast.makeText(context,R.string.rideIt, Toast.LENGTH_LONG).show();

                DBSongUrlChoice = "https://firebasestorage.googleapis.com/v0/b/shalominc-smartbeats.appspot.com/o/Regard%20-%20Ride%20It.mp3?alt=media&token=d52d0d1e-1152-4b64-9cfc-0def83505f00";
                DBSongName = getString(R.string.rideIt);
                DBSongExtension = getString(R.string.mp3);
                TextChanger = getString(R.string.downloading_riedIt);

                downloadingSong();
                databaseToMediaPlayer();

                break;

            case "SAINt Jhn - Roses":
                Toast.makeText(context,R.string.roses, Toast.LENGTH_LONG).show();

                DBSongUrlChoice = "https://firebasestorage.googleapis.com/v0/b/shalominc-smartbeats.appspot.com/o/SAINt%20Jhn%20-%20Roses.mp3?alt=media&token=d077c318-e028-4ee1-a043-bc896c49dacb";
                DBSongName = getString(R.string.roses);
                DBSongExtension = getString(R.string.mp3);
                TextChanger = getString(R.string.downloading_roses);

                downloadingSong();
                databaseToMediaPlayer();

                break;
        }
    }

    //Receives the musics from database and connects to musics;
    public void databaseToMediaPlayer(){
        shalomMediaPlayer.reset();

        try
        {
            shalomMediaPlayer.setDataSource(DBSongUrlChoice);
            shalomMediaPlayer.prepare();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // Refactored code where it would download songs and play it from the stream so now it prompts a snackbar, downloads song and enables user to still stream the track.
    public void downloadingSong(){
        MusicFragment.DownloadMP3 downloadMP3 = new MusicFragment.DownloadMP3();

        Snackbar snackbarSongOneIM = Snackbar.make(getView(), TextChanger, Snackbar.LENGTH_LONG);
        snackbarSongOneIM.setTextColor(getResources().getColor(R.color.black));
        snackbarSongOneIM.setBackgroundTint(getResources().getColor(R.color.purple_200));
        snackbarSongOneIM.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
        snackbarSongOneIM.show();

        new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) { }

            public void onFinish()
            {
                downloadMP3.execute();
            }
        }.start();

        new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) { }

            public void onFinish() {
                Snackbar snackbarSongOneC = Snackbar.make(getView(), "Download Complete", Snackbar.LENGTH_LONG);
                snackbarSongOneC.setTextColor(getResources().getColor(R.color.black));
                snackbarSongOneC.setBackgroundTint(getResources().getColor(R.color.purple_200));
                snackbarSongOneC.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
                snackbarSongOneC.show();
            }}.start();
    }

    // Music Disc spinning rotating animation
    private void startRotateAnimation() {
        rotateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.spinimage);
        shalomMusicDiscIV.startAnimation(rotateAnimation);

    }

    // Music Disc spinning rotating animation
    private void endRotateAnimation() {
        rotateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.spinimage);
        shalomMusicDiscIV.clearAnimation();
    }

  // Converting format for start and end time of a song
    private String convertFormat(int duration) {
        return String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(duration),
                        TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
}
