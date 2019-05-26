package com.example.sean.qjournalv11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        TextView txthelpv = (TextView) findViewById(R.id.txthelp);
        txthelpv.setMovementMethod(new ScrollingMovementMethod());
        txthelpv.setText(Html.fromHtml("<b>Help</b>" +  "<br /><br />" +
                "<b>Add category</b><br /><small> This App is quick journal App and for that all entry must be tagged by " +
                "a category, so you can add one event just by defining time and that event automatically tagged by its category</small>" + "<br /><br />" +
                "<b>Event</b><br /><small>After adding a category you can add an event under that category, an event could be simply " +
                "left empty and just keep the category as a lable, please be careful after adding event we don't create any " +
                "message because this is quick journal and we directly take you to the page which you can add next event, if something " +
                " needed to reedit or change you can find your entry alltimes in history page on the left drawer</small><br /><br />" +
                "<b>NewGoalActivity</b><br /><small>You can allwasy find all your event and categories in specific period of time in that page and " +
                "you can simply edit them</small><br /><br />"+
                "<b>Goal</b><br /><small>After you add a category, then you can choose that as goal, in this way you can follow how much time have you practice " +
                "that category in the target time</small><br /><br />"+
                "<b>Activity</b><br /><small>You can see how much time you spend in one special activiy in each day for a weekly period as a barchart</small><br /><br />"+
                "<b>Reminder</b><br /><small>In the start page of the App you can set a reminder, then you get a notification after the time past so you can remember" +
                " when is the time to add new category or event</small>"));


        Button buttonhome = (Button) findViewById(com.example.sean.qjournalv11.R.id.home);
        buttonhome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
