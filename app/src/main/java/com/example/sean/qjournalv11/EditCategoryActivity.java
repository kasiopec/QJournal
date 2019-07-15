package com.example.sean.qjournalv11;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.Iterator;
import java.util.List;

/**
 Class which represent edit category activity
 **/
public class EditCategoryActivity extends AppCompatActivity implements OnDialogCloseListener {
    LinearLayout ll;
    TextView bottomText;
    BottomAppBar bottomAppBar;
    RecyclerView recyclerView;
    public String passedCategory;
    public String passingFragmentName;
    private String goalID;
    private DatabaseHelper db;
    private CategoryAdapter categoryAdapter;
    private List<Goal> data;
    Iterator<Goal> itr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.sean.qjournalv11.R.layout.activity_edit_category);


        //getting goal details from category adapter class
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-msg"));

        //Loading extras from above
        Bundle extras = getIntent().getExtras();
        passedCategory = extras.getString("category");
        passingFragmentName = extras.getString("tab_name");

        db = new DatabaseHelper(this);
        data  = db.getAllCategoryGoals(passedCategory, passingFragmentName);
        categoryAdapter = new CategoryAdapter(this, data, this);

        setTitle(getString(R.string.categoryActivityTitle, passedCategory));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //bottom bar for goal navigation appears on click in the category adapter class
        bottomAppBar = (BottomAppBar) findViewById(R.id.bottomAppBar);
        bottomAppBar.replaceMenu(R.menu.bottom_app_bar_menu);

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                FragmentManager fragManager= getSupportFragmentManager();
                itr = data.iterator();
                switch (id){
                    case R.id.appBarInsertProg:
                        int curProg = 0;
                        //check for goals in the list
                        while (itr.hasNext()){
                            Goal g = itr.next();
                            if(g.getId() == Integer.parseInt(goalID)){
                                curProg = g.getCurrentTime();
                            }
                        }
                        //call edit goal progress fragment where user can change goal progress
                        EditGoalProgressDialog editGoalDialogProgress = EditGoalProgressDialog.newInstance(
                                getApplicationContext(),
                                "EditGoalProgressDialog",
                                goalID, curProg,EditCategoryActivity.this);
                        editGoalDialogProgress.show(fragManager, "tag");
                        break;

                    case R.id.appBarEditGoal:
                        while (itr.hasNext()){
                            Goal g = itr.next();
                            if(g.getId() == Integer.parseInt(goalID)){
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("Goal", g);
                                //call edit goal fragment where user can edit goal content
                                EditGoalDialog editGoalDialog  = EditGoalDialog.newInstance(getApplicationContext(), "EditGoal", EditCategoryActivity.this);
                                editGoalDialog.setArguments(bundle);
                                editGoalDialog.show(fragManager, "tag");
                            }
                        }
                        break;

                    //deletes goal if delete button was pressed
                    case R.id.appBarDel:
                        db.removeGoal(goalID);
                        while (itr.hasNext()){
                            if(itr.next().getId() == Integer.parseInt(goalID)){
                                itr.remove();
                                categoryAdapter.notifyDataSetChanged();
                                ll.setVisibility(View.INVISIBLE);
                            }
                        }
                        break;
                }
                return false;
            }
        });

        //floating button setup
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabBottomBar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll.setVisibility(View.INVISIBLE);
            }
        });
        //bottomText = (TextView) findViewById(R.id.your_title);

        ll= (LinearLayout) findViewById(R.id.bottomWrapper);
        ll.setVisibility(View.INVISIBLE);


        recyclerView = (RecyclerView) findViewById(R.id.rv_edit_category);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(categoryAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //broadcast receiver which listend for categoryAdapter data
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            goalID = intent.getStringExtra("goalID");

        }
    };





    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //when dialog box (fragment) is closed update recall recycleview with new data
    @Override
    public void onDialogClose() {
        data  = db.getAllCategoryGoals(passedCategory, passingFragmentName);
        categoryAdapter = new CategoryAdapter(this, data, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(categoryAdapter);

        itr = data.iterator();
        while (itr.hasNext()){
            Goal g = itr.next();
            if(g.getId() == Integer.parseInt(goalID)){
                //add selected goal name, not the best use of this feature
                bottomAppBar.getMenu().findItem(R.id.appBarTitle).setTitle(g.getName());
            }
        }

    }


}
