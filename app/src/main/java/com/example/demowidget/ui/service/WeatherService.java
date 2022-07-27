package com.example.demowidget.ui.service;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import com.example.demowidget.R;
import com.example.demowidget.data.client.OnDataChange;
import com.example.demowidget.data.model.CurrentWeather;
import com.example.demowidget.data.model.DirectGeocoding;
import com.example.demowidget.data.repository.CurrentWeatherRepository;
import com.example.demowidget.data.source.impl.CurrentWeatherSourceImpl;
import com.example.demowidget.data.source.impl.GeocodingSourceImpl;
import com.example.demowidget.ui.WeatherActivity;
import com.example.demowidget.ui.WeatherWidget;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Nguyen Tuan Anh on 27/07/2022.
 * FPT Software
 * tuananhprogrammer@gmail.com
 */
public class WeatherService extends Service {

    public static final String ACTION_REQUEST_DATA = "ACTION_REQUEST_DATA";

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(ACTION_REQUEST_DATA);

        this.registerReceiver(new ServiceReceiver(), filter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String data = intent.getStringExtra(WeatherWidget.REQUEST_DATA_KEY);
        if(data != null) {
            fetchData(getBaseContext(), intent);
        }
        return START_REDELIVER_INTENT;
    }

    public class ServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            fetchData(context, intent);
        }
    }

    private void fetchData(Context context, Intent intent) {
        Location currentLocation = getLocation(context);
        CurrentWeatherRepository
                .getInstance(
                        CurrentWeatherSourceImpl.getInstance(),
                        GeocodingSourceImpl.getInstance()
                )
                .getCurrentWeather(new OnDataChange<CurrentWeather>() {
                    @Override
                    public void onResponse(Call<CurrentWeather> call,
                                           Response<CurrentWeather> response) {
                        sendDataToWidget(context, response.body());
                    }
                    @Override
                    public void onFailure(Call<CurrentWeather> call, Throwable t) {
                        t.printStackTrace();
                    }
                }, currentLocation.getLatitude(), currentLocation.getLongitude());

    }

    private void sendDataToWidget(Context context, CurrentWeather currentWeather) {
        AppWidgetManager appWidgetManager =  AppWidgetManager.getInstance(context);
        AppWidgetHost mAppWidgetHost = new AppWidgetHost(this, R.id.APPWIDGET_HOST_ID);

        Intent nextIntent = new Intent(context, WeatherWidget.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(WeatherWidget.DATA_WEATHER_KEY, currentWeather);
        nextIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        nextIntent.putExtras(bundle);
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, WeatherWidget.class));
        if(ids != null && ids.length > 0) {
            nextIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            context.sendBroadcast(nextIntent);
        }
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
//        appWidgetManager.updateAppWidget(mAppWidgetHost.allocateAppWidgetId(), views);
    }

    @SuppressLint("MissingPermission")
    private Location getLocation(Context context) {
        boolean gps_enabled = false;
        boolean network_enabled = false;

        LocationManager locationManager = (LocationManager)context
                .getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location net_loc = null, gps_loc = null, finalLoc = null;

        if (gps_enabled) {
            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (network_enabled) {
            net_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (gps_loc != null && net_loc != null) {
            if (gps_loc.getAccuracy() > net_loc.getAccuracy())
                finalLoc = net_loc;
            else
                finalLoc = gps_loc;
        } else {

            if (gps_loc != null) {
                finalLoc = gps_loc;
            } else if (net_loc != null) {
                finalLoc = net_loc;
            }
        }
        return finalLoc;
    }
}
