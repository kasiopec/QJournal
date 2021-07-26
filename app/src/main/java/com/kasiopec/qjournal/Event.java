package com.kasiopec.qjournal;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by admin on 08-04-2016.
 */
public class Event {

    private int _id;
    private String _name;
    private int _upid;
    private String _desc;
    private String _date;
    private String _starttime;
    private String _endtime;
    private int _active;


    public Event() {
        this._id = _id;
        this._name = _name;
    }

    // constructor
    public Event(int _id, String name) {
        this._id = _id;
        this._name = name;
    }

    public long getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public int getUpid() {
        return this._upid;
    }

    public void setUpid(int upid) {
        this._upid = upid;
    }


    public String getDesc() {
        return this._desc;
    }

    public void setDesc(String desc) {
        this._desc = desc;
    }


    public String getDate() {
        return this._date;
    }

    public void setDate(String date) {
        this._date = date;
    }

    public String getStarttime() {
        return this._starttime;
    }

    public void setStarttime(String starttime) {
        this._starttime = starttime;
    }

    public String getEndtime() {
        return this._endtime;
    }

    public void setEndtime(String endtime) {
        this._endtime = endtime;
    }

    public int getActive() {
        return this._active;
    }

    public void setActive(int active) {
        this._active = active;
    }


   /* public String[] foo() {
        return new String[]{Integer.toString(_id), _name};
    }

    public String toString(){
    return _name;
}
*/

}