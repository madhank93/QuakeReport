package com.madhank93.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.net.URL;
import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    /** URL to query the USGS dataset for earthquake information */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-12-01&minmagnitude=7";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Creating a object for EarthQuakeAsyncTask class and executing http request, extracting JSON response
        // in a background thread
        new EarthQuakeAsyncTask().execute(USGS_REQUEST_URL);

    }


    private class EarthQuakeAsyncTask extends AsyncTask<String, Void, ArrayList<EarthQuake>> {

        // Handling background job
        @Override
        protected ArrayList<EarthQuake> doInBackground(String... urls) {

            final ArrayList<EarthQuake> earthquakes = QueryUtils.fetchEarthquakeData(urls[0]);

            return earthquakes;
        }

        /**
         * Update the screen with the given ArrayList of earthquakes object (which was the result of the
         * {@link EarthQuakeAsyncTask}).
         */
        @Override
        protected void onPostExecute(ArrayList<EarthQuake> earthquakes) {
            if (earthquakes == null) {
                return;
            }

            updateUi(earthquakes);
        }


    }

    private void updateUi(final ArrayList<EarthQuake> earthquakes) {

        // Create a new {@link ArrayAdapter} of earthquakes
        EarthQuakeAdapter quakeAdapter = new EarthQuakeAdapter(this, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(quakeAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent websiteIntent  = new Intent(Intent.ACTION_VIEW);
                websiteIntent .setData(Uri.parse(earthquakes.get(position).getUrl()));
                startActivity(websiteIntent );
            }
        });
    }


}
