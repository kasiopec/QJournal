package com.example.sean.qjournalv11;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ShowGraphs extends AppCompatActivity implements GraphFragment.OnFragmentInteractionListener{
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


    }
    private String getdateAgo(int dag) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, dag);
        return dateFormat.format(cal.getTime());
    }

    private void setupViewPager(ViewPager viewPager){
        ShowGraphs.ViewPagerAdapter adapter = new ShowGraphs.ViewPagerAdapter(getSupportFragmentManager());
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
