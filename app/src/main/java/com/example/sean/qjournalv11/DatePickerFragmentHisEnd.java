package com.example.sean.qjournalv11;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by admin on 10-04-2016.
 */
public class DatePickerFragmentHisEnd extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        //Button stbutton = (Button)getActivity().findViewById(R.id.datestart);
       // TextView pickedEnd = (TextView)getActivity().findViewById(com.example.sean.qjournalv11.R.id.dateend);
        String Month=Integer.toString(month+1);
        if(month<=9)Month="0"+Month;
        String Day=Integer.toString(day);
        if(day<=9)Day="0"+Day;
        String outset=Integer.toString(year) + "-" + Month + "-" + Day;

       // pickedEnd.setText(outset);
    }
}