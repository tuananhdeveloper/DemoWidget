package com.example.demowidget.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;

import com.example.demowidget.R;
import com.example.demowidget.data.model.DirectGeocoding;
import com.example.demowidget.databinding.ActivityWeatherBinding;
import com.example.demowidget.viewmodel.WeatherViewModel;

public class WeatherActivity extends AppCompatActivity {

    private ActivityWeatherBinding binding;
    private WeatherViewModel viewModel;
    private DirectGeocoding geocoding;

    private AppWidgetManager mAppWidgetManager;
    private AppWidgetHost mAppWidgetHost;
    private ViewGroup mainlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case R.id.APPWIDGET_HOST_ID:
                    addAppWidget(data);
                    break;
            }
        }
    }

    private void initData() {
        Intent intentMain = getIntent();
        if (intentMain != null) {
            if (intentMain.getSerializableExtra(MainActivity.EXTRA_LOCATION_DATA) != null) {
                geocoding = (DirectGeocoding) intentMain
                        .getSerializableExtra(MainActivity.EXTRA_LOCATION_DATA);

                viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
                viewModel.getCurrentWeatherData(geocoding.getLat(), geocoding.getLon())
                        .observe(this, currentWeathers -> {
                            binding.setWeatherModel(currentWeathers);

                            binding.buttonPickWidget.setOnClickListener(v -> {
                                pickWidget();
                            });
                        });
            }
        }

    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather);
        mAppWidgetManager = AppWidgetManager.getInstance(this);
        mAppWidgetHost = new AppWidgetHost(this, R.id.APPWIDGET_HOST_ID);
    }

    private void pickWidget() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ComponentName myProvider =
                    new ComponentName(this, WeatherWidget.class);
            if (mAppWidgetManager.isRequestPinAppWidgetSupported()) {
                Intent pinnedWidgetCallbackIntent = new Intent(this, MainActivity.class);
                PendingIntent successCallback = PendingIntent.getBroadcast(this, 0,
                        pinnedWidgetCallbackIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mAppWidgetManager.requestPinAppWidget(myProvider, null, successCallback);
            }
            else {
                int appWidgetId = this.mAppWidgetHost.allocateAppWidgetId();
                ComponentName myWidgetProvider =
                        new ComponentName(this, WeatherWidget.class);
                this.mAppWidgetManager.bindAppWidgetIdIfAllowed(appWidgetId, myWidgetProvider);
                Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
                pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                startActivityForResult(pickIntent, R.id.APPWIDGET_HOST_ID);
            }
        }
        else {
            int appWidgetId = this.mAppWidgetHost.allocateAppWidgetId();
            ComponentName myWidgetProvider =
                    new ComponentName(this, WeatherWidget.class);
            this.mAppWidgetManager.bindAppWidgetIdIfAllowed(appWidgetId, myWidgetProvider);

            Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
            pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            startActivityForResult(pickIntent, R.id.APPWIDGET_HOST_ID);
        }
    }

    private void addAppWidget(Intent data) {
        int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidget = mAppWidgetManager.getAppWidgetInfo(appWidgetId);

        if (appWidget.configure != null) {
            // Launch over to configure widget, if needed
            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            intent.setComponent(appWidget.configure);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            startActivityForResult(intent, R.id.REQUEST_CREATE_APPWIDGET);
        } else {
            // Otherwise just add it
        }
    }

}