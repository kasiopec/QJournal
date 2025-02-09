package com.kasiopec.qjournal;

import static com.kasiopec.qjournal.ApplicationActivity.DAILY_NOTIFICATION_BOOL;
import static com.kasiopec.qjournal.ApplicationActivity.GENERAL_NOTIFICATIONS_BOOL;
import static com.kasiopec.qjournal.ApplicationActivity.MONTHLY_NOTIFICATIONS_BOOL;
import static com.kasiopec.qjournal.ApplicationActivity.WEEKLY_NOTIFICATIONS_BOOL;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private EditText notifText;
    private static final double DEFAULT_NOTIFICATION_TIME = 2.0;
    public static String NOTIFICATION_TIME = "NotificationTime";


    private double notifyTime;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private SwitchCompat switchDaily, switchWeekly, switchMonthly, switchResetGen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (!prefs.contains(NOTIFICATION_TIME)) {
            editor = prefs.edit();
            editor.putString(NOTIFICATION_TIME, String.valueOf(DEFAULT_NOTIFICATION_TIME));
            editor.apply();
            notifyTime = DEFAULT_NOTIFICATION_TIME;
        } else {
            notifyTime = Double.parseDouble(prefs.getString(NOTIFICATION_TIME, "-1"));
        }


        Button btnInc = findViewById(R.id.btnInc);
        Button btnDec = findViewById(R.id.btnDec);
        Button btnApply = findViewById(R.id.btnApply);

        switchDaily = findViewById(R.id.switchDaily);
        switchResetGen = findViewById(R.id.switchResetGen);
        switchWeekly = findViewById(R.id.switchWeekly);
        switchMonthly = findViewById(R.id.switchMonthly);

        applySwitchState();


        switchDaily.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor = prefs.edit();

            editor.putBoolean(DAILY_NOTIFICATION_BOOL, isChecked);

            editor.apply();
        });

        switchResetGen.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor = prefs.edit();
            if (isChecked) {
                editor.putBoolean(GENERAL_NOTIFICATIONS_BOOL, true);
                switchWeekly.setClickable(true);
                switchMonthly.setClickable(true);

            } else {
                editor.putBoolean(GENERAL_NOTIFICATIONS_BOOL, false);
                switchWeekly.setClickable(false);
                switchMonthly.setClickable(false);
            }
            editor.apply();
        });

        switchWeekly.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor = prefs.edit();
            if (isChecked) {
                editor.putBoolean(WEEKLY_NOTIFICATIONS_BOOL, true);
            } else {
                editor.putBoolean(WEEKLY_NOTIFICATIONS_BOOL, false);
            }
            editor.apply();
        });

        switchMonthly.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor = prefs.edit();
            if (isChecked) {
                editor.putBoolean(MONTHLY_NOTIFICATIONS_BOOL, true);
            } else {
                editor.putBoolean(MONTHLY_NOTIFICATIONS_BOOL, false);
            }
            editor.apply();
        });

        notifText = findViewById(R.id.notifEditTextSet);

        notifText.setText(String.valueOf(notifyTime));
        btnInc.setText("+");
        btnInc.setOnClickListener(v -> {
            notifyTime = notifyTime + 0.5;
            if (notifyTime > 24) {
                notifyTime = 24;
            }
            notifText.setText(String.valueOf(notifyTime));
        });
        btnDec.setText("-");
        btnDec.setOnClickListener(v -> {
            notifyTime = notifyTime - 0.5;
            if (notifyTime < 0.5) {
                notifyTime = 0.5;
            }
            notifText.setText(String.valueOf(notifyTime));
        });

        btnApply.setOnClickListener(v -> {
            editor = prefs.edit();
            editor.putString(NOTIFICATION_TIME, notifText.getText().toString());
            editor.apply();

            ApplicationActivity appActivity = new ApplicationActivity();
            appActivity.startNotificationTimer(SettingsActivity.this);

            Intent i = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(i);

        });


    }

    private void applySwitchState() {
        switchDaily.setChecked(prefs.getBoolean(DAILY_NOTIFICATION_BOOL, true));

        if (prefs.getBoolean(GENERAL_NOTIFICATIONS_BOOL, true)) {
            switchResetGen.setChecked(true);
        } else {
            switchResetGen.setChecked(false);
            switchMonthly.setClickable(false);
            switchWeekly.setClickable(false);
        }

        switchWeekly.setChecked(prefs.getBoolean(WEEKLY_NOTIFICATIONS_BOOL, true));

        switchMonthly.setChecked(prefs.getBoolean(MONTHLY_NOTIFICATIONS_BOOL, true));
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
