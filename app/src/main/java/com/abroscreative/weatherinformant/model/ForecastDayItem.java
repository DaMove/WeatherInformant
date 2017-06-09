package com.abroscreative.weatherinformant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by David on 6/6/2017.
 */

public class ForecastDayItem implements Parcelable {
    private double tempMax, tempMin;
    private int humidity;

    public ForecastDayItem() {

    }

    public ForecastDayItem(double tempMax, double tempMin, int humidity) {
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.humidity = humidity;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.tempMax);
        dest.writeDouble(this.tempMin);
        dest.writeInt(this.humidity);
    }

    protected ForecastDayItem(Parcel in) {
        this.tempMax = in.readDouble();
        this.tempMin = in.readDouble();
        this.humidity = in.readInt();
    }

    public static final Parcelable.Creator<ForecastDayItem> CREATOR = new Parcelable.Creator<ForecastDayItem>() {
        @Override
        public ForecastDayItem createFromParcel(Parcel source) {
            return new ForecastDayItem(source);
        }

        @Override
        public ForecastDayItem[] newArray(int size) {
            return new ForecastDayItem[size];
        }
    };
}
