package com.example.sean.qjournalv11;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class ApplicationActivity extends Application {
    public static final String UPDATE_WEEK = "QJournalUpdateWeek";
    public static final String UPDATE_MONTH = "QJournalUpdateMonth";
    private static DatabaseHelper db;
    String timeFrame;
    @Override
    public void onCreate() {
        super.onCreate();
        db = new DatabaseHelper(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor;



        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        Log.d("myUPD", "FDAYWEEK "+c.getFirstDayOfWeek());
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(currentDate);
        int currentWeek = c.get(Calendar.WEEK_OF_YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        if(!prefs.contains(UPDATE_WEEK) && !prefs.contains(UPDATE_MONTH)){
            Log.d("myUPD", "Prefs does not exist. Creating prefs.");
            editor = prefs.edit();
            editor.putInt(UPDATE_WEEK, currentWeek);
            editor.putInt(UPDATE_MONTH, currentMonth);
            editor.apply();
            Log.d("myUPD", "Prefs Created");
        }
        int updateWeek = prefs.getInt(UPDATE_WEEK, -1);
        int updateMonth = prefs.getInt(UPDATE_MONTH, -1);

        Log.d("myUPD", "mnth "+String.valueOf(updateMonth));
        Log.d("myUPD", "calendar "+c.getTime());

        Log.d("myUPD", "сmnth "+String.valueOf(currentMonth));
        Log.d("myUPD", "сWeek "+String.valueOf(currentWeek));
        Log.d("myUPD", "week "+String.valueOf(updateWeek));



        if(currentWeek != updateWeek && updateWeek != -1){
            timeFrame = "Weekly";
            Log.d("myUPD", "Weeks are different, beginning database update");

            db.dataReset(timeFrame, currentWeek);
            editor = prefs.edit();
            editor.putInt(UPDATE_WEEK, currentWeek);
            editor.apply();

        }else if (currentMonth != updateMonth && updateMonth != -1){
            timeFrame = "Monthly";
            Log.d("myUPD", "Months are different, updating database");
            db.dataReset(timeFrame, currentMonth);
            editor = prefs.edit();
            editor.putInt(UPDATE_WEEK, currentWeek);
            editor.apply();

        }



    }
}
