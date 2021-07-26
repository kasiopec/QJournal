package com.kasiopec.qjournal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;


import androidx.appcompat.app.AppCompatActivity;

import com.kasiopec.qjournal.R;

import static com.kasiopec.qjournal.ApplicationActivity.DAILY_NOTIFICATION_BOOL;
import static com.kasiopec.qjournal.ApplicationActivity.GENERAL_NOTIFICATIONS_BOOL;
import static com.kasiopec.qjournal.ApplicationActivity.MONTHLY_NOTIFICATIONS_BOOL;
import static com.kasiopec.qjournal.ApplicationActivity.WEEKLY_NOTIFICATIONS_BOOL;

public class SettingsActivity extends AppCompatActivity {

    private EditText notifText;
    private static double DEFAULT_NOTIFICATION_TIME = 2.0;
    public static String NOTIFICATION_TIME = "NotificationTime";


    private double notifyTime;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private Switch switchDaily, switchWeekly, switchMonthly, switchResetGen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(!prefs.contains(NOTIFICATION_TIME)){
            editor = prefs.edit();
            editor.putString(NOTIFICATION_TIME, String.valueOf(DEFAULT_NOTIFICATION_TIME));
            editor.apply();
            notifyTime = DEFAULT_NOTIFICATION_TIME;
        }else{
            notifyTime = Double.valueOf(prefs.getString(NOTIFICATION_TIME, "-1"));
        }


        Button btnInc = (Button) findViewById(R.id.btnInc);
        Button btnDec = (Button) findViewById(R.id.btnDec);
        Button btnApply = (Button) findViewById(R.id.btnApply);

        switchDaily = (Switch) findViewById(R.id.switchDaily);
        switchResetGen = (Switch) findViewById(R.id.switchResetGen);
        switchWeekly = (Switch) findViewById(R.id.switchWeekly);
        switchMonthly = (Switch) findViewById(R.id.switchMonthly);

        applySwitchStae();


        switchDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor = prefs.edit();

                if(isChecked){
                    editor.putBoolean(DAILY_NOTIFICATION_BOOL, true);
                }else{
                    editor.putBoolean(DAILY_NOTIFICATION_BOOL, false);
                }

                editor.apply();
            }
        });

        switchResetGen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor = prefs.edit();
                if(isChecked){
                    //shared pref resetNotification = true;
                    editor.putBoolean(GENERAL_NOTIFICATIONS_BOOL, true);
                    switchWeekly.setClickable(true);
                    switchMonthly.setClickable(true);

                }else{
                    editor.putBoolean(GENERAL_NOTIFICATIONS_BOOL, false);
                    switchWeekly.setClickable(false);
                    switchMonthly.setClickable(false);
            }
                editor.apply();
            }
        });

        switchWeekly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor = prefs.edit();
                if(isChecked){
                    editor.putBoolean(WEEKLY_NOTIFICATIONS_BOOL, true);
                }else{
                    editor.putBoolean(WEEKLY_NOTIFICATIONS_BOOL, false);
                }
                editor.apply();
            }
        });

        switchMonthly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor = prefs.edit();
                if(isChecked){
                    editor.putBoolean(MONTHLY_NOTIFICATIONS_BOOL, true);
                }else{
                    editor.putBoolean(MONTHLY_NOTIFICATIONS_BOOL, false);
                }
                editor.apply();
            }
        });

        notifText = (EditText) findViewById(R.id.notifEditTextSet);

        notifText.setText(String.valueOf(notifyTime));

        btnInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyTime = notifyTime + 0.5;
                if(notifyTime > 24){
                    notifyTime = 24;
                }
                notifText.setText(String.valueOf(notifyTime));
            }
        });

        btnDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyTime = notifyTime - 0.5;
                if(notifyTime < 0.5){
                    notifyTime = 0.5;
                }
                notifText.setText(String.valueOf(notifyTime));
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = prefs.edit();
                //editor.putString(NOTIFICATION_TIME, String.valueOf(0.017));
                editor.putString(NOTIFICATION_TIME, notifText.getText().toString());
                editor.apply();

                ApplicationActivity appActivity = new ApplicationActivity();
                appActivity.startNotificationTimer(SettingsActivity.this);

                Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(i);

            }
        });



    }

    private void applySwitchStae(){
        if(prefs.getBoolean(DAILY_NOTIFICATION_BOOL, true)){
            switchDaily.setChecked(true);
        }else{
            switchDaily.setChecked(false);
        }

        if(prefs.getBoolean(GENERAL_NOTIFICATIONS_BOOL, true)){
            switchResetGen.setChecked(true);
        }else{
            switchResetGen.setChecked(false);
            switchMonthly.setClickable(false);
            switchWeekly.setClickable(false);
        }

        if(prefs.getBoolean(WEEKLY_NOTIFICATIONS_BOOL, true)){
            switchWeekly.setChecked(true);
        }else{
            switchWeekly.setChecked(false);
        }

        if(prefs.getBoolean(MONTHLY_NOTIFICATIONS_BOOL, true)){
            switchMonthly.setChecked(true);
        }else{
            switchMonthly.setChecked(false);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
