package com.example.sean.qjournalv11;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.EventLogTags;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Show_History extends AppCompatActivity implements GraphFragment.OnFragmentInteractionListener{
    private EventOperations eventDBoperation;
    public int day=0;
    public String id="1";
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.sean.qjournalv11.R.layout.activity_show__history);

        Toolbar toolbar = (Toolbar) findViewById(com.example.sean.qjournalv11.R.id.toolbar);
        setSupportActionBar(toolbar);

       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setDisplayShowHomeEnabled(true);

        ViewPager viewPager =  (ViewPager) findViewById(R.id.viewPagerGraphs);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayoutGraph);
        tabLayout.setupWithViewPager(viewPager);


       // eventDBoperation = new EventOperations(this);
       // eventDBoperation.open();
        //Event stud = eventDBoperation.addEvent("Add new event");

        //  List values = eventDBoperation.getAllEvents("0");

       // Intent intent = getIntent();
       // id = intent.getStringExtra("id");
       // day = Integer.parseInt(intent.getStringExtra("startdate"));




        /*
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry( eventDBoperation.getSactivityDate(id.toString(),getdateAgo(-6+day)), 0));
        entries.add(new BarEntry( eventDBoperation.getSactivityDate(id.toString(),getdateAgo(-5+day)), 1));
        entries.add(new BarEntry( eventDBoperation.getSactivityDate(id.toString(),getdateAgo(-4+day)), 2));
        entries.add(new BarEntry( eventDBoperation.getSactivityDate(id.toString(),getdateAgo(-3+day)), 3));
        entries.add(new BarEntry( eventDBoperation.getSactivityDate(id.toString(),getdateAgo(-2+day)), 4));
        entries.add(new BarEntry( eventDBoperation.getSactivityDate(id.toString(),getdateAgo(-1+day)), 5));
        entries.add(new BarEntry( eventDBoperation.getSactivityDate(id.toString(),getdateAgo(0+day)), 6));

        */



        /*
        ArrayList<String> labels = new ArrayList<String>();
        labels.add(getdateAgo(-6+day).substring(8));
        labels.add(getdateAgo(-5+day).substring(8));
        labels.add(getdateAgo(-4+day).substring(8));
        labels.add(getdateAgo(-3+day).substring(8));
        labels.add(getdateAgo(-2+day).substring(8));
        labels.add(getdateAgo(-1+day).substring(8));
        labels.add(getdateAgo(0+day).substring(8));
        */


    }
    private String getdateAgo(int dag) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, dag);
        return dateFormat.format(cal.getTime());
    }

    private void setupViewPager(ViewPager viewPager){
        Show_History.ViewPagerAdapter adapter = new Show_History.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(GraphFragment.newInstance("Weekly"), "WEEKLY");
        adapter.addFragment(GraphFragment.newInstance("Monthly"), "MONTHLY");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
