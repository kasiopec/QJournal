package com.kasiopec.qjournal;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.Date;

import static com.kasiopec.qjournal.SettingsActivity.NOTIFICATION_TIME;

public class ApplicationActivity extends Application {
    public static final String UPDATE_WEEK = "QJournalUpdateWeek";
    public static final String UPDATE_MONTH = "QJournalUpdateMonth";
    public static final String NOTIFICATION_CH_ID1 = "weekly_notifications";
    public static final String NOTIFICATION_CH_ID2 = "monthly_notifications";
    public static final String DAILY_NOTIFICATION_BOOL = "DailyNotifications";
    public static final String GENERAL_NOTIFICATIONS_BOOL = "GeneralResetNotifications";
    public static final String WEEKLY_NOTIFICATIONS_BOOL = "WeeklyNotifications";
    public static final String MONTHLY_NOTIFICATIONS_BOOL = "MonthlyNotifications";
    private static DatabaseHelper db;

    private NotificationManagerCompat nt_mangerCompat;
    private SharedPreferences prefs;
    SharedPreferences.Editor editor;


    String timeFrame;
    @Override
    public void onCreate() {
        super.onCreate();

        db = new DatabaseHelper(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);


        //Creating default values for notification setting in shared preferences
        editor = prefs.edit();
        if(!prefs.contains(DAILY_NOTIFICATION_BOOL)){
            editor.putBoolean(DAILY_NOTIFICATION_BOOL, true);
        }else if(!prefs.contains(GENERAL_NOTIFICATIONS_BOOL)){
            editor.putBoolean(GENERAL_NOTIFICATIONS_BOOL, true);
        }else if (!prefs.contains(WEEKLY_NOTIFICATIONS_BOOL)){
            editor.putBoolean(WEEKLY_NOTIFICATIONS_BOOL, true);
        }else if(!prefs.contains(MONTHLY_NOTIFICATIONS_BOOL)){
            editor.putBoolean(MONTHLY_NOTIFICATIONS_BOOL, true);
        }

        editor.apply();


        //custom method to launch new timer for daily notifictions, restart on new app open
        startNotificationTimer(this);

        //custom method for creating notification channels
        createNotificationChannels();

        //Notification push manager
        nt_mangerCompat = NotificationManagerCompat.from(this);


        //Setting up update dates with calendar
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(currentDate);
        int currentWeek = c.get(Calendar.WEEK_OF_YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentYear = c.get(Calendar.YEAR);
        if(!prefs.contains(UPDATE_WEEK) && !prefs.contains(UPDATE_MONTH)){
            editor = prefs.edit();
            editor.putInt(UPDATE_WEEK, currentWeek);
            editor.putInt(UPDATE_MONTH, currentMonth);
            editor.apply();
        }
        int updateWeek = prefs.getInt(UPDATE_WEEK, -1);
        int updateMonth = prefs.getInt(UPDATE_MONTH, -1);


        //checking if weekly updates were done
        if(currentWeek != updateWeek && updateWeek != -1){
            timeFrame = "Weekly";

            db.dataReset(timeFrame, currentWeek-1, currentYear);
            editor = prefs.edit();
            editor.putInt(UPDATE_WEEK, currentWeek);
            editor.apply();

            startResetNotification(timeFrame);

        }
        //checking if monthly updates were done
        if (currentMonth != updateMonth && updateMonth != -1){
            timeFrame = "Monthly";
            db.dataReset(timeFrame, currentMonth-1, currentYear);
            editor = prefs.edit();
            editor.putInt(UPDATE_MONTH, currentMonth);
            editor.apply();

            startResetNotification(timeFrame);

        }



    }

    //Method to create notification channels for Oreo and higher.
    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel ch_weekly = new NotificationChannel(
                    NOTIFICATION_CH_ID1,
                    "Weekly notifications",
                    NotificationManager.IMPORTANCE_LOW
            );
            ch_weekly.setDescription("These notifications informs user about weekly updates");


            NotificationChannel ch_monthly = new NotificationChannel(
                    NOTIFICATION_CH_ID2,
                    "Monthly notifications",
                    NotificationManager.IMPORTANCE_LOW
            );

            ch_monthly.setDescription("These notifications informs user about weekly updates");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(ch_weekly);
            manager.createNotificationChannel(ch_monthly);


        }
    }
    //Method which sends reset notification on reset complete
    public void startResetNotification(String timeFrame){

        //bools from settings activity
        boolean generalResetNotify = prefs.getBoolean(GENERAL_NOTIFICATIONS_BOOL, true);
        boolean weeklyNotify = prefs.getBoolean(WEEKLY_NOTIFICATIONS_BOOL, true);
        boolean monthlyNotify = prefs.getBoolean(MONTHLY_NOTIFICATIONS_BOOL, true);


        if(generalResetNotify){
            if(weeklyNotify && timeFrame.equals("Weekly")){
                //call weekly notifications

                Notification notificationWeekly = new NotificationCompat.Builder(this, NOTIFICATION_CH_ID1)
                        .setSmallIcon(R.drawable.ic_diary_black_24dp)
                        .setContentTitle("Goals updated")
                        .setContentText("Weekly goals have been reset.")
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .build();

                nt_mangerCompat.notify(2, notificationWeekly);
            }

            if(monthlyNotify && timeFrame.equals("Monthly")){
                //call monthly notifications

                Notification notificationMonthly = new NotificationCompat.Builder(this, NOTIFICATION_CH_ID2)
                        .setSmallIcon(R.drawable.ic_diary_black_24dp)
                        .setContentTitle("Goals updated")
                        .setContentText("Monthly goals have been reset.")
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .build();

                nt_mangerCompat.notify(1, notificationMonthly);
            }
        }


    }

    //Method to start daily notification
    public void startNotificationTimer(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        boolean dailyNotify = prefs.getBoolean(DAILY_NOTIFICATION_BOOL, true);


        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_MUTABLE);
        PackageManager packageManager = context.getPackageManager();
        ComponentName receiver = new ComponentName(context, DeviceBootReceiver.class);

        if(dailyNotify){

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            int notificationTime = (int) Math.round(Double.valueOf(prefs.getString(NOTIFICATION_TIME, "2")) * 60);

            calendar.add(Calendar.MINUTE, notificationTime);

            long repeatIn = notificationTime * 60 * 1000;


            if(manager != null){
                manager.setExact(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    manager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            pendingIntent);
                }
            }

            packageManager.setComponentEnabledSetting(
                    receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                    PackageManager.DONT_KILL_APP);
        //killing alarm if daily notifications are disabled
        }else{
            if(PendingIntent.getBroadcast(context,
                    0, alarmIntent, PendingIntent.FLAG_MUTABLE) != null && manager != null){
                manager.cancel(pendingIntent);
            }

            packageManager.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }




    }
}
