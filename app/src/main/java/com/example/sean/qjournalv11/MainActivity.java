package com.example.sean.qjournalv11;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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
                Intent intent = new Intent(MainActivity.this, History.class);
                startActivity(intent);


            }
        });









        eventDBoperation = new EventOperations(this);
        eventDBoperation.open();
        //Event stud = eventDBoperation.addEvent("Add new event");

     //  List values = eventDBoperation.getAllEvents("0");


        Cursor cursor =eventDBoperation.getAllEventsc("0");




        // The desired columns to be bound
        String[] columns = new String[] {
                DataBaseWrapper.EVENT_ID,
                DataBaseWrapper.EVENT_NAME
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                com.example.sean.qjournalv11.R.id._id,
                com.example.sean.qjournalv11.R.id._name
        };





String sharewithusstatus=eventDBoperation.getSettingStatus("sharingwithus");
       if(sharewithusstatus.equals("1")) {
           String android_id = Settings.Secure.getString(this.getContentResolver(),
                   Settings.Secure.ANDROID_ID);

           Cursor cursorsync = eventDBoperation.getAllEventsSync();
           for (cursorsync.moveToFirst(); !cursorsync.isAfterLast(); cursorsync.moveToNext()) {
               SendDataToServer(android_id, "events", cursorsync.getString(0), cursorsync.getString(1), cursorsync.getString(2)
                       , cursorsync.getString(3), cursorsync.getString(4), cursorsync.getString(5)
                       , cursorsync.getString(6), cursorsync.getString(7), cursorsync.getString(8)
                       , cursorsync.getString(9), cursorsync.getString(10));
           }

           Cursor cursorsyncg = eventDBoperation.getAllGoalsSync();
           for (cursorsyncg.moveToFirst(); !cursorsyncg.isAfterLast(); cursorsyncg.moveToNext()) {
               SendDataToServer(android_id, "goals", cursorsyncg.getString(0), cursorsyncg.getString(1), cursorsyncg.getString(2)
                       , cursorsyncg.getString(3), cursorsyncg.getString(4), cursorsyncg.getString(5)
                       , cursorsyncg.getString(6), "", ""
                       , "", "");
           }
       }


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

/*
        Button btn1 = (Button) findViewById(com.example.sean.qjournalv11.R.id.button);
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hours = 4;
                Intent intentService = new Intent(getBaseContext(), NotificationService.class);
                intentService.putExtra("hours", hours);
                Toast.makeText(getApplicationContext(), "Notification in " + hours + " hours.", Toast.LENGTH_LONG).show();
                stopService(intentService);
                startService(intentService);


            }
        });

        Button btn2 = (Button) findViewById(com.example.sean.qjournalv11.R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hours = 6;
                Intent intentService = new Intent(getBaseContext(), NotificationService.class);
                intentService.putExtra("hours", hours);
                Toast.makeText(getApplicationContext(), "Notification in " + hours + " hours.", Toast.LENGTH_LONG).show();
                stopService(intentService);
                startService(intentService);

            }
        });

        Button btn3 = (Button) findViewById(com.example.sean.qjournalv11.R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hours = 12;
                Intent intentService = new Intent(getBaseContext(), NotificationService.class);
                intentService.putExtra("hours", hours);
                Toast.makeText(getApplicationContext(), "Notification in " + hours + " hours.", Toast.LENGTH_LONG).show();
                stopService(intentService);
                startService(intentService);

            }
        });
*/



       // new SyncData();


/*
        String url = "http://www.parsvisa.com/sean/sean/api1/index.php";
        String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
        String param1 = "value1";
        String param2 = "value2";

        URLConnection.setDefaultAllowUserInteraction(true);
        try {
            String query = String.format("param1=%s&param2=%s",
                    URLEncoder.encode(param1, charset),
                    URLEncoder.encode(param2, charset));

            URLConnection connection = new URL(url).openConnection();
            connection.setDoOutput(true); // Triggers POST.
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            OutputStream output=connection.getOutputStream();
            output.write(query.getBytes(charset));


            InputStream response = connection.getInputStream();

            Button btn7 = (Button) findViewById(R.id.button2);
            btn7.setText(response.toString());
        }catch (IOException e){

        }

        */

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

        } else if (id == com.example.sean.qjournalv11.R.id.nav_help){
            Intent i = new Intent(getBaseContext(), Help.class);
            startActivity(i);
        } else if (id == R.id.nav_share){
            Intent i = new Intent(getBaseContext(), Share.class);
            startActivity(i);
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

    public void SendDataToServer(final String androidid, final String table, final String id
                                 ,final String name , final String raw, final String cat, final String upid
            , final String descr, final String dated, final String starttimed
            , final String endtimed, final String active
            , final String sync){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
/*                String QuickName = name ;
                String QuickEmail = email ;
                String QuickWebsite = website;*/
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("androidid", androidid));
                nameValuePairs.add(new BasicNameValuePair("table", table));
                nameValuePairs.add(new BasicNameValuePair("id", id));
                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("raw", raw));
                nameValuePairs.add(new BasicNameValuePair("cat", cat));
                nameValuePairs.add(new BasicNameValuePair("upid", upid));
                nameValuePairs.add(new BasicNameValuePair("descr", descr));
                nameValuePairs.add(new BasicNameValuePair("dated", dated));
                nameValuePairs.add(new BasicNameValuePair("starttimed", starttimed));
                nameValuePairs.add(new BasicNameValuePair("endtimed", endtimed));
                nameValuePairs.add(new BasicNameValuePair("active", active));
                nameValuePairs.add(new BasicNameValuePair("sync", sync));
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://parsvisa.com/sean/app1/index.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();

                    eventDBoperation = new EventOperations(MainActivity.this);
                    eventDBoperation.open();
                    if(table.equals("events"))
                    {eventDBoperation.setEventsSync(id);
                    }else if(table.equals("goals"))
                    {eventDBoperation.setGoalsSync(id);
                    }
                } catch (ClientProtocolException e) {
                } catch (IOException e) {
                }
                return table;
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                //Toast.makeText(MainActivity.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(androidid,table,id,name,raw,cat,upid,descr,dated,starttimed,endtimed,active,sync);
    }
}