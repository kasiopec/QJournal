package com.example.sean.qjournalv11;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

/**
 Class which tracks broadcasts for daily notification.
 **/

public class AlarmReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_CH_ID = "daily_notifications";
    private NotificationManagerCompat nt_mangerCompat;

    @Override
    public void onReceive(Context context, Intent intent) {

        //creating intent for the activity to open on notification click
        Intent notificationIntent = new Intent(context, MainActivity.class);
        nt_mangerCompat = NotificationManagerCompat.from(context);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent, 0);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel ch_daily = new NotificationChannel(
                    NOTIFICATION_CH_ID,
                    "Daily notifications",
                    NotificationManager.IMPORTANCE_LOW);

            ch_daily.setDescription("These notifications informs user about daily updates");

            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.createNotificationChannel(ch_daily);
        }


        Notification notification = new NotificationCompat.Builder(context, NOTIFICATION_CH_ID)
                .setSmallIcon(R.drawable.ic_diary_black_24dp)
                .setContentTitle("Daily update")
                .setContentText("Goal update is required, tap to open the app.")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .build();

        nt_mangerCompat.notify(1, notification);

        ApplicationActivity appActivity = new ApplicationActivity();
        appActivity.startNotificationTimer(context);

        Log.d("alarma", "called the alarm from alarmReceiver");


    }
}
