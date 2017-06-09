package com.abroscreative.weatherinformant.network;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.abroscreative.weatherinformant.network.NetworkTaskUtils;
import com.abroscreative.weatherinformant.ui.MainActivity;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class WeatherDownloadService extends IntentService {

    public static final String KEY_BROADCAST ="key_broadcast" ;
    String mDownloadedResponseStr;
    private String mActionReceived;


    public WeatherDownloadService() {
        super("WeatherDownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                //Receiving the location or latitude and longitude from the client ie our MainActivity
                String location = extras.getString(MainActivity.KEY_LOCATION);
                double latRecieved = extras.getDouble(MainActivity.KEY_LATITUDE, 0);
                double longRecieved = extras.getDouble(MainActivity.KEY_LONGITUDE, 0);
                mActionReceived = intent.getAction();
                Uri uriBuilt= null;

                if (mActionReceived.equals(MainActivity.ACTION_FORECAST)) {
                    if(!TextUtils.isEmpty(location)){

                        uriBuilt = NetworkTaskUtils.setupForecastUriWithLocation(location);

                    }else if(latRecieved != 0 && longRecieved!= 0){
                         uriBuilt = NetworkTaskUtils.setupForecastUriWithLatLng(latRecieved, longRecieved);
                    }
                }
                if (mActionReceived.equals(MainActivity.ACTION_CURRENT)) {
                    if (!TextUtils.isEmpty(location)) {
                        uriBuilt = NetworkTaskUtils.setupCurrentWeatherUriWithLocation(location);
                    }
                    else if(latRecieved != 0 && longRecieved!= 0){
                        uriBuilt = NetworkTaskUtils.setupCurrentWeatherUriWithLatLng(latRecieved, longRecieved);
                    }
                }
                Log.i("SERVICE_URL",uriBuilt.toString());

                    mDownloadedResponseStr = NetworkTaskUtils.fetchWeatherData(uriBuilt.toString());


            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("LOGGER", "onDestroy happened with action of "+mActionReceived);
        sendBroadcast(new Intent()
                .setAction(mActionReceived)
                .putExtra(KEY_BROADCAST, mDownloadedResponseStr));
    }
}
