package com.example.sean.qjournalv11;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Show_History extends AppCompatActivity {
    private EventOperations eventDBoperation;
    public int day=0;
    public String id="1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.sean.qjournalv11.R.layout.activity_show__history);



        eventDBoperation = new EventOperations(this);
        eventDBoperation.open();
        //Event stud = eventDBoperation.addEvent("Add new event");

        //  List values = eventDBoperation.getAllEvents("0");

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        day = Integer.parseInt(intent.getStringExtra("startdate"));

        TextView txtmotov = (TextView) findViewById(com.example.sean.qjournalv11.R.id.txtmoto);
        txtmotov.setText("This activity from "+getdateAgo(day-6)+" to "+getdateAgo(day));


        Button btnstartdate = (Button) findViewById(R.id.startdate);
        btnstartdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Show_History.this, Show_History.class);
                intent.putExtra("id", id);
                intent.putExtra("startdate",Integer.toString(day-6));
                startActivity(intent);
            }
        });

        Button btnenddate = (Button) findViewById(R.id.enddate);
        btnenddate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Show_History.this, Show_History.class);
                intent.putExtra("id", id);
                intent.putExtra("startdate",Integer.toString(day+6));
                startActivity(intent);
            }
        });



        Button btnhome = (Button) findViewById(com.example.sean.qjournalv11.R.id.home);
        btnhome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Show_History.this, MainActivity.class);
                startActivity(intent);
            }
        });


        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry( eventDBoperation.getSactivityDate(id.toString(),getdateAgo(-6+day)), 0));
        entries.add(new BarEntry( eventDBoperation.getSactivityDate(id.toString(),getdateAgo(-5+day)), 1));
        entries.add(new BarEntry( eventDBoperation.getSactivityDate(id.toString(),getdateAgo(-4+day)), 2));
        entries.add(new BarEntry( eventDBoperation.getSactivityDate(id.toString(),getdateAgo(-3+day)), 3));
        entries.add(new BarEntry( eventDBoperation.getSactivityDate(id.toString(),getdateAgo(-2+day)), 4));
        entries.add(new BarEntry( eventDBoperation.getSactivityDate(id.toString(),getdateAgo(-1+day)), 5));
        entries.add(new BarEntry( eventDBoperation.getSactivityDate(id.toString(),getdateAgo(0+day)), 6));


        BarDataSet dataset = new BarDataSet(entries, "# of Calls");
        ArrayList<String> labels = new ArrayList<String>();
        labels.add(getdateAgo(-6+day).substring(8));
        labels.add(getdateAgo(-5+day).substring(8));
        labels.add(getdateAgo(-4+day).substring(8));
        labels.add(getdateAgo(-3+day).substring(8));
        labels.add(getdateAgo(-2+day).substring(8));
        labels.add(getdateAgo(-1+day).substring(8));
        labels.add(getdateAgo(0+day).substring(8));

        BarChart chart = new BarChart(this);
        LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
        layout.addView(chart, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
       // chart.setContentView(chart);

        BarData data = new BarData(labels, dataset);
        chart.setData(data);
        chart.setDescription("Times spend on category last 7 days");
        dataset.setColor(Color.rgb(200, 199, 133));
        chart.animateY(2000);
    }
    private String getdateAgo(int dag) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, dag);
        return dateFormat.format(cal.getTime());
    }
}
