package com.example.sean.qjournalv11;



import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class Goals extends AppCompatActivity {
    LinearLayout ll;
    TextView bottomText;
    BottomAppBar bottomAppBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.sean.qjournalv11.R.layout.activity_goals);

        setTitle(getString(R.string.categoryActivityTitle, "XXCategory"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bottomAppBar = (BottomAppBar) findViewById(R.id.bottomAppBar);
        bottomAppBar.replaceMenu(R.menu.bottom_app_bar_menu);

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.appBarEditGoal:
                        FragmentManager fragManager= getSupportFragmentManager();
                        EditGoalDialog editGoalDialog  = EditGoalDialog.newInstance("huj");
                        editGoalDialog.show(fragManager, "tag");


                    case R.id.appBarDel:

                }
                return false;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabBottomBar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll.setVisibility(View.INVISIBLE);
            }
        });
        bottomText = (TextView) findViewById(R.id.your_title);
        ll= (LinearLayout) findViewById(R.id.bottomWrapper);
        ll.setVisibility(View.INVISIBLE);


        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_edit_category);

        final List<String> data  = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(String.valueOf(i));
        }

        CategoryAdapter categoryAdapter = new CategoryAdapter(this, data, this);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(categoryAdapter);


    }





    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
