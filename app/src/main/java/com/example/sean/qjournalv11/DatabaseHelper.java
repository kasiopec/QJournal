package com.example.sean.qjournalv11;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "Qjournal";
    private static final int DB_VERSION = 1;

    private static final String TABLE_GOALS = "OngoingGoals_table";
    private static final String GOAL_COL_ID = "ID";
    private static final String GOAL_COL_NAME = "Name";
    private static final String GOAL_COL_CAT = "Category";
    private static final String GOAL_COL_START = "StartDate";
    private static final String GOAL_COL_TIMEFRAME = "TimeFrame";
    private static final String GOAL_COL_GOALTIME = "GoalTime";
    private static final String GOAL_COL_CURTIME = "CurrentTime";

    private static final String TABLE_CATEGORIES = "Categories_table";
    private static final String CATEGORY_COL_ID = "ID";
    private static final String CATEGORY_COL_NAME = "Name";

    private static final String TABLE_COMPLETED_GOALS = "CompletedGoals_table";
    private static final String COMPLETED_COL_ID = "ID";
    private static final String COMPLETED_COL_NAME = "Name";
    private static final String COMPLETED_COL_CAT = "Category";
    private static final String COMPLETED_COL_WEEK = "Week";
    private static final String COMPLETED_COL_MONTH = "Month";
    private static final String COMPLETED_COL_YEAR = "Year";
    private static final String COMPLETED_PROG = "Progress";

    private static final String CREATE_TABLE_GOALS = "create table "+TABLE_GOALS +
            "(" + GOAL_COL_ID + " integer primary key autoincrement" + ", "
            + GOAL_COL_NAME + " TEXT not null" + ", "
            + GOAL_COL_CAT + " TEXT not null" + ", "
            + GOAL_COL_START + " TEXT not null" + ", "
            + GOAL_COL_TIMEFRAME + " TEXT not null" + ", "
            + GOAL_COL_GOALTIME + " TEXT not null" + ", "
            + GOAL_COL_CURTIME + " TEXT not null );";

    private static final String CREATE_TABLE_CATEGORIES = "create table " + TABLE_CATEGORIES +
            "(" + CATEGORY_COL_ID + " integer primary key autoincrement" + ", "
            + CATEGORY_COL_NAME + " TEXT not null );";

    private static final String CREATE_TABLE_COMPLETED_GOALS = "create table "+TABLE_COMPLETED_GOALS +
            "(" + COMPLETED_COL_ID + " integer primary key autoincrement" + ", "
            + COMPLETED_COL_NAME + " TEXT not null" + ", "
            + COMPLETED_COL_CAT + " TEXT not null" + ", "
            + COMPLETED_COL_WEEK + " TEXT" + ", "
            + COMPLETED_COL_MONTH + " TEXT" + ", "
            + COMPLETED_COL_YEAR + " TEXT" + ", "
            + COMPLETED_PROG + " TEXT not null );";




    public DatabaseHelper(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_GOALS);
            db.execSQL(CREATE_TABLE_CATEGORIES);

            db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (Name)" + " VALUES ('Sport')");
            db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (Name)" + " VALUES ('School')");
            db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (Name)" + " VALUES ('Gym')");
            db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (Name)" + " VALUES ('Games')");
            db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (Name)" + " VALUES ('Car')");

            db.execSQL(CREATE_TABLE_COMPLETED_GOALS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLETED_GOALS);

        onCreate(db);

    }

    public void addGoal(String name, String category, String timeFrame, int goalTime, String startDate){
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(GOAL_COL_NAME, name);
        values.put(GOAL_COL_CAT, category);
        values.put(GOAL_COL_TIMEFRAME, timeFrame);
        values.put(GOAL_COL_GOALTIME, goalTime);
        values.put(GOAL_COL_START, startDate);
        values.put(GOAL_COL_CURTIME, 0);


        db.insert(TABLE_GOALS, null, values);
        db.close();



    }

    public void dataReset(String timeFrame, int week_month, int year){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = null;
        if(timeFrame.equals("Weekly")){

           selectQuery = "INSERT INTO " + TABLE_COMPLETED_GOALS + " (" +
                    COMPLETED_COL_NAME + "," +
                    COMPLETED_COL_CAT + "," +
                    COMPLETED_COL_WEEK + "," +
                    COMPLETED_COL_MONTH + "," +
                    COMPLETED_COL_YEAR + "," +
                    COMPLETED_PROG + ") SELECT " +
                    GOAL_COL_NAME + "," +
                    GOAL_COL_CAT + "," +
                    "'" + week_month+ "'," +
                   "'null'," +
                   "'" + year+ "'," +
                    GOAL_COL_CURTIME + " FROM " +
                    TABLE_GOALS + " WHERE " + GOAL_COL_TIMEFRAME + " = '" + timeFrame + "'";

        }else if (timeFrame.equals("Monthly")){

            selectQuery = "INSERT INTO " + TABLE_COMPLETED_GOALS + " (" +
                    COMPLETED_COL_NAME + "," +
                    COMPLETED_COL_CAT + "," +
                    COMPLETED_COL_WEEK + "," +
                    COMPLETED_COL_MONTH + "," +
                    COMPLETED_COL_YEAR + "," +
                    COMPLETED_PROG + ") SELECT " +
                    GOAL_COL_NAME + "," +
                    GOAL_COL_CAT + "," +
                    "'null'," +
                    "'" + week_month + "'," +
                    "'" + year + "'," +
                    GOAL_COL_CURTIME + " FROM " +
                    TABLE_GOALS + " WHERE " + GOAL_COL_TIMEFRAME + " = '" + timeFrame + "'";
        }

        db.execSQL(selectQuery);

        ContentValues values = new ContentValues();
        values.put(GOAL_COL_CURTIME, 0);

        db.update(TABLE_GOALS, values,
                GOAL_COL_TIMEFRAME + " = ?", new String[]{timeFrame});

        db.close();

    }

    public void updateGoalProgress(String goal_id, int progTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GOAL_COL_CURTIME, progTime);

        db.update(TABLE_GOALS, values,
                GOAL_COL_ID + " = ?", new String[]{String.valueOf(goal_id)});
        db.close();
    }

    public void updateGoal(Goal g ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GOAL_COL_NAME, g.getName());
        values.put(GOAL_COL_TIMEFRAME, g.getTimeFrame());
        values.put(GOAL_COL_GOALTIME, g.getGoalTime());

        db.update(TABLE_GOALS, values,
                GOAL_COL_ID + " = ?", new String[]{String.valueOf(g.getId())});
        //db update
        db.close();
    }

    public ArrayList<Goal> getAllGoals(){

        ArrayList<Goal> goals = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_GOALS;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                Goal goal = new Goal();
                goal.setId(cursor.getInt(cursor.getColumnIndex(GOAL_COL_ID)));
                goal.setName(cursor.getString(cursor.getColumnIndex(GOAL_COL_NAME)));
                goal.setCategory(cursor.getString(cursor.getColumnIndex(GOAL_COL_CAT)));
                goal.setCurrentTime(cursor.getInt(cursor.getColumnIndex(GOAL_COL_CURTIME)));
                goal.setTimeFrame(cursor.getString(cursor.getColumnIndex(GOAL_COL_TIMEFRAME)));
                goal.setStartDate(cursor.getString(cursor.getColumnIndex(GOAL_COL_START)));
                goal.setGoalTime(cursor.getInt(cursor.getColumnIndex(GOAL_COL_GOALTIME)));

                goals.add(goal);
            }while (cursor.moveToNext());
        }

        db.close();
        return goals;


    }

    public void addCategory (String name){

        SQLiteDatabase db =this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CATEGORY_COL_NAME, name);

        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }

    public ArrayList<String> getAllCategories(){
        ArrayList<String> categories = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES;

        SQLiteDatabase db =this.getWritableDatabase();


        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                categories.add(cursor.getString(cursor.getColumnIndex(CATEGORY_COL_NAME)));

            }while (cursor.moveToNext());
        }

        db.close();

        return categories;
    }

    public void removeCategory(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORIES, CATEGORY_COL_NAME + " = ?", new String[]{name});
        db.delete(TABLE_GOALS, GOAL_COL_CAT + " = ? ", new String[]{name});
        db.close();
    }

    //Removes category block on the Main screen.
    public void removeGoals(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GOALS, GOAL_COL_CAT + " = ? ", new String[]{name});
        db.close();
    }

    public void removeGoal(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GOALS, GOAL_COL_ID + " = ?", new String[]{id});
        db.close();
    }

    public ArrayList<Goal> getAllCategoryGoals(String category, String timeFrame){
        ArrayList<Goal> goals = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = " SELECT * FROM " + TABLE_GOALS + " WHERE " + GOAL_COL_CAT + " = '" +
                category + "' AND " + GOAL_COL_TIMEFRAME + " = '" + timeFrame + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Goal goal = new Goal();
                goal.setId(cursor.getInt(cursor.getColumnIndex(GOAL_COL_ID)));
                goal.setName(cursor.getString(cursor.getColumnIndex(GOAL_COL_NAME)));
                goal.setCategory(cursor.getString(cursor.getColumnIndex(GOAL_COL_CAT)));
                goal.setCurrentTime(cursor.getInt(cursor.getColumnIndex(GOAL_COL_CURTIME)));
                goal.setTimeFrame(cursor.getString(cursor.getColumnIndex(GOAL_COL_TIMEFRAME)));
                goal.setStartDate(cursor.getString(cursor.getColumnIndex(GOAL_COL_START)));
                goal.setGoalTime(cursor.getInt(cursor.getColumnIndex(GOAL_COL_GOALTIME)));

                goals.add(goal);
            }while (cursor.moveToNext());
        }
        db.close();
        return goals;
    }

    public ArrayList<Goal> getWeekGoals(int week_nr, String category, int year){
        ArrayList<Goal> goals = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;

        String selectQuery = "SELECT * FROM "
                + TABLE_COMPLETED_GOALS + " WHERE "
                + COMPLETED_COL_CAT + " = '" + category + "' AND "
                + COMPLETED_COL_WEEK + " = '" + week_nr + "' AND "
                + COMPLETED_COL_YEAR + " = '" + year + "'";

        String selectQuery2 = "SELECT * FROM "
                + TABLE_COMPLETED_GOALS + " WHERE "
                + COMPLETED_COL_WEEK + " = '" + week_nr + "' AND "
                + COMPLETED_COL_YEAR + " = '" + year + "'";

        if(category.equals("All")){
            cursor = db.rawQuery(selectQuery2, null);
            Log.d("myDB", selectQuery2);
        }else{
            cursor = db.rawQuery(selectQuery, null);
            Log.d("myDB", selectQuery);

        }

        if(cursor.moveToFirst()){
            do{
                Goal goal = new Goal();
                goal.setName(cursor.getString(cursor.getColumnIndex(COMPLETED_COL_NAME)));
                goal.setCategory(cursor.getString(cursor.getColumnIndex(COMPLETED_COL_CAT)));
                goal.setCurrentTime(cursor.getInt(cursor.getColumnIndex(COMPLETED_PROG)));
                goals.add(goal);

            }while (cursor.moveToNext());
        }
        db.close();
        return goals;
    }

    public ArrayList<Goal> getMonthGoals(int month_nr, String category, int year){
        ArrayList<Goal> goals = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;

        String selectQuery = "SELECT * FROM "
                + TABLE_COMPLETED_GOALS + " WHERE "
                + COMPLETED_COL_CAT + " = '" + category + "' AND "
                + COMPLETED_COL_MONTH + " = '" + month_nr + "' AND "
                + COMPLETED_COL_YEAR + " = '" + year + "'";

        String selectQuery2 = "SELECT * FROM "
                + TABLE_COMPLETED_GOALS + " WHERE "
                + COMPLETED_COL_MONTH + " = '" + month_nr + "' AND "
                + COMPLETED_COL_YEAR + " = '" + year + "'";

        if(category.equals("All")){
            cursor = db.rawQuery(selectQuery2, null);
            Log.d("myDB", selectQuery2);
        }else{
            cursor = db.rawQuery(selectQuery, null);
            Log.d("myDB", selectQuery);

        }

        if(cursor.moveToFirst()){
            do{
                Goal goal = new Goal();
                goal.setName(cursor.getString(cursor.getColumnIndex(COMPLETED_COL_NAME)));
                goal.setCategory(cursor.getString(cursor.getColumnIndex(COMPLETED_COL_CAT)));
                goal.setCurrentTime(cursor.getInt(cursor.getColumnIndex(COMPLETED_PROG)));
                goals.add(goal);

            }while (cursor.moveToNext());
        }
        db.close();
        return goals;
    }



}
