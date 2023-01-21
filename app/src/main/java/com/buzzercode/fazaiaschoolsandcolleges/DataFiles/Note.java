package com.buzzercode.fazaiaschoolsandcolleges.DataFiles;

public class Note {
    private String mHeading;
    private String mDescription;
    private String mTime;
    private String mPriority;
    private long mTimeStamp;
    private String mId;

    public Note(String heading, String description, String time, String priority,long timestamp,String id) {
        mDescription = description;
        mHeading = heading;
        mTime = time;
        mPriority = priority;
        mTimeStamp=timestamp;
        mId=id;
    }

    public String getmHeading() {
        return mHeading;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmTime() {
        return mTime;
    }

    public String getmPriority() {
        return mPriority;
    }

    public long getmTimeStamp() {
        return mTimeStamp;
    }

    public String getmId() {
        return mId;
    }

    public void setmHeading(String mHeading) {
        this.mHeading = mHeading;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public void setmPriority(String mPriority) {
        this.mPriority = mPriority;
    }

    public void setmTimeStamp(long mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }



}
