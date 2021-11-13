package ca.shalominc.it.smartbeats.ui.music;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ca.shalominc.it.smartbeats.R;

import static android.content.ContentValues.TAG;
import static java.sql.Types.NULL;

public class MusicFragment extends Fragment
{
    //Media Player
    MediaPlayer mediaPlayer;
    TextView shalomPosition, shalomDuration;
    SeekBar shalomSeekBar;
    ImageView shalomRew, shalomPlay, shalomPause, shalomFastForward, shalomStop;
    ImageView shalomVinyl;
    Animation rotateAnimation;
    Handler handler = new Handler();
    Runnable runnable;

    //Audio Manager
    AudioManager aM;
    SeekBar shalomVolume;

    //Spinner for user to select songs.
    Spinner shalomSongSpinner;
    String spinnerString;

    //Song selector
    String DBSongUrl, DBSongUrlChoice, DBSongName, DBSongExtension, PDTextChanger;



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
    private long lastDownload;

    //Async
    int PDChoice;
    ProgressDialog PD;
    File dir;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_music, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //Media Player and the options
        mediaPlayer = new MediaPlayer();
        shalomPosition = view.findViewById(R.id.shalom_timer);
        shalomDuration = view.findViewById(R.id.shalom_timer_duration);
        shalomSeekBar = view.findViewById(R.id.shalom_seekbar);
        shalomRew = view.findViewById(R.id.shalom_rewind);
        shalomPlay = view.findViewById(R.id.bt_play);
        shalomPause = view.findViewById(R.id.bt_pause);
        shalomFastForward = view.findViewById(R.id.bt_ff);
        shalomVinyl = view.findViewById(R.id.shalom_IV);
        shalomStop = view.findViewById(R.id.bt_stop);

        //Adjust Volumes.
        shalomVolume = view.findViewById(R.id.shalom_volume);
        aM = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        int maxVol = aM.getStreamMaxVolume(aM.STREAM_MUSIC);
        int curVol = aM.getStreamVolume(aM.STREAM_MUSIC);
        shalomVolume.setMax(maxVol);
        shalomVolume.setProgress(curVol);

        // Timer Count down
        shalomEditTextInput = view.findViewById(R.id.shalom_edit_text_input);
        shalomTextViewCountDown = view.findViewById(R.id.shalom_text_view_countdown);
        shalomButtonSet = view.findViewById(R.id.shalom_button_set);
        shalomButtonStartPause = view.findViewById(R.id.shalom_button_start_pause);
        shalomButtonReset = view.findViewById(R.id.shalom_button_reset);

        //Spinner for user to select their songs
        shalomSongSpinner = view.findViewById(R.id.shalom_music_spinner);
        ArrayAdapter<CharSequence> sAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Songs, android.R.layout.simple_spinner_item);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shalomSongSpinner.setAdapter(sAdapter);

        //Finding the downloads Folder
