package com.abroscreative.weatherinformant;

import android.util.Log;

import com.abroscreative.weatherinformant.model.CurrentWeatherItem;
import com.abroscreative.weatherinformant.model.ForecastDayItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by David on 6/9/2017.
 */

public class JSONParser {

    public static CurrentWeatherItem processCurrentResponseJSON(String responseString) {
        try {
            JSONObject jsonRootObject = new JSONObject(responseString);
            JSONArray weather = jsonRootObject.getJSONArray("weather") ;
            String description = weather.getJSONObject(0).getString("description");
            String iconUrl = weather.getJSONObject(0).getString("icon");
            JSONObject mainJSONObject = jsonRootObject.getJSONObject("main");
            double temperature = mainJSONObject.getDouble("temp");
            double pressure = mainJSONObject.getDouble("pressure");
            double humidity = mainJSONObject.getDouble("humidity");
            JSONObject sysJSONObject = jsonRootObject.getJSONObject("sys");
            String country = sysJSONObject.getString("country");
            String cityName = jsonRootObject.getString("name");
            String location = cityName.concat(", ").concat(country);

            CurrentWeatherItem currentWeatherItem = new CurrentWeatherItem(iconUrl,temperature, humidity,pressure,description,location);
            return currentWeatherItem;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<ForecastDayItem> processForecastResponseJSON(String responseString, List<ForecastDayItem> forecastDayItems) {
        try {
            JSONObject jsonRootObject = new JSONObject(responseString);
            JSONArray jsonList = jsonRootObject.getJSONArray("list");

            //clearing the list in case there were items inside it already
            if (!forecastDayItems.isEmpty()) {
                forecastDayItems.clear();
            }
            for (int i = 0; i < jsonList.length(); i++) {
                JSONObject eachJsonItem = jsonList.getJSONObject(i);
                JSONObject eachMainSubItem = eachJsonItem.getJSONObject("main");
                double maxTemp = eachMainSubItem.getDouble("temp_max");
                double minTemp = eachMainSubItem.getDouble("temp_min");
                int humidity = eachMainSubItem.getInt("humidity");
                forecastDayItems.add(new ForecastDayItem(maxTemp, minTemp, humidity));
            }
            return forecastDayItems;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR_JSON", e.getMessage());
        }

        return forecastDayItems;
    }
}
