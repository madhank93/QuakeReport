package com.madhank93.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class EarthQuakeLoader extends AsyncTaskLoader<List<EarthQuake>> {

    String url;

    public EarthQuakeLoader(Context context,String url) {
        super(context);
        this.url = url;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    // Handling background job
    @Override
    public List<EarthQuake> loadInBackground() {

        // Don't perform the request if there are no URLs, or the first URL is null.
        if (url == null) {
            return null;
        }

        List<EarthQuake> earthquakes = QueryUtils.fetchEarthquakeData(url);

        return earthquakes;
    }

}
