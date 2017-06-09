package com.abroscreative.weatherinformant.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by David on 6/5/2017.
 */

public class NetworkTaskUtils {

    private static final String BASE_FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast" ;
    private static final String BASE_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather" ;//for the current weather

    /* The format we want our API to return */
    private static final String format = "json";
    /* The units we want our API to return */
    private static final String units = "metric";
    /* The number of days we want our API to return */
    private static final int numDays = 7;
    private static final String apiKey = "b2b15094b7727c5a9804df1deffb277d";

    /* The query parameter allows us to provide a location string to the API */
    private static final String PARAM_QUERY = "q";

    private static final String PARAM_LAT = "lat";
    private static final String PARAM_LONG = "lon";
    private static final String PARAM_API_KEY= "APPID";

    /* The format parameter allows us to designate whether we want JSON or XML from our API */
    private static final String PARAM_FORMAT = "mode";
    /* The units parameter allows us to designate whether we want metric units or imperial units */
    private static final String PARAM_UNITS = "units";
    /* The days parameter allows us to designate how many days of weather data we want */
    private static final String PARAM_DAYS = "cnt";

    public static Uri setupForecastUriWithLatLng(double latitude, double longitude) {
        Uri resultUri = Uri.parse(BASE_FORECAST_URL).buildUpon()
                .appendQueryParameter(PARAM_LAT, latitude+"")
                .appendQueryParameter(PARAM_LONG, longitude+"")
                .appendQueryParameter(PARAM_FORMAT, format)
                .appendQueryParameter(PARAM_UNITS, units)
                .appendQueryParameter(PARAM_DAYS, numDays+"")
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .build();
        return resultUri;
    }

    public static Uri setupForecastUriWithLocation(String locationName) {
        Uri resultUri = Uri.parse(BASE_FORECAST_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, locationName)
                .appendQueryParameter(PARAM_FORMAT, format)
                .appendQueryParameter(PARAM_UNITS, units)
                .appendQueryParameter(PARAM_DAYS, Integer.toString(numDays))
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .build();
         return resultUri;
    }

    public static String fetchWeatherData(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine())!= null) {
                sb.append(line).append("\n");
            }
            urlConnection.disconnect();
            reader.close();
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Error\nThis url: "+urlStr+" is problematic";
    }

    public static Uri setupCurrentWeatherUriWithLocation(String locationName) {
        Uri resultUri = Uri.parse(BASE_WEATHER_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, locationName)
                .appendQueryParameter(PARAM_FORMAT, format)
                .appendQueryParameter(PARAM_UNITS, units)
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .build();

        return resultUri;

    }

    public static Uri setupCurrentWeatherUriWithLatLng(double latitude, double longitude) {
        Uri resultUri = Uri.parse(BASE_WEATHER_URL).buildUpon()
                .appendQueryParameter(PARAM_LAT, latitude+"")
                .appendQueryParameter(PARAM_LONG, longitude+"")
                .appendQueryParameter(PARAM_FORMAT, format)
                .appendQueryParameter(PARAM_UNITS, units)
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .build();
        return resultUri;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!= null && networkInfo.isConnected()){
            return true;
        }

        return false;
    }
}
