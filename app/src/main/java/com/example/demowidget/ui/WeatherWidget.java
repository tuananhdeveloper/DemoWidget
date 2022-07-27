package com.example.demowidget.ui;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.demowidget.R;
import com.example.demowidget.data.model.CurrentWeather;
import com.example.demowidget.ui.service.WeatherService;

/**
 * Implementation of App Widget functionality.
 */
public class WeatherWidget extends AppWidgetProvider {
    public static final String REQUEST_DATA_KEY = "REQUEST_DATA";
    public static final String REQUEST_DATA_VALUE = "REQUEST_DATA_VALUE";
    public static final String DATA_WEATHER_KEY = "DATA_WEATHER_KEY";

    private static RemoteViews views;
    private static AppWidgetManager appWidgetManager;
    private static int appWidgetId;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        WeatherWidget.appWidgetManager = appWidgetManager;
        WeatherWidget.appWidgetId = appWidgetId;

        // Construct the RemoteViews object
        views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

        Intent intent = new Intent(context, WeatherService.class);
        intent.putExtra(REQUEST_DATA_KEY, REQUEST_DATA_VALUE);
        context.startService(intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Intent intent = new Intent(context, WeatherService.class);
        intent.putExtra(REQUEST_DATA_KEY, REQUEST_DATA_VALUE);
        context.startService(intent);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent != null && intent.getAction() != null) {
                if (intent.getExtras() != null &&
                        intent.getExtras().getSerializable(DATA_WEATHER_KEY) != null) {

                    CurrentWeather data = (CurrentWeather) intent
                            .getExtras()
                            .getSerializable(DATA_WEATHER_KEY);

                    updateUI(context, data);
                }
        }
    }

    private void updateUI(Context context, CurrentWeather data) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://openweathermap.org/img/wn/");
        stringBuilder.append(data.getWeather().get(0).getIcon());
        stringBuilder.append("@4x.png");
        Glide.with(context)
                .asBitmap()
                        .load(stringBuilder.toString()).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        views.setImageViewBitmap(R.id.image_weather, resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
        views.setTextViewText(R.id.text_temp, data.getMain().getTempInCelsius().toString());
        views.setTextViewText(R.id.text_location_name, data.getName());
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

}