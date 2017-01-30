package com.example.android.quakereport;

/**
 * Created by HP-PC on 21-01-2017.
 */

public class Earthquake {

    private double mMagnitude;

    private String mLocation;

    private String url;



    private long mTimeInMilliseconds;

    public Earthquake(double vMagnitude, String vLocation
    , long vTimeOfOccurence, String vUrl)
    {
        mMagnitude = vMagnitude;
        mLocation = vLocation;
        mTimeInMilliseconds = vTimeOfOccurence;
        url = vUrl;
    }

    public double getmMagnitude() {
        return mMagnitude;
    }

    public String getmLocation() {
        return mLocation;
    }



    public long getmTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    public String getUrl() {
        return url;
    }
}
