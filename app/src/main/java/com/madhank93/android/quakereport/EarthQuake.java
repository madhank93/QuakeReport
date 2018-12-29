package com.madhank93.android.quakereport;

public class EarthQuake {

    public double getEarthQuakeScale() {
        return earthQuakeScale;
    }

    public void setEarthQuakeScale(double earthQuakeScale) {
        this.earthQuakeScale = earthQuakeScale;
    }

    public String getLocation() {
        return location;
    }

    public void setCity(String city) {
        this.location = city;
    }

    public long getTimeInMilliseconds() {
        return timeInMilliseconds;
    }

    public void setTimeInMilliseconds(long date) {
        this.timeInMilliseconds = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private double earthQuakeScale;
    private String location;
    private long timeInMilliseconds;
    private String url;

    public EarthQuake(double earthQuakeScale, String city, long timeInMilliseconds, String url){
        this.earthQuakeScale = earthQuakeScale;
        this.location = city;
        this.timeInMilliseconds = timeInMilliseconds;
        this.url = url;
    }

}
