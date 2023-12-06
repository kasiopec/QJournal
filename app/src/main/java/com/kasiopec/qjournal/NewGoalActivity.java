package com.kasiopec.qjournal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

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

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

public class NewGoalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseHelper db;
    ArrayList<String> categories;
    Spinner catSpinner;
    ArrayAdapter<String> catAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

        db = new DatabaseHelper(this);
        categories = db.getAllCategories();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        final EditText editTextName  = (EditText) findViewById(R.id.goalNameEdit);
        final EditText goalHours  = (EditText) findViewById(R.id.goalTextHours);
        final EditText goalMin  = (EditText) findViewById(R.id.goalTextMin);
        Button btnSaveGoal = (Button) findViewById(R.id.btnSaveGoal);
        ImageButton btnAddCat = (ImageButton) findViewById(R.id.imgBtnNewCat);
        ImageButton btnDeleteCat = (ImageButton) findViewById(R.id.imgBtnDel);
        final TextInputLayout nameTil = (TextInputLayout) findViewById(R.id.textInputLayoutGoalName);
        final TextInputLayout hourTil = (TextInputLayout) findViewById(R.id.textInputLayoutHours);
        final TextInputLayout minTil = (TextInputLayout) findViewById(R.id.textInputLayoutMin);


        catSpinner = (Spinner) findViewById(R.id.spinnerCategory);
        final Spinner wmSpinner = (Spinner) findViewById(R.id.spinnerWM);

        catAdapter = new ArrayAdapter<String> (this, R.layout.support_simple_spinner_dropdown_item, categories);


        ArrayAdapter<CharSequence> adapterWM = ArrayAdapter.createFromResource(this,
                R.array.period_arrays, android.R.layout.simple_spinner_dropdown_item);


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

        btnSaveGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        v.setEnabled(true);
                    }
                }, 500);
                String name = editTextName.getText().toString();
                String category = catSpinner.getSelectedItem().toString();
                int hours = 0;
                int min  = 0;

                if(name.equals("")){
                    nameTil.setError("Name can not be empty");
                    return;
                }else if(goalMin.getText().toString().equals("") && goalHours.getText().length() > 0){
                    hours = Integer.parseInt(goalHours.getText().toString());
                }else if (goalHours.getText().toString().equals("") && goalMin.getText().length()>0) {
                    min = Integer.parseInt(goalMin.getText().toString());
                }else if(goalHours.getText().length() == 0 && goalMin.getText().length() == 0) {
                    hourTil.setError("Enter minutes or hours");
                    minTil.setError(" ");
                    return;
                }else{

                    min = Integer.parseInt(goalMin.getText().toString());
                    hours = Integer.parseInt(goalHours.getText().toString());
                }

                int goalTimeInMin = hours*60 + min;

                String timeFrame = wmSpinner.getSelectedItem().toString();

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();

                String startDate = dateFormat.format(cal.getTime());

                db.addGoal(name, category, timeFrame, goalTimeInMin, startDate);
                Intent i = new Intent(NewGoalActivity.this, MainActivity.class);
                startActivity(i);

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
                            db.addCategory(catName);
                            categories.add(catName);
                            catAdapter.notifyDataSetChanged();
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
                        db.removeCategory(catSpinner.getSelectedItem().toString());
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(com.kasiopec.qjournal.R.id.drawer_layout);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(i);
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

        if (id == com.kasiopec.qjournal.R.id.nav_home) {
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
        } else if (id == com.kasiopec.qjournal.R.id.nav_goals) {
            Intent i = new Intent(getBaseContext(), EditCategoryActivity.class);
            startActivity(i);

        }else if (id == com.kasiopec.qjournal.R.id.nav_exit){
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);

        } else if (id == com.kasiopec.qjournal.R.id.nav_history){
            Intent i = new Intent(getBaseContext(), NewGoalActivity.class);
            startActivity(i);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(com.kasiopec.qjournal.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
