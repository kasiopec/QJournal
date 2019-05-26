package com.example.sean.qjournalv11;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ParentFragment.OnFragmentInteractionListener {
    private EventOperations eventDBoperation;
    private ListView m_listview;
    private GoalsAdapter goalsAdapter;

    private SimpleCursorAdapter dataAdapter;


    private int hours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.sean.qjournalv11.R.layout.activity_main);


        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.getWritableDatabase();

        if(!isMyServiceRunning(NotificationService.class)){
            hours = 2;
            Intent intentService = new Intent(getBaseContext(), NotificationService.class);
            intentService.putExtra("hours",hours);
            startService(intentService);
        }


        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewGoalActivity.class);
                startActivity(intent);


            }
        });


        eventDBoperation = new EventOperations(this);
        eventDBoperation.open();


        Toolbar toolbar = (Toolbar) findViewById(com.example.sean.qjournalv11.R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager =  (ViewPager) findViewById(R.id.viewPagerMain);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayoutMain);
        tabLayout.setupWithViewPager(viewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(com.example.sean.qjournalv11.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, com.example.sean.qjournalv11.R.string.navigation_drawer_open, com.example.sean.qjournalv11.R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(com.example.sean.qjournalv11.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ParentFragment.newInstance("Weekly"), "WEEKLY");
        adapter.addFragment(ParentFragment.newInstance("Monthly"), "MONTHLY");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

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

    public boolean isMyServiceRunning(Class<?> serviceClass){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if (serviceClass.getName().equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.example.sean.qjournalv11.R.menu.main, menu);
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
                Intent i0 = new Intent(getBaseContext(), NewGoalActivity.class);
                startActivity(i0);
                return true;
            case R.id.goals:
                return true;
            case R.id.activities:
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
            Intent i = new Intent(getBaseContext(), Show_History.class);
            startActivity(i);

        } else if (id == com.example.sean.qjournalv11.R.id.nav_help){
            Intent i = new Intent(getBaseContext(), Help.class);
            startActivity(i);
        } else if (id == R.id.nav_share){
            //implement sharing function
        }else if (id == com.example.sean.qjournalv11.R.id.nav_exit){
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(com.example.sean.qjournalv11.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}