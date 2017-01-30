package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by HP-PC on 23-01-2017.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>>
{
    String mUrl;
    protected void onStartLoading() {
        forceLoad();
    }
    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }
    @Override
    public List<Earthquake> loadInBackground() {

        return QueryUtils.extractEarthquakes(mUrl);

    }
}
