package com.example.sean.qjournalv11;

/**
 * Created by admin on 08-04-2016.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class EventOperations {

    // Database fields
    private DataBaseWrapper dbHelper;
    private String[] EVENT_TABLE_COLUMNS = { DataBaseWrapper.EVENT_ID, DataBaseWrapper.EVENT_NAME, DataBaseWrapper.EVENT_UPID,DataBaseWrapper.EVENT_CAT, DataBaseWrapper.EVENT_DESC, DataBaseWrapper.EVENT_DATE,DataBaseWrapper.EVENT_STARTTIME,DataBaseWrapper.EVENT_ENDTIME,DataBaseWrapper.EVENT_ACTIVE};
    private SQLiteDatabase database;
    public String Cat;
    public EventOperations(Context context) {
        dbHelper = new DataBaseWrapper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        //database.execSQL("delete from Events where _id <> 1");
    }

    public void close() {
        dbHelper.close();
    }

    public Event addEvent(String name,String upid,String desc,String cat,String date,String starttime,String endtime) {

        ContentValues values = new ContentValues();
if(upid.equals("1")){upid="0";}
        values.put(DataBaseWrapper.EVENT_NAME, name);
        values.put(DataBaseWrapper.EVENT_UPID, Integer.parseInt(upid));
        values.put(DataBaseWrapper.EVENT_DESC, desc);
        values.put(DataBaseWrapper.EVENT_CAT, cat);
        values.put(DataBaseWrapper.EVENT_DATE, date);
        values.put(DataBaseWrapper.EVENT_STARTTIME, starttime);
        values.put(DataBaseWrapper.EVENT_ENDTIME, endtime);
        values.put(DataBaseWrapper.EVENT_ACTIVE, 1);

        long studId = database.insert(DataBaseWrapper.EVENTS, null, values);

        // now that the event is created return it ...
        Cursor cursor = database.query(DataBaseWrapper.EVENTS,
                EVENT_TABLE_COLUMNS, DataBaseWrapper.EVENT_ID + " = "
                        + studId, null,null,null,null,null);

        cursor.moveToFirst();

        Event newComment = parseEvent(cursor);
        cursor.close();
        return newComment;
    }


    public void addGoal(String name,String id,String hours,String period) {

        Cursor cursor1 = dbHelper.getReadableDatabase().rawQuery("insert into Goals (_name,_catid,_time,_period,_active) values (?,?,?,?,?) ",  new String[] { name,id,hours,period,"1" });

        cursor1.moveToFirst();
        cursor1.close();
    }


    public String[] getlastevent() {

        Cursor cursor1 = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM events  ORDER BY _date DESC, _endtime DESC ", null);
        String date="";
        String endtime="";
        if (cursor1.moveToFirst()) {
            date= cursor1.getString(cursor1.getColumnIndex("_date"));
            endtime= cursor1.getString(cursor1.getColumnIndex("_endtime"));
        }
        cursor1.close();
        return new String[]{ date, endtime };
    }


    public void editGoal(String id,String hours,String period) {

        Cursor cursor1 = dbHelper.getReadableDatabase().rawQuery("update Goals set _time=?, _period=? where _id=?",  new String[] {hours,period,id });

        cursor1.moveToFirst();
        cursor1.close();
    }


    public void editEvent(String name,String upid,String desc,String cat,String date,String starttime,String endtime) {

        ContentValues values = new ContentValues();
        if(upid.equals("1")){upid="0";}
        values.put(DataBaseWrapper.EVENT_DESC, name);
        values.put(DataBaseWrapper.EVENT_DATE, date);
        values.put(DataBaseWrapper.EVENT_STARTTIME, starttime);
        values.put(DataBaseWrapper.EVENT_ENDTIME, endtime);
        values.put(DataBaseWrapper.EVENT_SYNC, 0);
        String[] whereArgs = {upid};
        long studId = database.update(DataBaseWrapper.EVENTS, values," _id=?", whereArgs);

        // now that the event is created return it ...
        Cursor cursor = database.query(DataBaseWrapper.EVENTS,
                EVENT_TABLE_COLUMNS, DataBaseWrapper.EVENT_ID + " = "
                        + upid, null,null,null,null,null);

        cursor.moveToFirst();

        Event newComment = parseEvent(cursor);
        cursor.close();

    }

    public Cursor getAllGoals() {

        Cursor cursor1 = dbHelper.getReadableDatabase().rawQuery("select * from Goals where _active = 1 ", null);

        cursor1.moveToFirst();
        return cursor1;
    }

    public void deleteEvent(Event comment) {
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(DataBaseWrapper.EVENTS, DataBaseWrapper.EVENT_ID
                + " = " + id, null);
    }

    public List getAllEvents(String pid) {
        List events = new ArrayList();
        String whereClause = "_upid = "+pid+" and _name<> '' and _active = 1 ";
        Cursor cursor = database.query(DataBaseWrapper.EVENTS,
                EVENT_TABLE_COLUMNS, whereClause, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event = parseEvent(cursor);
            events.add(event);
            cursor.moveToNext();

        }

        cursor.close();
        return events;
    }
    public Cursor getAllEventsc(String pid1) {
        String whereClause1 = "_upid = "+pid1+" and _name<> '' and _active = 1 ";
        Cursor cursor1 = dbHelper.getReadableDatabase().rawQuery("select _id,_name from Events where _upid = ?  and _name<> '' and _active = 1 ", new String[] { pid1 });


        cursor1.moveToFirst();
        return cursor1;
    }


    public String getItem(String pid1,String item) {
        Cursor cursor1 = dbHelper.getReadableDatabase().rawQuery("select * from Events where _id =?", new String[] { pid1});
        String retitem="";
        if (cursor1.moveToFirst()) {
             retitem= cursor1.getString(cursor1.getColumnIndex(item));
        }
        cursor1.close();
        return retitem;
    }


    public String getGoalItem(String pid1,String item) {
        Cursor cursor1 = dbHelper.getReadableDatabase().rawQuery("select * from Goals where _id =?", new String[] { pid1});
        String retitem="";
        if (cursor1.moveToFirst()) {
            retitem= cursor1.getString(cursor1.getColumnIndex(item));
        }
        cursor1.close();
        return retitem;
    }

    public String getCat(String pid1) {
        List events1 = new ArrayList();
        String whereClause1 = "_id = "+pid1+" and _name<> ''";
        Cursor cursor1 = database.query(DataBaseWrapper.EVENTS,
                EVENT_TABLE_COLUMNS, whereClause1, null, null, null, null);

        if (cursor1.moveToFirst()) {
          Cat = cursor1.getString(cursor1.getColumnIndex("_name"));
        }
        return Cat;
    }

    public Cursor getAllEventsSync() {

        Cursor cursor1 = dbHelper.getReadableDatabase().rawQuery("select _id,_name,_raw,_cat,_upid,_desc,_date,_starttime,_endtime,_active,_sync from events where _sync =0", null);
        cursor1.moveToFirst();
        return cursor1;
    }

    public Cursor getAllGoalsSync() {

        Cursor cursor1 = dbHelper.getReadableDatabase().rawQuery("select * from goals where _sync = 0", null);
        cursor1.moveToFirst();
        return cursor1;
    }
    public void setEventsSync(String id) {

        Cursor cursor1 = dbHelper.getReadableDatabase().rawQuery("update events set _sync = '1' where _id=? ", new String[] { id });
        cursor1.close();
    }
    public void setGoalsSync(String id) {

        Cursor cursor1 = dbHelper.getReadableDatabase().rawQuery("update goals set _sync = '1' where _id=? ",  new String[] { id });
        cursor1.close();
    }
    public String getSettingStatus(String item) {

        Cursor cursor1 = dbHelper.getReadableDatabase().rawQuery("select * from Settings where _item=? ",  new String[] { item });
        if (cursor1.moveToFirst()) {
            Cat = cursor1.getString(cursor1.getColumnIndex("_status"));
        }
        cursor1.close();
        return Cat;
    }
    public void setSettingStatus(String item,String status) {

        Cursor cursor1 = dbHelper.getReadableDatabase().rawQuery("update Settings set _status=? where _item=?",  new String[] {status,item });
        cursor1.moveToFirst();
        cursor1.close();
    }
    public Cursor getAllEventsdate(String startdate,String enddate) {

        Cursor cursor1 = dbHelper.getReadableDatabase().rawQuery("select _id,_name,_upid,_cat || ' '  || _name || ' '  ||  _desc as _cat,_desc,_date,_starttime,_endtime,_active,(strftime('%s',_endtime) - strftime('%s',_starttime))/60 as diftime  from Events where _date >= ? and _date <= ? and diftime!=0  ORDER BY _date, _starttime ASC", new String[] { startdate,enddate });
        cursor1.moveToFirst();
        return cursor1;
    }
    public Integer getSactivityDate(String upid, String cdate) {

        int dayamount=0;
             Cursor cursor1 = dbHelper.getReadableDatabase().rawQuery("select _date,_id,_starttime,_endtime,(strftime('%s',_endtime) - strftime('%s',_starttime))/60 AS diftime from Events WHERE _upid = ?", new String[] { upid });
        cursor1.moveToFirst();
        for (cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext()) {

            if(cursor1.getString(0).equals(cdate))dayamount=dayamount+ cursor1.getInt(4);


            upid=Integer.toString(cursor1.getInt(1));
            Cursor cursor2 = dbHelper.getReadableDatabase().rawQuery("select _date,_id,_starttime,_endtime,(strftime('%s',_endtime) - strftime('%s',_starttime))/60 AS diftime from Events WHERE _upid = ? ", new String[] { upid });
            cursor2.moveToFirst();
            for (cursor2.moveToFirst(); !cursor2.isAfterLast(); cursor2.moveToNext()) {

                if(cursor2.getString(0).equals(cdate))dayamount=dayamount+ cursor2.getInt(4);

                upid=Integer.toString(cursor2.getInt(1));
                Cursor cursor3 = dbHelper.getReadableDatabase().rawQuery("select _date,_id,_starttime,_endtime,(strftime('%s',_endtime) - strftime('%s',_starttime))/60 AS diftime from Events WHERE _upid = ? ", new String[] { upid });
                cursor3.moveToFirst();
                for (cursor3.moveToFirst(); !cursor3.isAfterLast(); cursor3.moveToNext()) {

                    if(cursor3.getString(0).equals(cdate))dayamount=dayamount+ cursor3.getInt(4);




                }
                cursor3.close();


            }
            cursor2.close();
        }
        cursor1.close();






        return dayamount;
    }



    public Cursor getAllCats() {

        Cursor cursor1 = dbHelper.getReadableDatabase().rawQuery("select _id,_name,_upid,_cat || ' '  || _name || ' '  ||  _desc as _cat,_desc,_date,_starttime,_endtime,_active,(strftime('%s',_endtime) - strftime('%s',_starttime))/60 as diftime  from Events where _name != ? and _id != 1 ", new String[] { "" });
        cursor1.moveToFirst();
        return cursor1;
    }



    private Event parseEvent(Cursor cursor) {
        Event eventn = new Event();
        eventn.setId((cursor.getInt(0)));
        eventn.setName(cursor.getString(1));

        eventn.setUpid(cursor.getInt(2));

        eventn.setDesc(cursor.getString(3));

        eventn.setDate(cursor.getString(4));

        eventn.setStarttime(cursor.getString(5));

        eventn.setEndtime(cursor.getString(6));

        return eventn;
    }
    private Event parseEventid(Cursor cursor) {
        Event eventn = new Event();
        eventn.setId((cursor.getInt(0)));
        eventn.setName(cursor.getString(1));
        return eventn;
    }
}