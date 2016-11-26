package com.example.sean.qjournalv11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Share extends AppCompatActivity {
    private EventOperations eventDBoperation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);

        eventDBoperation = new EventOperations(this);
        eventDBoperation.open();

        String sharewithusstatus=eventDBoperation.getSettingStatus("sharingwithus");
        if(sharewithusstatus.equals("0")){
            TextView txthelpv = (TextView) findViewById(R.id.txthelp);
            txthelpv.setMovementMethod(new ScrollingMovementMethod());
            txthelpv.setText(Html.fromHtml("<b>Share data</b>" + "<br /><br />" +
                    "<small>Dear participant in our research, we really would like to have your registered data" +
                    " as part of our research project anonymously, by accepting this term you will share your register data with us. " +
                    " We just use these data for statistics analysis in our research project about users experience with diary apps." +
                    "<br/><br/> Thanks for your partisanship<br/><br/>" +
                    "QuickJournal Team</small>"));


            Button buttonhome = (Button) findViewById(com.example.sean.qjournalv11.R.id.home);
            buttonhome.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });

            Button buttonshare = (Button) findViewById(R.id.accept);
            buttonshare.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    eventDBoperation.setSettingStatus("sharingwithus","1");
                    //Intent intentService = new Intent(getBaseContext(), Share.class);
                    //startService(intentService);
                    finish();
                }
            });
            buttonshare.setText("Accept");
        }else{
                TextView txthelpv = (TextView) findViewById(R.id.txthelp);
                txthelpv.setMovementMethod(new ScrollingMovementMethod());
                txthelpv.setText(Html.fromHtml("<b>Thanks for sharing</b>" +  "<br /><br />" +
                        "<Smal>We appreciate your sharing, and hope our app will help you for following your progress" +
                        " with your goals and help you to keep your life event for future.<br/><br/>" +
                        "You are in the status sharing and we collect your data, if you changed your mind you can simply click on not sharing" +
                        " so data sharing would be stopped." +
                        "Quickjournal Team </small>"));


                Button buttonhome = (Button) findViewById(com.example.sean.qjournalv11.R.id.home);
                buttonhome.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        finish();
                    }
                });
                Button buttonshare = (Button) findViewById(R.id.accept);
                buttonshare.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        eventDBoperation.setSettingStatus("sharingwithus","0");
                        finish();
                    }
                });
                buttonshare.setText("Stop sharing");
        }
    }
}