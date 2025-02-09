package com.kasiopec.qjournal.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 Goal class that represents goals in the app
 **/


public class Goal implements Parcelable {

    private String name;
    private String category;
    private String timeFrame;
    private int goalTime;
    private int currentTime;
    private String startDate;
    private String endDate;


    private int id;


    public Goal(){

    }
    /*
    public Goal (String name, String category, String timeFrame, int goalTime,
                 String startDate){
        this.name = name;
        this.category = category;
        this.timeFrame = timeFrame;
        this.goalTime = goalTime;
        this.startDate = startDate;
    }
    */

    public static final Creator<Goal> CREATOR = new Creator<Goal>() {
        @Override
        public Goal createFromParcel(Parcel in) {
            return new Goal(in);
        }

        @Override
        public Goal[] newArray(int size) {
            return new Goal[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }

    public int getGoalTime() {
        return goalTime;
    }

    public void setGoalTime(int goalTime) {
        this.goalTime = goalTime;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(goalTime);
        dest.writeInt(currentTime);
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(startDate);
        dest.writeString(endDate);
    }

    public Goal (Parcel in){
        this.id = in.readInt();
        this.goalTime = in.readInt();
        this.currentTime = in.readInt();
        this.name = in.readString();
        this.category = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
    }
}
