package com.example.sean.qjournalv11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Goal_setting_edit extends AppCompatActivity {
    private EventOperations eventDBoperation;
    private ListView m_listview;
    private TextView m_textlist;
    public String cat;
    public String id;
    public int totalminuts=0;
    public String period="0";
    public String report="";
    String pecnetstring="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.sean.qjournalv11.R.layout.activity_goal_setting_edit);

        eventDBoperation = new EventOperations(this);
        eventDBoperation.open();
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        String goalsname = eventDBoperation.getGoalItem(id,"_name");
        String goalstime = eventDBoperation.getGoalItem(id,"_time");
        String goalperiod = eventDBoperation.getGoalItem(id,"_period");

        String goalcatid = eventDBoperation.getGoalItem(id,"_catid");

        EditText m_txthours = (EditText) findViewById(com.example.sean.qjournalv11.R.id.hours);
        m_txthours.setText(goalstime);
        TextView m_txtaddcat = (TextView) findViewById(com.example.sean.qjournalv11.R.id.cattitle);
        m_txtaddcat.setText(goalsname);

        Spinner sp = (Spinner)findViewById(com.example.sean.qjournalv11.R.id.period);
        if(goalperiod.equals("7"))
        {sp.setSelection(0);
        for(int i=0;i>=-6;i=i-1)
        {totalminuts=totalminuts+eventDBoperation.getSactivityDate(goalcatid.toString(),getdateAgo(i));}
        BigDecimal percent=round((float)totalminuts/(float)(Integer.parseInt(goalstime)*60)*100,0);
           pecnetstring= percent.toString();
            String totalmiutss=Integer.toString(totalminuts);
            report="This activity registered for <b><big>"+totalmiutss+"</big></b> minutes during the last week. You achieved <b><big>"+pecnetstring+"%</big></b> of your goal.";
            //SurfaceView surfaceView=(SurfaceView) findViewById(R.id.surfaceView);
            //surfaceView.set
           // progressBar.setProgress(Integer.parseInt(pecnetstring));
        }
        if(goalperiod.equals("30")) {
            sp.setSelection(1);
            for(int i=0;i>=-29;i=i-1)
            {totalminuts=totalminuts+eventDBoperation.getSactivityDate(goalcatid.toString(),getdateAgo(i));}
            BigDecimal percent=round((float)totalminuts/(float)(Integer.parseInt(goalstime)*60)*100,0);
            pecnetstring= percent.toString();
            String totalmiutss=Integer.toString(totalminuts);
            report="This activity registered for <b>"+totalmiutss+"</b> minutes during the last week. You achieved <b>"+pecnetstring+"%</b> of your goal.";
        }

        TextView statusv = (TextView) findViewById(com.example.sean.qjournalv11.R.id.status);

        statusv.setText(Html.fromHtml(report));
        TextView graph0 = (TextView) findViewById(com.example.sean.qjournalv11.R.id.graph0);
        TextView graph1 = (TextView) findViewById(com.example.sean.qjournalv11.R.id.graph1);

int pecnetstringfrograph=Integer.parseInt(pecnetstring);
        if(pecnetstringfrograph<=40){
        graph0.setText(pecnetstring+"%");}
        else if(pecnetstringfrograph<=100){
            graph1.setText(pecnetstring+"%");
        }else{pecnetstringfrograph=100;

            graph1.setText(pecnetstring+"%");}

        final RelativeLayout.LayoutParams layoutparams = (RelativeLayout.LayoutParams) graph1.getLayoutParams();
        layoutparams.width = pecnetstringfrograph*6;
        graph1.setLayoutParams(layoutparams);


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
                    eventDBoperation.editGoal(id,hoursv, period);
                    Intent intent1 = new Intent(Goal_setting_edit.this, Goals.class);
                    startActivity(intent1);
                }
            }
        });



        Button buttonhome = (Button) findViewById(com.example.sean.qjournalv11.R.id.home);
        buttonhome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(Goal_setting_edit.this, MainActivity.class);
                startActivity(intent1);
            }
        });


        Button buttonGoal = (Button) findViewById(com.example.sean.qjournalv11.R.id.bgoals);
        buttonGoal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(Goal_setting_edit.this, Goals.class);
                startActivity(intent1);
            }
        });
    }

    private String getdateAgo(int dag) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, dag);
        return dateFormat.format(cal.getTime());
    }

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
}
