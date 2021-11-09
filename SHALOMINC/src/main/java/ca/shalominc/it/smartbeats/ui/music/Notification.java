package ca.shalominc.it.smartbeats.ui.music;

import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class Notification {
    public static final String CHANNEL_ID = "channel1";
    public static final String ACTIONPREVIOUS = "actionPrevious";
    public static final String CHANNEL_PLAY = "actionPlay";
    public static final String CHANNEL_NEXT = "actionNext";

    public static Notification notification;

    public static void createNotification(Context context, int playbutton, int pos, int size) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        }
//        notification = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setLargeIcon(icon)
//

    }

}
