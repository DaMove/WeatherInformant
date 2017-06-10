package com.abroscreative.weatherinformant.ui;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.abroscreative.weatherinformant.JSONParser;
import com.abroscreative.weatherinformant.model.CurrentWeatherItem;
import com.abroscreative.weatherinformant.model.ForecastDayItem;
import com.abroscreative.weatherinformant.network.NetworkTaskUtils;
import com.abroscreative.weatherinformant.R;
import com.abroscreative.weatherinformant.ui.ui.settings.SettingsActivity;
import com.abroscreative.weatherinformant.network.WeatherDownloadService;
import com.abroscreative.weatherinformant.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    public static final String KEY_LATITUDE = "key_latitude";
    public static final String KEY_LONGITUDE = "key_longitude";
    public static final String KEY_LOCATION = "key_location";

    public static final String ACTION_CURRENT="action_current_weather";
    public static final String ACTION_FORECAST="action_forecast_weather";

    ActivityMainBinding mMainBinding; //a reference for binding our UI widgets using dataBinding api

    LocationManager mLocationManager;//to manage and launch location service to access device's coordinates
    double mLatitude, mLongitude;// to store location coordinates
    String mCityAndCountry;
    SharedPreferences mSharedPreferences;

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String resultStr = intent.getExtras().getString(WeatherDownloadService.KEY_BROADCAST);
            String actionReturned = intent.getAction();
            Log.i("TAG", "\n\n" + resultStr + "\n\nACTION"+actionReturned);

            if (actionReturned.equals(ACTION_CURRENT)) {
                Log.i("ACTION:","Current");
                mCurrentWeatherItem = JSONParser.processCurrentResponseJSON(resultStr);
                CurrentWeatherFragment currentWeatherFragment = CurrentWeatherFragment.newInstance(mCurrentWeatherItem);
                showFragment(currentWeatherFragment);

            } else if (actionReturned.equals(ACTION_FORECAST)) {
                Log.i("ACTION:","Forecast");
                mForecastDayItemList=JSONParser.processForecastResponseJSON(resultStr, mForecastDayItemList);
                //mMainBinding.linearLayoutTitle.setVisibility(View.VISIBLE);
                ForecastFragment forecastFragment = ForecastFragment.newInstance((ArrayList<ForecastDayItem>) mForecastDayItemList);
                showFragment(forecastFragment);


            }
            mMainBinding.linearProgressBar.setVisibility(View.INVISIBLE);



        }
    };

    private void showFragment(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction().replace(R.id.container_for_fragment, fragment )
                .addToBackStack("Forecast_List_Fragment")
                .commit();
    }

    IntentFilter mIntentFilter;
    List<ForecastDayItem> mForecastDayItemList;
    CurrentWeatherItem mCurrentWeatherItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);//inflating layout to initialize our dataBining reference

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        updateLocationName(mSharedPreferences);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);


        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.getProvider(LocationManager.GPS_PROVIDER);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(ACTION_CURRENT);
        mIntentFilter.addAction(ACTION_FORECAST);

        mForecastDayItemList = new ArrayList<>();


    }




    @Override
    protected void onStart() {
        super.onStart();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();


        registerReceiver(mReceiver, mIntentFilter);
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mLocationManager = null;
        }

        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);


    }

    public void onClickFindCoordinates(View view) {

        mCityAndCountry = mMainBinding.eTLocationEntry.getText().toString();
        if (mCityAndCountry != null && !mCityAndCountry.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            List<Address> addressList;
            Address address;


            try {
                addressList = geocoder.getFromLocationName(mCityAndCountry, 1);
                if (addressList.size() > 0) {
                    address = addressList.get(0);
                    mLatitude = address.getLatitude();
                    mLongitude = address.getLongitude();
                    Toast.makeText(this,
                            "location found\n" +
                                    "Latitude: " + mLatitude + "\n" +
                                    "Longitude: " + mLongitude, Toast.LENGTH_LONG).show();
                    dismissKeyboard(mMainBinding.eTLocationEntry);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            showErrorMessageDialog(R.string.error_msg_location_entry);
        }
    }

    private void showErrorMessageDialog(int errorMsgResourceId) {
        new AlertDialog.Builder(this)
                .setTitle("Location Entry Error")
                .setMessage(errorMsgResourceId)
                .create()
                .show();
    }

    public void dismissKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void launchSettings(MenuItem item) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void fetchForecastUsingLatlng(MenuItem item) {
        mMainBinding.linearProgressBar.setVisibility(View.VISIBLE);
        requestUsingLatLng(ACTION_FORECAST);
    }

    private void requestUsingLatLng(String action) {
        if (NetworkTaskUtils.isConnected(this)) {
            startService(
                    new Intent(this, WeatherDownloadService.class)
                            .setAction(action)
                            .putExtra(KEY_LATITUDE, mLatitude)
                            .putExtra(KEY_LONGITUDE, mLongitude)

            );
        } else {
            Toast.makeText(this, "Cannot connect to the network.\nCheck your network access settings before you try again.", Toast.LENGTH_SHORT).show();
        }
    }

    public void fetchForecastUsingLocation(MenuItem item) {
        mMainBinding.linearProgressBar.setVisibility(View.VISIBLE);
        requestUsingLocation(ACTION_FORECAST);
    }

    public void fetchCurrentWeather(View v){
        String[]items = {"Fetch current weather using Coordinates", "Fetch current weather using city, country"};
        new AlertDialog.Builder(this)
                .setTitle("Current Weather Selection")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mMainBinding.linearProgressBar.setVisibility(View.VISIBLE);
                        if(i==0){
                            requestUsingLatLng(ACTION_CURRENT);
                        }else{
                            requestUsingLocation(ACTION_CURRENT);
                        }
                    }
                })
                .create()
                .show();
    }

    private void requestUsingLocation(String action) {
        if (NetworkTaskUtils.isConnected(this)) {
            mCityAndCountry = mMainBinding.eTLocationEntry.getText().toString();
            if (!TextUtils.isEmpty(mCityAndCountry)) {
                String location = mCityAndCountry.split(", ")[0];
                startService(
                        new Intent(this, WeatherDownloadService.class)
                                .setAction(action)
                                .putExtra(KEY_LOCATION, location)

                );
            }else{
                Toast.makeText(this, "Make sure you have entered a valid city, country before you try this", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Cannot connect to the network.\nCheck your network access settings before you try again.", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendNotification(String title, String content) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setSmallIcon(android.R.drawable.stat_notify_chat);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_notification_overlay));

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(this).addNextIntent(resultIntent);
        PendingIntent pending = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pending);
        Notification notification = builder.build();

        notificationManager.notify(0, notification);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        updateLocationName(sharedPreferences);
    }

    private void updateLocationName(SharedPreferences sharedPreferences) {
        mCityAndCountry  = sharedPreferences.getString("key_location_name", "Atlanta, USA");
        mMainBinding.eTLocationEntry.setText(mCityAndCountry);
    }


}
