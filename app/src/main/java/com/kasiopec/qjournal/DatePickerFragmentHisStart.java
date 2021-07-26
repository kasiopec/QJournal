package com.kasiopec.qjournal;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by admin on 10-04-2016.
 */
public class DatePickerFragmentHisStart extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public DatePickerDialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        //Button stbutton = (Button)getActivity().findViewById(R.id.datestart);
        //TextView pickedStart = (TextView)getActivity().findViewById(com.kasiopec.qjournal.R.id.datestart);
        if (day != DialogInterface.BUTTON_NEGATIVE && month != DialogInterface.BUTTON_NEGATIVE && year != DialogInterface.BUTTON_NEGATIVE) {

            String Month = Integer.toString(month + 1);
            if (month <= 9) Month = "0" + Month;
            String Day = Integer.toString(day);
            if (day <= 9) Day = "0" + Day;
            String outset = Integer.toString(year) + "-" + Month + "-" + Day;

           // pickedStart.setText(outset);
        }
    }
}