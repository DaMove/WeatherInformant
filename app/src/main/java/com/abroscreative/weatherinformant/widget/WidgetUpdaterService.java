package com.abroscreative.weatherinformant.widget;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.preference.PreferenceManager;

import com.abroscreative.weatherinformant.network.NetworkTaskUtils;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class WidgetUpdaterService extends IntentService {

    public static final String KEY_WIDGET_BROADCAST = "key_widget_broadcast";
    String mDownloadedResponseStr;

    public WidgetUpdaterService() {
        super("WidgetUpdaterService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if(intent.hasExtra("KEY_WIDGET_LOCATION")){
                String location = intent.getStringExtra("KEY_WIDGET_LOCATION");
               Uri uriBuilt = NetworkTaskUtils.setupForecastUriWithLocation(location);
                mDownloadedResponseStr = NetworkTaskUtils.fetchWeatherData(uriBuilt.toString());
            }


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sendBroadcast(new Intent().putExtra(KEY_WIDGET_BROADCAST, mDownloadedResponseStr));
    }
}
