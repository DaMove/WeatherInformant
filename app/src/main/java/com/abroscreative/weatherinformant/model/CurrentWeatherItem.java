package com.abroscreative.weatherinformant.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by David on 6/8/2017.
 */

public class CurrentWeatherItem implements Parcelable {//our Data Model
    private String mIcon;

    private double mTemperature;
    private double mHumidity;
    private double mPressure;
    private String description;
    private String mLocation;

    private String mTimezone;
    private long mTime;

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public double getPressure() {
        return mPressure;
    }

    public void setPressure(double pressure) {
        mPressure = pressure;
    }
    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String mTimezone) {
        this.mTimezone = mTimezone;
    }

    public CurrentWeatherItem(String icon, long time, double temperature, double humidity, double pressure, String description, String timeZone) {
        this.mIcon = icon;
        this.mTime = time;
        this.mTemperature = temperature;
        this.mHumidity = humidity;
        this.mPressure = pressure;
        this.description = description;
        this.mTimezone = timeZone;
    }
    public CurrentWeatherItem(String icon, double temperature, double humidity, double pressure, String description, String location) {
        this.mIcon = icon;
        this.mTemperature = temperature;
        this.mHumidity = humidity;
        this.mPressure = pressure;
        this.description = description;
        mLocation = location;
    }

    public CurrentWeatherItem() {

    }

    public String getFormattedTime(){
        /*
        The Pattern (check out the docs)
        1 h    gives you the hour without the leading zero  eg 2 v hh would gave you say 02 for 2 oclock
        mm     because we'd always want the leading zero digit included for minutes
        a      would return am or pm
         */
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");

        //note we need to the set the time appropriately with the TimeZone in mind
        formatter.setTimeZone(TimeZone.getTimeZone(mTimezone));
        Date dateTime = new Date(mTime*1000); //Date constructor expects millisecs and our time is in sec so to get in ms we seconds *1000
        String timeString = formatter.format(dateTime);
        return timeString;
    }

//    public int getIconId(){
//
//        return getIconId(mIcon);
//    }
/*
    public static int getIconId(String iconString){
        int iconId;

        switch (iconString){
            //clear-day, clear-night, rain, snow, sleet
            case "clear sky":
                iconId = R.drawable.clear_sky;
                break;
            case "clear-night":
                iconId = R.drawable.clear_night;
                break;
            case "rain":
                iconId = R.drawable.rain;
                break;
            case "snow":
                iconId = R.drawable.snow;
                break;
            case "sleet":
                iconId = R.drawable.sleet;
                break;
            //wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night
            case "wind":
                iconId = R.drawable.wind;
                break;
            case "cloudy":
                iconId = R.drawable.cloudy;
                break;
            case "partly-cloudy-day":
                iconId = R.drawable.partly_cloudy;
                break;

            case "partly-cloudy-night":
                iconId = R.drawable.cloudy_night;
                break;
            default:
                iconId=R.drawable.clear_day;

        }

        return iconId;
    }
*/
    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        this.mIcon = icon;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        this.mTime = time;
    }

    public int getTemperature() {
        return (int) Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        this.mTemperature = temperature;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        this.mHumidity = humidity;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mIcon);
        dest.writeDouble(this.mTemperature);
        dest.writeDouble(this.mHumidity);
        dest.writeDouble(this.mPressure);
        dest.writeString(this.mLocation);
        dest.writeString(this.description);
        dest.writeString(this.mTimezone);
        dest.writeLong(this.mTime);
    }

    protected CurrentWeatherItem(Parcel in) {
        this.mIcon = in.readString();
        this.mTemperature = in.readDouble();
        this.mHumidity = in.readDouble();
        this.mPressure = in.readDouble();
        this.mLocation = in.readString();
        this.description = in.readString();
        this.mTimezone = in.readString();
        this.mTime = in.readLong();
    }

    public static final Parcelable.Creator<CurrentWeatherItem> CREATOR = new Parcelable.Creator<CurrentWeatherItem>() {
        @Override
        public CurrentWeatherItem createFromParcel(Parcel source) {
            return new CurrentWeatherItem(source);
        }

        @Override
        public CurrentWeatherItem[] newArray(int size) {
            return new CurrentWeatherItem[size];
        }
    };
}
