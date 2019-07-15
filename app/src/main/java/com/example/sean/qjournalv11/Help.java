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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView txthelpv = (TextView) findViewById(R.id.textViewHelpBody);
        txthelpv.setMovementMethod(new ScrollingMovementMethod());
        txthelpv.setText(Html.fromHtml("<b>Main screen & Goals</b><br />" +
                "<small> Main screen displays active weekly or monthly goals set by user. Swipe left or right to switch between weekly or monthly goals. " +
                "Goals are grouped by categories. Each category can have unlimited amount of goals. " +
                "On the card user can see goal name and progress towards set goal time. " +
                "Goals progress will automatically reset depending on which reset type was selected when creating a goal - once per week or once per month. This option can be changed.</small>" +
                "<br /><br />" +
                "<b>New goal</b>" +
                "<br />" +
                "<small>To add a new goal user must click on the floating button at the bottom of the screen. " +
                "On click user will be redirected to a new screen where goal data should be inserted and saved. " +
                "When saved, goal appears on the main screen.</small>" +
                "<br /><br />" +
                "<b>Categories</b>" +
                "<br />" +
                "<small>When user creates a goal to track it needs to pick category. " +
                "By default there is small list which can be used as inspiration or if it fits well used as a real category. " +
                "User is able to add his own categories by pressing on + sign when creating a new goal." +
                "When goal is created it will be added to the category section and later displayed on the main screen. " +
                "By tapping on the triple dot icon in the right corner of the category user can decide on further actions (delete or edit). </small>" +
                "<br /><br />"+
                "<b>History </b><br />" +
                "<small>This screen allows user to see how much time was spent on goals within specific category. " +
                "All the data is represented as bar charts. " +
                "On top of that user can cycle between weeks or months to see time spent on activities during previous weeks and months.</small>" +
                "<br /><br />"+
                "<b>Notifications</b>" +
                "<br />" +
                "<small>Notification are informing user about weekly and monthly resets as well as daily reminders to update goals progress. Time in which daily reminder appears " +
                "can be modified by the user. Lowest time period can be set to 30 min and highest to 24 hours. All notifications can be disabled in the setting screen.</small>"));

    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