//        dir = new File (String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
//        if(!dir.exists())
//        {
//            try
//            {
//                dir.mkdirs();
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }

        //Seekbar progress Duration check for the song.
        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                shalomSeekBar.setProgress(mediaPlayer.getCurrentPosition());

                handler.postDelayed(this, 500);

            }
        };

        // Counter for the start text and end text of the music duration.
        int duration = mediaPlayer.getDuration();
        String sDuration = convertFormat(duration);
        shalomDuration.setText(sDuration);


        //Setting up Media Player
        mediaPlayer.setAudioAttributes
                (new AudioAttributes
                        .Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                );

        rotateAnimation();  // Calling function rotateAnimation();

        //Setting Audio Stream / Default Track
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        DBSongUrl = "https://firebasestorage.googleapis.com/v0/b/shalominc-smartbeats.appspot.com/o/BLR%20-%20Taj.mp3?alt=media&token=e3aacbca-33a4-4368-ad0a-7ba49f2f6692";
        try
        {
            mediaPlayer.setDataSource(DBSongUrl);
            mediaPlayer.prepare();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // Spinner Item selector
        shalomSongSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l)
            {
                spinnerString = shalomSongSpinner.getItemAtPosition(position).toString();
                Context context = getContext();
                MusicFragment.DownloadMP3 downloadMP3 = new MusicFragment.DownloadMP3();
                switch (spinnerString)
                {
                    case "Select Your Song":

                        Toast.makeText(context, "Select a Song Below", Toast.LENGTH_LONG).show();

                        break;

                    case "ATC - All Around The World":
                        Toast.makeText(context, "ATC - All Around The World", Toast.LENGTH_LONG).show();
                        DBSongUrlChoice = "https://firebasestorage.googleapis.com/v0/b/shalominc-smartbeats.appspot.com/o/ATC%20-%20All%20Around%20The%20World.mp3?alt=media&token=41077a29-12e9-4371-b8a0-af1c7179a0d4";
                        DBSongName = "ATC - All Around The World";
                        DBSongExtension = ".mp3";
                        PDTextChanger = "Downloading ATC - All Around The World ...";
                        PDChoice = 1;

                        downloadMP3.execute();

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
                        DBSongUrlChoice = "https://firebasestorage.googleapis.com/v0/b/shalominc-smartbeats.appspot.com/o/Dynoro%20-%20In%20My%20Mind.mp3?alt=media&token=8600afad-31fb-4f7f-97b4-92e2968ff851";
                        DBSongName = "Dynoro - In My Mind";
                        DBSongExtension = ".mp3";
                        PDTextChanger = "Downloading Dynoro - In My Mind ...";
                        PDChoice = 2;

                        downloadMP3.execute();

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
                        DBSongUrlChoice = "https://firebasestorage.googleapis.com/v0/b/shalominc-smartbeats.appspot.com/o/MEDUZA%20-%20Lose%20Control.mp3?alt=media&token=92253d10-47c6-455b-897b-14bec7e1b923";
                        DBSongName = "MEDUZA - Lose Control";
                        DBSongExtension = ".mp3";
                        PDTextChanger = "Downloading MEDUZA - Lose Control ...";
                        PDChoice = 3;

                        downloadMP3.execute();

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
                        DBSongUrlChoice = "https://firebasestorage.googleapis.com/v0/b/shalominc-smartbeats.appspot.com/o/Regard%20-%20Ride%20It.mp3?alt=media&token=d52d0d1e-1152-4b64-9cfc-0def83505f00";
                        DBSongName = "Regard - Ride It";
                        DBSongExtension = ".mp3";
                        PDTextChanger = "Downloading Regard - Ride It ...";
                        PDChoice = 4;

                        downloadMP3.execute();

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
                        DBSongUrlChoice = "https://firebasestorage.googleapis.com/v0/b/shalominc-smartbeats.appspot.com/o/SAINt%20Jhn%20-%20Roses.mp3?alt=media&token=d077c318-e028-4ee1-a043-bc896c49dacb";
                        DBSongName = "SAINt Jhn - Roses";
                        DBSongExtension = ".mp3";
                        PDTextChanger = "Downloading SAINt Jhn - Roses ...";
                        PDChoice = 5;

                        downloadMP3.execute();

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

        // Spinner Item selector ENDS HERE

        // Volume changer for music
        shalomVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                aM.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);

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
        // Volume Changer for music Ends here

        // Play button click listener
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

        // Pause button click listener
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

        //FastForward button click Listener
        shalomFastForward.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int currentPosition = mediaPlayer.getCurrentPosition();

                int duration = mediaPlayer.getDuration();

                if (mediaPlayer.isPlaying() && duration != currentPosition) {
                    currentPosition = currentPosition + 5000;
                    shalomPosition.setText(convertFormat(currentPosition));
                    mediaPlayer.seekTo(currentPosition);

                }

            }
        });

        //Rewind button click listener
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

        //Stop button click listener
        shalomStop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mediaPlayer.stop();

                mediaPlayer.seekTo(0);

                handler.removeCallbacks(runnable);
            }
        });

        //SeekBar change listener
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

        //Pause and play visibility
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

    private class DownloadMP3 extends AsyncTask<String, Integer, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Log.w("MP3 PRE-DOWNLOAD","1");
            PD = new ProgressDialog(getContext());
            if(PDChoice == 1)
            {
                PD.setIcon(R.drawable.music_icon_foreground);
            }
            else if (PDChoice == 2)
            {
                PD.setIcon(R.drawable.music_icon_foreground);
            }
            else if (PDChoice == 3)
            {
                PD.setIcon(R.drawable.music_icon_foreground);
            }
            else if (PDChoice == 4)
            {
                PD.setIcon(R.drawable.music_icon_foreground);
            }
            else if(PDChoice == 5)
            {
                PD.setIcon(R.drawable.music_icon_foreground);
            }
            PD.setTitle(PDTextChanger);
            PD.setIndeterminate(false);
            PD.setCancelable(false);
            PD.show();
        }
        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        @Override
        protected String doInBackground(String... urlParams)
        {
            int count;
            try
            {
                Context context1 = getActivity();
                Uri uri=Uri.parse(DBSongUrlChoice);
                mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                lastDownload=
                        mgr.enqueue(new DownloadManager.Request(uri)
                                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                        DownloadManager.Request.NETWORK_MOBILE)
                                .setAllowedOverRoaming(false)
                                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                                        DBSongName+DBSongExtension));


            }
            catch (Exception ignored)
            {
                PD.hide();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Log.w("MP3 DOWNLOADED","2 " + s);
            PD.hide();
        }

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

//   //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

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