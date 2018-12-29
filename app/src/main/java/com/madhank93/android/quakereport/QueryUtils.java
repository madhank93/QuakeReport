package com.madhank93.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QueryUtils {

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * Query the USGS dataset and return an {@link EarthQuake} object to represent a ArrayList of earthquake.
     */
    public static ArrayList<EarthQuake> fetchEarthquakeData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        jsonResponse = makeHttpRequest(url);

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<EarthQuake> earthquake = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return earthquake;
    }


    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    public static String makeHttpRequest(URL url){

        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        if (url == null){
            return jsonResponse;
        }

        try {
            // Setting up the http request
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);

            // Establishing a connection
            urlConnection.connect();

            // If the request was successful (response code 200)
            // then read and parse the response.
            // Else log the Error response code.
            if (urlConnection.getResponseCode() == 200) {
                // Receiving the response
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else {
                Log.e(LOG_TAG, "Error response code:" + urlConnection.getResponseCode());
            }
        }
        // Catch the IOException and log it.
        catch (IOException e) {
            Log.e(LOG_TAG, "Problem in making http request", e);
        }
        // Close all the open resources (urlConnection and inputStream)
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Problem in closing input stream connection", e);
                }
            }
        }

        // return the JSON response
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = null;
            try {
                line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem in reading the input stream", e);
            }

        }
        return output.toString();

    }

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link EarthQuake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<EarthQuake> extractFeatureFromJson(String earthQuakeJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthQuakeJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<EarthQuake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            JSONObject jsonRootObject = new JSONObject(earthQuakeJSON);

            JSONArray featuresArray = jsonRootObject.optJSONArray("features");

            // For every values in featuresArray
            for (int i=0; i <featuresArray.length(); i++) {

                JSONObject featuresObject = featuresArray.optJSONObject(i);
                JSONObject propertiesObject = featuresObject.optJSONObject("properties");

                double magnitude = propertiesObject.optDouble("mag");
                String place = propertiesObject.optString("place");
                long timeInMilliseconds = propertiesObject.optLong("time");
                String url = propertiesObject.optString("url");

                earthquakes.add(new EarthQuake(magnitude, place, timeInMilliseconds,url));
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }


}
