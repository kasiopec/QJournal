package com.example.sean.qjournalv11;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

public class History extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<String> categories = new ArrayList<>();
    Spinner catSpinner;
    ArrayAdapter<String> catAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);



        categories.add("Sport");
        categories.add("School");
        categories.add("Gym");
        categories.add("Games");
        categories.add("Car");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




        /*
        DrawerLayout drawer = (DrawerLayout) findViewById(com.example.sean.qjournalv11.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, com.example.sean.qjournalv11.R.string.navigation_drawer_open, com.example.sean.qjournalv11.R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(com.example.sean.qjournalv11.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        */



        EditText goalName  = (EditText) findViewById(R.id.goalNameEdit);
        EditText goalHours  = (EditText) findViewById(R.id.goalTextHours);
        EditText goalMin  = (EditText) findViewById(R.id.goalTextMin);
        Button btnSaveGoal = (Button) findViewById(R.id.btnSaveGoal);
        ImageButton btnAddCat = (ImageButton) findViewById(R.id.imgBtnNewCat);
        ImageButton btnDeleteCat = (ImageButton) findViewById(R.id.imgBtnDel);

        catSpinner = (Spinner) findViewById(R.id.spinnerCategory);
        Spinner wmSpinner = (Spinner) findViewById(R.id.spinnerWM);









        ArrayAdapter<CharSequence> adapterWM = ArrayAdapter.createFromResource(this,
                R.array.period_arrays, android.R.layout.simple_spinner_dropdown_item);

        catAdapter = new ArrayAdapter<String> (this, R.layout.support_simple_spinner_dropdown_item, categories);
        //catAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        catSpinner.setDropDownVerticalOffset(97);
        catSpinner.setAdapter(catAdapter);



        wmSpinner.setDropDownVerticalOffset(97);
        wmSpinner.setAdapter(adapterWM);


        btnAddCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomAlertDialog(v);
            }
        });

        btnDeleteCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomAlertDialog(v);


            }
        });



        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String Month=Integer.toString(month+1);
        if(month<=9)Month="0"+Month;
        String Day=Integer.toString(day);
        if(day<=9)Day="0"+Day;
        String outset=Integer.toString(year) + "-" + Month + "-" + Day;


    }

    public void showCustomAlertDialog(View v){

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();

        TextView aDialogTitle;

        switch (v.getId())
        {
            case R.id.imgBtnNewCat:
                View aDialogView = inflater.inflate(R.layout.alert_dialog_layout_new, null);

                final TextInputLayout aDialogTil = (TextInputLayout) aDialogView.findViewById(R.id.textInputNewCat);
                final EditText newCatEditText = (EditText) aDialogView.findViewById(R.id.newCatNameEdit);
                Button addNewCat = (Button) aDialogView.findViewById(R.id.btnYes);
                Button cancelAddCat = (Button) aDialogView.findViewById(R.id.btnCloseDialog);

                alertDialog.setView(aDialogView);
                alertDialog.show();

                addNewCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Pattern regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]");
                        String catName = newCatEditText.getText().toString();
                        if(regex.matcher(catName).find()){
                            aDialogTil.setError("Name contains restricted symbols");
                        }else{
                            categories.add(catName);
                            alertDialog.dismiss();
                        }

                    }
                });

                cancelAddCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                break;

            case R.id.imgBtnDel:

                View aDialogView2 = inflater.inflate(R.layout.alert_dialog_layout_delete, null);
                TextView msg = (TextView) aDialogView2.findViewById(R.id.textDeleteMsg);
                Button btnYes = (Button) aDialogView2.findViewById(R.id.btnYes);
                Button btnCloseDialog = (Button) aDialogView2.findViewById(R.id.btnCloseDialog);

                String delText = getString(R.string.delCategory, catSpinner.getSelectedItem().toString());
                msg.setText(delText);

                alertDialog.setView(aDialogView2);
                alertDialog.show();

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categories.remove(catSpinner.getSelectedItemPosition());
                        catAdapter.notifyDataSetChanged();
                        alertDialog.dismiss();


                    }
                });

                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                break;
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
