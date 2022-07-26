package com.example.demowidget.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.demowidget.BuildConfig;
import com.example.demowidget.data.client.OnDataChange;
import com.example.demowidget.data.model.CurrentWeather;
import com.example.demowidget.data.model.DirectGeocoding;
import com.example.demowidget.data.source.CurrentWeatherSource;
import com.example.demowidget.data.source.GeocodingSource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Nguyen Tuan Anh on 25/07/2022.
 * FPT Software
 * tuananhprogrammer@gmail.com
 */

public class CurrentWeatherRepository {

    private static CurrentWeatherRepository currentWeatherRepository;
    private static CurrentWeatherSource currentWeatherSource;
    private static GeocodingSource geocodingSource;
    private static final int LIMIT_COORDINATES_NUMBER = 10;

    private CurrentWeatherRepository() {}

    public static CurrentWeatherRepository getInstance(
            CurrentWeatherSource currentWeatherSource,
            GeocodingSource geocodingSource)
    {
        CurrentWeatherRepository.currentWeatherSource = currentWeatherSource;
        CurrentWeatherRepository.geocodingSource = geocodingSource;

        if (currentWeatherRepository == null) {
            currentWeatherRepository = new CurrentWeatherRepository();
        }
        return currentWeatherRepository;
    }

    public void getCurrentWeather(OnDataChange<CurrentWeather> onDataChange, Double lat, Double lon) {
        currentWeatherSource
                .getCurrentWeather(onDataChange, lat, lon, BuildConfig.API_KEY);
    }

    public void getCoordinatesByLocation(OnDataChange<List<DirectGeocoding>> onDataChange, String locationName) {
        geocodingSource
                .getCoordinatesByLocationName(onDataChange,
                        locationName, LIMIT_COORDINATES_NUMBER,
                        BuildConfig.API_KEY);
    }
}
