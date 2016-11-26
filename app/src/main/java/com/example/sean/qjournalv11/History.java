package com.example.sean.qjournalv11;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class History extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.sean.qjournalv11.R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(com.example.sean.qjournalv11.R.id.toolbar);
        setSupportActionBar(toolbar);





        DrawerLayout drawer = (DrawerLayout) findViewById(com.example.sean.qjournalv11.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, com.example.sean.qjournalv11.R.string.navigation_drawer_open, com.example.sean.qjournalv11.R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(com.example.sean.qjournalv11.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);





        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String Month=Integer.toString(month+1);
        if(month<=9)Month="0"+Month;
        String Day=Integer.toString(day);
        if(day<=9)Day="0"+Day;
        String outset=Integer.toString(year) + "-" + Month + "-" + Day;

        TextView stbutton = (TextView) this.findViewById(com.example.sean.qjournalv11.R.id.datestart);
        TextView endbutton = (TextView) this.findViewById(com.example.sean.qjournalv11.R.id.dateend);

        stbutton.setText(outset);
        endbutton.setText(outset);


        Button button1 = (Button) findViewById(com.example.sean.qjournalv11.R.id.datesubmit);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                TextView dstart = (TextView) findViewById(com.example.sean.qjournalv11.R.id.datestart);
                String dstartv = dstart.getText().toString();

                TextView dendt = (TextView) findViewById(com.example.sean.qjournalv11.R.id.dateend);
                String dendv = dendt.getText().toString();

                Intent intent1 = new Intent(History.this, Daily_report.class);
                intent1.putExtra("dstartv",dstartv);
                intent1.putExtra("dendv",dendv);
                startActivity(intent1);
            }
        });


        Button buttonhome = (Button) findViewById(com.example.sean.qjournalv11.R.id.home);
        buttonhome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(History.this, MainActivity.class);
                startActivity(intent1);
            }
        });

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(com.example.sean.qjournalv11.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragmentHisStart();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public void showDatePickerDialoge(View v) {
        DialogFragment newFragment = new DatePickerFragmentHisEnd();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.example.sean.qjournalv11.R.menu.history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.example.sean.qjournalv11.R.id.action_settings) {
            Intent i = new Intent(getBaseContext(), Help.class);
            startActivity(i);
            return true;
        }
        switch (item.getItemId()) {
            case com.example.sean.qjournalv11.R.id.action_5:
                Intent i0 = new Intent(getBaseContext(), History.class);
                startActivity(i0);
                return true;
            case com.example.sean.qjournalv11.R.id.action_10:
                Intent i1 = new Intent(getBaseContext(), Goals.class);
                startActivity(i1);
                return true;
            case com.example.sean.qjournalv11.R.id.action_30:
                Intent i2 = new Intent(getBaseContext(), Activity_report.class);
                startActivity(i2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == com.example.sean.qjournalv11.R.id.nav_home) {
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);

        } else if (id == com.example.sean.qjournalv11.R.id.nav_activities) {
            Intent i2 = new Intent(getBaseContext(), Activity_report.class);

            i2.putExtra("id", "0");
            i2.putExtra("cat", "");
            startActivity(i2);

        } else if (id == com.example.sean.qjournalv11.R.id.nav_goals) {
            Intent i = new Intent(getBaseContext(), Goals.class);
            startActivity(i);

        }else if (id == com.example.sean.qjournalv11.R.id.nav_exit){
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);

        } else if (id == com.example.sean.qjournalv11.R.id.nav_history){
            Intent i = new Intent(getBaseContext(), History.class);
            startActivity(i);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(com.example.sean.qjournalv11.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
