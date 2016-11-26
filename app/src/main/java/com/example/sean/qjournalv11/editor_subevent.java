package com.example.sean.qjournalv11;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class editor_subevent extends AppCompatActivity {


    private EventOperations eventDBoperation;
    private ListView m_listview;
    private TextView m_textlist;
    public String cat;
    private DataBaseWrapper dbHelper;
    private SimpleCursorAdapter dataAdapter;
public String dstartv;
    public String dendv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.sean.qjournalv11.R.layout.activity_editor_subevent);


        Intent intent = getIntent();
        dstartv = intent.getStringExtra("dstartv");
        dendv = intent.getStringExtra("dendv");
        String id = intent.getStringExtra("id");
        eventDBoperation = new EventOperations(this);
        eventDBoperation.open();



        String cdate=eventDBoperation.getItem(id,"_date");
        String starttime=eventDBoperation.getItem(id,"_starttime");
        String endtime=eventDBoperation.getItem(id,"_endtime");
        String name=eventDBoperation.getItem(id,"_name");
        String desc=eventDBoperation.getItem(id,"_desc");
        name=desc;
        String cat=eventDBoperation.getItem(id,"_cat");
        Button stbutton = (Button) this.findViewById(com.example.sean.qjournalv11.R.id.btndate);
        stbutton.setText(cdate);

        Button stbuttons = (Button) this.findViewById(com.example.sean.qjournalv11.R.id.btnstarttime);
        stbuttons.setText(starttime);

        Button stbuttone = (Button) this.findViewById(com.example.sean.qjournalv11.R.id.btnendtime);
        stbuttone.setText(endtime);

        TextView tname = (TextView) findViewById(com.example.sean.qjournalv11.R.id.itemname);
        tname.setText(name);
        TextView cattitle = (TextView) findViewById(com.example.sean.qjournalv11.R.id.cattitle);
        cattitle.setText(cat);


        Button button1 = (Button) findViewById(com.example.sean.qjournalv11.R.id.addnewcat);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Intent intent = getIntent();
                String id = intent.getStringExtra("id");
                String cat = intent.getStringExtra("cat");
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
                        eventDBoperation.editEvent(tnamev, id, "", cat, tdatev, tstarttimev, tendtimev);
                        Intent intent1 = new Intent(editor_subevent.this, Daily_report.class);
                        intent1.putExtra("dendv", dendv);
                        intent1.putExtra("dstartv", dstartv);
                        startActivity(intent1);
                    }else{
                        tname.setError("Your end time is less than your start time, because it is daily raport we need all activities registered during one day" +
                                ", if your activity is after 12 PM please split it in two activities.");
                    }
                }
            }
        });



        Button buttonevent = (Button) findViewById(com.example.sean.qjournalv11.R.id.event);
        buttonevent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // Intent intent1 = new Intent(editor_subevent.this, History.class);
               // startActivity(intent1);
                finish();
            }
        });

        Button buttonhome = (Button) findViewById(com.example.sean.qjournalv11.R.id.home);
        buttonhome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(editor_subevent.this, MainActivity.class);
                startActivity(intent1);
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

