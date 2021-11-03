package ca.shalominc.it.smartbeats.ui.music;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import ca.shalominc.it.smartbeats.R;

public class MusicFragment extends Fragment {

    TextView shalomPosition, shalomDuration;
    SeekBar shalomSeekBar;
    ImageView shalomRew, shalomPlay, shalomPause, shalomFastForward;

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;

    Animation rotateAnimation;
    ImageView shalomVinyl;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shalomPosition = view.findViewById(R.id.shalom_timer);
        shalomDuration = view.findViewById(R.id.shalom_timer_duration);
        shalomSeekBar = view.findViewById(R.id.shalom_seekbar);
        shalomRew = view.findViewById(R.id.shalom_rewind);
        shalomPlay = view.findViewById(R.id.bt_play);
        shalomPause = view.findViewById(R.id.bt_pause);
        shalomFastForward = view.findViewById(R.id.bt_ff);
        shalomVinyl = view.findViewById(R.id.shalom_IV);

        rotateAnimation();

        mediaPlayer = MediaPlayer.create(getContext(), R.raw.music);




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

        shalomPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                shalomPlay.setVisibility(View.GONE);

                shalomPause.setVisibility(View.VISIBLE);

                mediaPlayer.start();

                shalomSeekBar.setMax(mediaPlayer.getDuration());

                handler.postDelayed(runnable, 0);



            }
        });

        shalomPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shalomPause.setVisibility(View.GONE);

                shalomPlay.setVisibility(View.VISIBLE);

                mediaPlayer.pause();

                handler.removeCallbacks(runnable);

            }
        });

        shalomFastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mediaPlayer.getCurrentPosition();

                int duration = mediaPlayer.getDuration();

                if (mediaPlayer.isPlaying() && duration != currentPosition){
                    currentPosition = currentPosition + 5000;
                    shalomPosition.setText(convertFormat(currentPosition));
                    mediaPlayer.seekTo(currentPosition);
                    Context context =getActivity();
                    Toast.makeText(context, "Song Fast Forwarded 5 seconds" , Toast.LENGTH_LONG).show();
                }
            }
        });

        shalomRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                if(mediaPlayer.isPlaying() && currentPosition > 5000){
                    currentPosition = currentPosition - 5000;
                    shalomPosition.setText(convertFormat(currentPosition));
                    mediaPlayer.seekTo(currentPosition);
                    Context context =getActivity();
                    Toast.makeText(context, "Song Rewinded by 5 seconds" , Toast.LENGTH_LONG).show();

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

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                shalomPause.setVisibility(View.GONE);
                shalomPlay.setVisibility(View.VISIBLE);
                mediaPlayer.seekTo(0);
            }
        });
    }

    private void rotateAnimation(){

        rotateAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.spinimage);
        shalomVinyl.startAnimation(rotateAnimation);

    }

    private String convertFormat(int duration) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
}