/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;


import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;


import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static android.media.CamcorderProfile.get;
import static java.util.Calendar.AM;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>> {
    private boolean isConnected;
    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private ArrayList<Earthquake> earthquakes;
    private static final String USGS_REQUEST_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query";
    private QuakeAdapter adapter;
    private TextView mEmptyStateTextView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        ConnectivityManager connMgr = (ConnectivityManager)
                             getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);
                      // If there is a network connection, fetch data
                              if (networkInfo != null && networkInfo.isConnected()) {
                                  // Create a fake list of earthquake locations.
                                  earthquakes = new ArrayList<>();

                                  // Find a reference to the {@link ListView} in the layout

                                  // Create a new {@link ArrayAdapter} of earthquakes
                                  adapter = new QuakeAdapter(this, earthquakes);

                                  // Set the adapter on the {@link ListView}
                                  // so the list can be populated in the user interface
                                  earthquakeListView.setAdapter(adapter);
                                  //new EarthquakeAsync().execute(USGS_REQUEST_URL);
                                  LoaderManager loaderManager = getLoaderManager();
                                  Log.v("PRITHIVI", "before LOADING !!");
                                  // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                                  // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                                  // because this activity implements the LoaderCallbacks interface).
                                  loaderManager.initLoader(1, null, this);


                                  earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      @Override
                                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                          Earthquake data = earthquakes.get(position);
                                          String urlData = data.getUrl();
                                          Uri webpage = Uri.parse(urlData);
                                          Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                                          if (intent.resolveActivity(getPackageManager()) != null) {
                                              startActivity(intent);
                                          }
                                      }
                                  });
                              }else{
                                  View loadingIndicator = findViewById(R.id.loading_spinner);
                                  loadingIndicator.setVisibility(View.GONE);
                                  mEmptyStateTextView.setText("NO INTERNET!!");
                              }
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
                Log.v("PRITHIVI","onCreateLoader");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data) {
        Log.v("PRITHIVI","Loader FINISHER");
        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);
        if(!isConnected){
            mEmptyStateTextView.setText("No Internet Connection!!");
        }
        //mEmptyStateTextView.setText("NO Earthquake data, Sorry!");
        adapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);


        }

    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        Log.v("PRITHIVI","LoaderReset");
        adapter.clear();
    }




}
