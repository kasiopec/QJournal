package com.kasiopec.qjournal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_CH_ID = "daily_notifications";

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent notificationIntent = new Intent(context, MainActivity.class);

        NotificationManagerCompat nt_mangerCompat = NotificationManagerCompat.from(context);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch_daily = new NotificationChannel(
                    NOTIFICATION_CH_ID,
                    "Daily notifications",
                    NotificationManager.IMPORTANCE_LOW
            );

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        nt_mangerCompat.notify(1, notification);
    }
}
