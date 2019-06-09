package com.example.sean.qjournalv11;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

import static com.example.sean.qjournalv11.SettingsActivity.NOTIFICATION_TIME;

public class DeviceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")){
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);


            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int notificationTime = Integer.valueOf(prefs.getString(NOTIFICATION_TIME, "2")) * 60;
            calendar.add(Calendar.MINUTE, notificationTime);


            Calendar newC = new GregorianCalendar();
            newC.setTimeInMillis(notificationTime * 60 * 1000);

            if (calendar.after(newC)) {
                calendar.add(Calendar.HOUR, 1);
            }

            if (manager != null) {
                manager.setRepeating(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        notificationTime * 60 * 1000,
                        pendingIntent);
            }


        }
    }
}
