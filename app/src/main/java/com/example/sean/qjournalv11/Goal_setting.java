package com.example.sean.qjournalv11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class Goal_setting extends AppCompatActivity {
    private EventOperations eventDBoperation;
    private ListView m_listview;
    private TextView m_textlist;
    public String cat;
    public String id;
    public String period="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.sean.qjournalv11.R.layout.activity_goal_setting);

        eventDBoperation = new EventOperations(this);
        eventDBoperation.open();
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        cat = intent.getStringExtra("cat");
        String ncat = eventDBoperation.getCat(id);
        if(!id.equals("1"))cat=cat+"->"+ncat;
        TextView m_txtaddcat = (TextView) findViewById(com.example.sean.qjournalv11.R.id.cattitle);
        m_txtaddcat.setText(cat);

        TextView hours = (TextView) findViewById(com.example.sean.qjournalv11.R.id.hours);
        hours.setText("1");

        Button button1 = (Button) findViewById(com.example.sean.qjournalv11.R.id.addnewgoal);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                TextView hours = (TextView) findViewById(com.example.sean.qjournalv11.R.id.hours);
                String hoursv = hours.getText().toString();
                if (hours.getText().toString().trim().equals("")) {

                    hours.setError("Category is required!");

                } else {
                    TextView tdate = (TextView) findViewById(com.example.sean.qjournalv11.R.id.hours);
                    Spinner perid = (Spinner)   findViewById(com.example.sean.qjournalv11.R.id.period);
                    String peridv = perid.getSelectedItem().toString();
                    if(peridv.equals("Weekly"))period="7";
                    if(peridv.equals("Monthly"))period="30";
                    eventDBoperation.addGoal(cat,id, hoursv, period);
                    Intent intent1 = new Intent(Goal_setting.this, Goals.class);
                    startActivity(intent1);
                }
            }
        });




        Button buttonhome = (Button) findViewById(com.example.sean.qjournalv11.R.id.home);
        buttonhome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(Goal_setting.this, MainActivity.class);
                startActivity(intent1);
            }
        });


        Button buttonGoal = (Button) findViewById(com.example.sean.qjournalv11.R.id.bgoals);
        buttonGoal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(Goal_setting.this, Goals.class);
                startActivity(intent1);
            }
        });
    }
}
