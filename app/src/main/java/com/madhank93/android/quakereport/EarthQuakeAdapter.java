package com.madhank93.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.graphics.drawable.GradientDrawable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

public class EarthQuakeAdapter extends ArrayAdapter<EarthQuake> {

    private static final String LOG_TAG = EarthQuakeAdapter.class.getSimpleName();

    public EarthQuakeAdapter(Activity context, ArrayList<EarthQuake> EarthQuake) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, EarthQuake);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        EarthQuake currentEarthQuake = getItem(position);

        TextView earthQuakeTextView = (TextView) listItemView.findViewById(R.id.earth_quake_scale);
        String earthQuakeScale = formatMagnitude(currentEarthQuake.getEarthQuakeScale());
        earthQuakeTextView.setText(earthQuakeScale);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) earthQuakeTextView.getBackground();
        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthQuake.getEarthQuakeScale());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        String locationDistance;
        String locationName;

        if (currentEarthQuake.getLocation().contains("of")){
            // Split a string AFTER a certain word here it's "of"
            String[] locationDetails = (currentEarthQuake.getLocation()).split("(?<=of)");

            locationDistance = locationDetails[0];
            locationName = locationDetails[1];
        } else {
            locationDistance = "Near the";
            locationName = currentEarthQuake.getLocation();
        }

        TextView distanceTextView = (TextView) listItemView.findViewById(R.id.distance);
        distanceTextView.setText(locationDistance);

        TextView cityTextView = (TextView) listItemView.findViewById(R.id.city);
        cityTextView.setText(locationName);

        // Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject = new Date(currentEarthQuake.getTimeInMilliseconds());

        // Find the TextView with view ID date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(dateObject);
        // Display the date of the current earthquake in that TextView
        dateView.setText(formattedDate);

        // Find the TextView with view ID time
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(dateObject);
        // Display the time of the current earthquake in that TextView
        timeView.setText(formattedTime);

        return listItemView;
    }

    private int getMagnitudeColor(double earthQuakeScale) {

        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(earthQuakeScale);

        switch (magnitudeFloor) {

            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;

        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    /**
     * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
     * from a decimal magnitude value.
     */
    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

}
