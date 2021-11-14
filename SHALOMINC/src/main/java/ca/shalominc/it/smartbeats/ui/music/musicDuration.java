package ca.shalominc.it.smartbeats.ui.music;

import android.media.MediaPlayer;

import java.util.concurrent.TimeUnit;


// Work in progress for Splitting the Music Fragment
public class musicDuration {

    public static String getDurationText(MediaPlayer mediaP){
        String d;

        int duration = mediaP.getDuration();

        d = convertFormat(duration);

        return d;

    }

    public static String convertFormat(int duration) {

        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

}