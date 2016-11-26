package com.example.sean.qjournalv11;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import java.util.Calendar;

public class edit_subevent extends AppCompatActivity {


    private EventOperations eventDBoperation;
    private ListView m_listview;
    private TextView m_textlist;
    public String cat;
    private DataBaseWrapper dbHelper;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.sean.qjournalv11.R.layout.activity_edit_subevent);



        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String Month=Integer.toString(month+1);
        if(month<=9)Month="0"+Month;
        String Day=Integer.toString(day);
        if(day<=9)Day="0"+Day;
        String outset=Integer.toString(year) + "-" + Month + "-" + Day;



        eventDBoperation = new EventOperations(this);
        eventDBoperation.open();

        String[] lastactivity=eventDBoperation.getlastevent();
        if(!lastactivity[0].equals(""))outset=lastactivity[0];
        Button stbutton = (Button) this.findViewById(com.example.sean.qjournalv11.R.id.btndate);

        stbutton.setText(outset);


        final Calendar t = Calendar.getInstance();
        int hour = t.get(Calendar.HOUR_OF_DAY);
        int minute = t.get(Calendar.MINUTE);

        Button stbuttons = (Button) this.findViewById(com.example.sean.qjournalv11.R.id.btnstarttime);
        String HourOfDay=Integer.toString(hour);
        if(hour<=9)HourOfDay="0"+HourOfDay;
        String Minute=Integer.toString(minute);
        if(minute<=9)Minute="0"+Minute;
        outset=HourOfDay + ":" + Minute;
        if(!lastactivity[1].equals(""))outset=lastactivity[1];
        stbuttons.setText(outset);

        Button stbuttone = (Button) this.findViewById(com.example.sean.qjournalv11.R.id.btnendtime);
        HourOfDay=Integer.toString(hour);
        if(hour<=9)HourOfDay="0"+HourOfDay;
        Minute=Integer.toString(minute);
        if(minute<=9)Minute="0"+Minute;
        outset=HourOfDay + ":" + Minute;

        stbuttone.setText(outset);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        cat = intent.getStringExtra("cat");
        String ncat = eventDBoperation.getCat(id);
        if(!id.equals("1"))cat=cat+"->"+ncat;

        TextView m_txtaddcat = (TextView) findViewById(com.example.sean.qjournalv11.R.id.cattitle);
        String topcatinfo=m_txtaddcat.getText()+"\n"+cat;
        if(cat.equals(""))topcatinfo="No category";
        m_txtaddcat.setText(topcatinfo);

        if (id.equals("1")) {
            m_listview = (ListView) findViewById(com.example.sean.qjournalv11.R.id.sublist);
            m_listview.setVisibility(View.GONE);
            Button buttonadnew = (Button) findViewById(com.example.sean.qjournalv11.R.id.addnew);
            buttonadnew.setText("Home");

        } else {

            //m_textlist = (TextView) findViewById(R.id.textlist);
            //m_textlist.setVisibility(View.GONE);
            //Event stud = eventDBoperation.addEvent("Add new event");


            // TextView t4 = (TextView) findViewById(R.id.textView4);
            // t4.setText(t4.getText()+" - -"+id);


            Cursor cursor = eventDBoperation.getAllEventsc(id);


            // The desired columns to be bound
            String[] columns = new String[]{
                    DataBaseWrapper.EVENT_ID,
                    DataBaseWrapper.EVENT_NAME
            };

            // the XML defined views which the data will be bound to
            int[] to = new int[]{
                    com.example.sean.qjournalv11.R.id._id,
                    com.example.sean.qjournalv11.R.id._name
            };


            dataAdapter = new SimpleCursorAdapter(
                    this, com.example.sean.qjournalv11.R.layout.list_layout,
                    cursor, columns, to, 0);


            m_listview = (ListView) findViewById(com.example.sean.qjournalv11.R.id.sublist);


            m_listview.setAdapter(dataAdapter);


            m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    String nid = Long.toString(m_listview.getItemIdAtPosition(position));

                    //m_listview.getAdapter().getItem(position);
                    Intent intent = new Intent(edit_subevent.this, edit_subevent.class);
                    intent.putExtra("id", nid);
                    intent.putExtra("cat", cat);
                    startActivity(intent);

                }
            });
        }// if id is equal zero

        TextView textView4 = (TextView) findViewById(com.example.sean.qjournalv11.R.id.textView4);
        textView4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent1 = new Intent(edit_subevent.this, MainActivity.class);
                startActivity(intent1);
            }
        });

        Button button = (Button) findViewById(com.example.sean.qjournalv11.R.id.addnew);
        button.setOnClickListener(new View.OnClickListener() {
            Intent intent = getIntent();
            String id = intent.getStringExtra("id");

            public void onClick(View v) {
                if (id.equals("1")) {
                    Intent intent1 = new Intent(edit_subevent.this, MainActivity.class);
                    startActivity(intent1);

                }else {

                    Intent intent = getIntent();
                    String id = intent.getStringExtra("id");
                    TextView tname = (TextView) findViewById(com.example.sean.qjournalv11.R.id.itemname);
                    String tnamev = tname.getText().toString();
                    if (tname.getText().toString().trim().equals("") && id.equals("1")) {

                        tname.setError("Category is required!");

                    } else {
                        TextView tdate = (TextView) findViewById(com.example.sean.qjournalv11.R.id.btndate);
                        String tdatev = tdate.getText().toString();
                        TextView tstarttime = (TextView) findViewById(com.example.sean.qjournalv11.R.id.btnstarttime);
                        String tstarttimev = tstarttime.getText().toString();
                        TextView tendtime = (TextView) findViewById(com.example.sean.qjournalv11.R.id.btnendtime);
                        String tendtimev = tendtime.getText().toString();
                        int tstarttimevint = Integer.parseInt(tstarttimev.replace(":", ""));
                        int tendtimevint = Integer.parseInt(tendtimev.replace(":", ""));
                        int difval=tendtimevint-tstarttimevint;
                        if(difval>=0) {
                        eventDBoperation.addEvent("", id, tnamev, cat, tdatev, tstarttimev, tendtimev);
                            finish();
                        }else{
                            tname.setError("Your end time is less than your start time, because it is daily raport we need all activities registered during one day" +
                                    ", if your activity is after 12 PM please split it in two activities.");
                        }
                    }
                    Toast.makeText(getApplicationContext(), tnamev + " event has been created", Toast.LENGTH_SHORT).show();
                }

            }
        });



        Button button1 = (Button) findViewById(com.example.sean.qjournalv11.R.id.addnewcat);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Intent intent = getIntent();
                String id = intent.getStringExtra("id");
                String cat = intent.getStringExtra("cat");
                TextView tname = (TextView) findViewById(com.example.sean.qjournalv11.R.id.itemname);
                String tnamev = tname.getText().toString();
                if (tname.getText().toString().trim().equals("")) {

                    tname.setError("Category name is required!");

                } else {
                    TextView tdate = (TextView) findViewById(com.example.sean.qjournalv11.R.id.btndate);
                    String tdatev = tdate.getText().toString();
                    TextView tstarttime = (TextView) findViewById(com.example.sean.qjournalv11.R.id.btnstarttime);
                    String tstarttimev = tstarttime.getText().toString();
                    TextView tendtime = (TextView) findViewById(com.example.sean.qjournalv11.R.id.btnendtime);
                    String tendtimev = tendtime.getText().toString();
                    int tstarttimevint = Integer.parseInt(tstarttimev.replace(":", ""));
                    int tendtimevint = Integer.parseInt(tendtimev.replace(":", ""));
                    int difval=tendtimevint-tstarttimevint;
                    if(difval>=0) {
                    eventDBoperation.addEvent(tnamev, id, "",cat, tdatev, tstarttimev, tendtimev);
                    finish();
                    }else{
                        tstarttimev=tendtimev;
                        eventDBoperation.addEvent(tnamev, id, "",cat, tdatev, tstarttimev, tendtimev);
                        finish();
                    }
                }
            }
        });

    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showTimePickerDialogend(View v) {
        DialogFragment newFragment = new TimePickerFragmentend();
        newFragment.show(getSupportFragmentManager(), "timePickere");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


}

