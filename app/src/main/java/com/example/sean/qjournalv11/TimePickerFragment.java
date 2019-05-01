package com.example.sean.qjournalv11;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TimePicker;
import java.util.Calendar;

/**
 * Created by admin on 09-04-2016.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

       // Button stbutton = (Button)getActivity().findViewById(com.example.sean.qjournalv11.R.id.btnstarttime);
        String HourOfDay=Integer.toString(hourOfDay);
        if(hourOfDay<=9)HourOfDay="0"+HourOfDay;
        String Minute=Integer.toString(minute);
        if(minute<=9)Minute="0"+Minute;
        String outset=HourOfDay + ":" + Minute;

       // stbutton.setText(outset);

    }


}