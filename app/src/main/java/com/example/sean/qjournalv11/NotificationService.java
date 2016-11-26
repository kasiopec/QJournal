package com.example.sean.qjournalv11;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Random;

/**
 * Created by Kasio on 13.04.2016.
 */
public class NotificationService extends Service {



    String[] msg;
    private int motoNr;
    private Random rand;
    private int timeleft;



    @Override
    //binds an activity to the service
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId){

   timeleft = convert(intent.getIntExtra("hours", 2));
    //timeleft = 10000;

        msg =  getResources().getStringArray(com.example.sean.qjournalv11.R.array.moto);
        rand = new Random();
        motoNr = rand.nextInt(msg.length);

        //what to be launched
        Log.w("MyApp", "timeleft " + timeleft);
        //2h countdown
        new CountDownTimer(timeleft, 1000){

            public void onTick(long millisUntilFinished){

            }
            public void onFinish() {
                //launch notification
                showNotification();
                onDestroy();
            }

        }.start();

        //start sticky means, that will be running until it is stopped.
        return START_STICKY;
    }

    public void showNotification(){

        NotificationManager myNotificationManager;
        int NOTE_ID = 1337;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("QJournal");
        mBuilder.setTicker("New Qjournal notification");
        mBuilder.setSmallIcon(com.example.sean.qjournalv11.R.drawable.desknotifier_icon);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg[motoNr]));
        mBuilder.setAutoCancel(true);

        //Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("notificationId", NOTE_ID);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //This ensures that navigating backward from the Activity leads out of the app to Home page
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent
        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        // start the activity when the user clicks the notification text
        mBuilder.setContentIntent(resultPendingIntent);

        myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT < 16) {
            myNotificationManager.notify(NOTE_ID, mBuilder.getNotification());
        } else {
            myNotificationManager.notify(NOTE_ID, mBuilder.build());
        }
    }

    public void onDestroy(){
        this.stopSelf();
    }

    private int convert(int hours){
        return this.timeleft = hours * 3600 * 1000;
    }


}
