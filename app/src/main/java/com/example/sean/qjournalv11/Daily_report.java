package com.example.sean.qjournalv11;

import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.sql.Date;

public class Daily_report extends AppCompatActivity {



    private EventOperations eventDBoperation;
    private ListView m_listview;
    public NotificationService ns = new NotificationService();

    private DataBaseWrapper dbHelper;
    private SimpleCursorAdapter dataAdapter;
    public NotificationManager myNotificationManager;
    public int NOTE_ID = 1337;
    public String dstartv;
    public String dendv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.sean.qjournalv11.R.layout.activity_dailly_report);


            eventDBoperation = new EventOperations(this);
            eventDBoperation.open();
            //Event stud = eventDBoperation.addEvent("Add new event");

            //  List values = eventDBoperation.getAllEvents("0");

        Intent intent = getIntent();
        dstartv = intent.getStringExtra("dstartv");
        dendv = intent.getStringExtra("dendv");
        Button btns = (Button) findViewById(com.example.sean.qjournalv11.R.id.startdate);
        Button btne = (Button) findViewById(com.example.sean.qjournalv11.R.id.enddate);
        btns.setText(dstartv);
        btne.setText(dendv);
            Cursor cursor =eventDBoperation.getAllEventsdate(dstartv,dendv);




            // The desired columns to be bound
            String[] columns = new String[] {
                    DataBaseWrapper.EVENT_ID,
                    DataBaseWrapper.EVENT_DATE,
                    DataBaseWrapper.EVENT_STARTTIME,
                    DataBaseWrapper.EVENT_ENDTIME,
                    DataBaseWrapper.EVENT_CAT
            };

            // the XML defined views which the data will be bound to
            int[] to = new int[] {
                    com.example.sean.qjournalv11.R.id._id,
                    com.example.sean.qjournalv11.R.id._date,
                    com.example.sean.qjournalv11.R.id._starttime,
                    com.example.sean.qjournalv11.R.id._endtime,
                    com.example.sean.qjournalv11.R.id._name
            };




            dataAdapter = new SimpleCursorAdapter(
                    this, com.example.sean.qjournalv11.R.layout.list_layout_daily,
                    cursor,columns,to,0);


            m_listview = (ListView) findViewById(com.example.sean.qjournalv11.R.id.list);



            m_listview.setAdapter(dataAdapter);


            m_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    String nid = Long.toString(m_listview.getItemIdAtPosition(position));

                    // Toast.makeText(getBaseContext(), parent.getItemAtPosition(position).getLong(0), Toast.LENGTH_LONG).show();
                    //String nid=Long.toString(position);

                    //m_listview.getAdapter().getItem(position);
                    Intent intent = new Intent(Daily_report.this, editor_subevent.class);
                    intent.putExtra("id", nid);
                    intent.putExtra("dendv", dendv);
                    intent.putExtra("dstartv", dstartv);
                    startActivity(intent);


                }
            });

        Button btn1 = (Button) findViewById(R.id.back);
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        Button btn2 = (Button) findViewById(R.id.home);
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Daily_report.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
