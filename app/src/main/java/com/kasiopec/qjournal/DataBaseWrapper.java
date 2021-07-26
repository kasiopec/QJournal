package com.kasiopec.qjournal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 Class which creates database

 **/

public class DataBaseWrapper extends SQLiteOpenHelper {

    public static final String EVENTS = "Events";
    public static final String EVENT_ID = "_id";
    public static final String EVENT_RAW = "_raw";
    public static final String EVENT_CAT= "_cat";
    public static final String EVENT_NAME = "_name";
    public static final String EVENT_UPID= "_upid";
    public static final String EVENT_DESC = "_desc";
    public static final String EVENT_DATE = "_date";
    public static final String EVENT_STARTTIME = "_starttime";
    public static final String EVENT_diftime = "diftime";
    public static final String EVENT_ENDTIME = "_endtime";
    public static final String EVENT_ACTIVE = "_active";
    public static final String EVENT_SYNC = "_sync";


    public static final String GOALS = "Goals";
    public static final String GOALS_ID = "_id";

    public static final String GOALS_NAME = "_name";
    public static final String GOALS_CATID = "_catid";
    public static final String GOALS_TIME= "_time";
    public static final String GOALS_PERIOD = "_period";

    public static final String GOALS_ACTIVE = "_active";
    public static final String GOALS_SYNC = "_sync";

    public static final String SETTINGS = "Settings";
    public static final String SETTINGS_ID = "_id";
    public static final String SETTINGS_ITEM = "_item";
    public static final String SETTINGS_STATUS = "_status";

    private static final String DATABASE_NAME = "Events.db";
    private static final int DATABASE_VERSION = 1;

    // creation SQLite statement
    private static final String DATABASE_CREATE = "create table " + EVENTS
            + "(" + EVENT_ID + " integer primary key autoincrement"+
            ", "+ EVENT_NAME + " text not null DEFAULT ''" +
            ", "+EVENT_RAW + " integer not null DEFAULT id"+
            ", "+ EVENT_CAT + " text not null DEFAULT ''" +
            ", "+ EVENT_UPID + " integer DEFAULT 0" +
            ", "+ EVENT_DESC + " text not null DEFAULT ''" +
            ", "+ EVENT_DATE + " text not null DEFAULT ''" +
            ", "+ EVENT_STARTTIME + " text not null DEFAULT ''" +
            ", "+ EVENT_ENDTIME + " text not null DEFAULT ''" +
            ", "+ EVENT_ACTIVE+ " integer DEFAULT 1" +
            ", "+ EVENT_SYNC + " integer DEFAULT 0 );";

    private static final String DATABASE_CREATE1 = "create table " + GOALS
            + "(" + GOALS_ID + " integer primary key autoincrement"+

            ", "+GOALS_NAME + " text not null DEFAULT ''"+
            ", "+GOALS_CATID + " integer not null DEFAULT 0"+
            ", "+GOALS_TIME + " integer not null DEFAULT 0"+
            ", "+GOALS_PERIOD + " integer not null DEFAULT 0"+
            ", "+GOALS_ACTIVE + " integer not null DEFAULT 1"+
            ", "+ GOALS_SYNC + " integer not null DEFAULT 0 );";


    private static final String DATABASE_CREATE2 = "create table " + SETTINGS
            + "(" + SETTINGS_ID + " integer primary key autoincrement"+

            ", "+SETTINGS_ITEM + " text not null DEFAULT ''"+
            ", "+SETTINGS_STATUS + " text not null DEFAULT '' );";


    public DataBaseWrapper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE1);
        db.execSQL(DATABASE_CREATE2);
        db.execSQL("insert into "+EVENTS+" (_name) values ('Add new category')");
        db.execSQL("insert into "+GOALS+" (_name) values ('Add new goal')");
        db.execSQL("insert into "+SETTINGS+" (_item,_status) values ('sharingwithus','0')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you should do some logging in here
        // ..

        db.execSQL("DROP TABLE IF EXISTS " + EVENTS);
        onCreate(db);
    }

}