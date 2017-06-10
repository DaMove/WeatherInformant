package com.abroscreative.weatherinformant.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.abroscreative.weatherinformant.JSONParser;
import com.abroscreative.weatherinformant.R;
import com.abroscreative.weatherinformant.model.CurrentWeatherItem;
import com.abroscreative.weatherinformant.network.WeatherDownloadService;
import com.abroscreative.weatherinformant.ui.MainActivity;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WeatherAppWidgetConfigureActivity WeatherAppWidgetConfigureActivity}
 */
public class WeatherAppWidget extends AppWidgetProvider {
    SharedPreferences mSharedPreferences;



    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = WeatherAppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String location = mSharedPreferences.getString("key_location_name","Atlanta, USA");

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            // Create an Intent to launch MainActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_app_widget);
           // remoteViews.setOnClickPendingIntent(R.id.layout_container_widget, pendingIntent);

            //To start the network downloading service
            Intent serviceIntent = new Intent(context, WidgetUpdaterService.class)
                   .putExtra("KEY_WIDGET_LOCATION", location);

            PendingIntent servicePendingIntent = PendingIntent.getService(context, 1,serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT );
            //context.startService(serviceIntent);
            remoteViews.setOnClickPendingIntent(R.id.layout_container_widget, servicePendingIntent);

            //Instruct the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            //updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            WeatherAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /*
    overriding this method to receive the broadcast intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String jsonReceived = intent.getStringExtra( WidgetUpdaterService.KEY_WIDGET_BROADCAST);
        CurrentWeatherItem currentWeatherItem = JSONParser.processCurrentResponseJSON(jsonReceived);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_app_widget);
        remoteViews.setTextViewText(R.id.tvDesc, currentWeatherItem.getDescription());
        remoteViews.setTextViewText(R.id.tvHumidity, currentWeatherItem.getHumidity()+"%");
        remoteViews.setTextViewText(R.id.tvLocation, currentWeatherItem.getLocation());
        remoteViews.setTextViewText(R.id.tvTemp, currentWeatherItem.getTemperature()+"");
        String iconId = currentWeatherItem.getIcon()+".png";
        Uri iconImageUri = Uri.parse("http://openweathermap.org/img/w/"+iconId);
        remoteViews.setImageViewUri(R.id.imgIcon, iconImageUri);
    }
}

