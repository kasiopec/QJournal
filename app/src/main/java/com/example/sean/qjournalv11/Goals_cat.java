package com.example.sean.qjournalv11;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class Goals_cat extends AppCompatActivity {
    private EventOperations eventDBoperation;
    private ListView m_listview;

    private SimpleCursorAdapter dataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.sean.qjournalv11.R.layout.activity_goals_cat);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        if(!id.equals("1")){
            Intent intent2 = new Intent(Goals_cat.this, Goal_setting_edit.class);
            intent2.putExtra("id", id);
            startActivity(intent2);
        }

        eventDBoperation = new EventOperations(this);
        eventDBoperation.open();
        //Event stud = eventDBoperation.addEvent("Add new event");

        //  List values = eventDBoperation.getAllEvents("0");


        Cursor cursor =eventDBoperation.getAllCats();




        // The desired columns to be bound
        String[] columns = new String[] {
                DataBaseWrapper.EVENT_ID,
                DataBaseWrapper.EVENT_CAT
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                com.example.sean.qjournalv11.R.id._id,
                com.example.sean.qjournalv11.R.id._name
        };




        dataAdapter = new SimpleCursorAdapter(
                this, com.example.sean.qjournalv11.R.layout.list_layout,
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
                Intent intent = new Intent(Goals_cat.this, Goal_setting.class);
                intent.putExtra("id", nid);
                intent.putExtra("cat", "");
                startActivity(intent);


            }
        });

        Button buttonhome = (Button) findViewById(com.example.sean.qjournalv11.R.id.home);
        buttonhome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(Goals_cat.this, MainActivity.class);
                startActivity(intent1);
            }
        });
    }
}
