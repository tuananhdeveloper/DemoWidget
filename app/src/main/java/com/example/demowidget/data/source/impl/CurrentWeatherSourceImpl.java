package com.example.demowidget.data.source.impl;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.demowidget.data.client.ApiUtils;
import com.example.demowidget.data.client.OnDataChange;
import com.example.demowidget.data.client.RetrofitClient;
import com.example.demowidget.data.model.CurrentWeather;
import com.example.demowidget.data.source.CurrentWeatherSource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nguyen Tuan Anh on 25/07/2022.
 * FPT Software
 * tuananhprogrammer@gmail.com
 */
public class CurrentWeatherSourceImpl implements CurrentWeatherSource {
    private static final String TAG = "CurrentWeatherSource";
    private static CurrentWeatherSourceImpl currentWeatherSourceImpl;

    private CurrentWeatherSourceImpl() {}

    @Override
    public void getCurrentWeather(OnDataChange<CurrentWeather> onDataChange,
                                  Double lat, Double lon, String apiKey) {
        ApiUtils.getRetrofitAPIService().getCurrentWeather(lat, lon, apiKey)
                .enqueue(new Callback<CurrentWeather>() {
                    @Override
                    public void onResponse(@NonNull Call<CurrentWeather> call,
                                           @NonNull Response<CurrentWeather> response) {
                        onDataChange.onResponse(call, response);
                    }

                    @Override
                    public void onFailure(@NonNull Call<CurrentWeather> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        onDataChange.onFailure(call, t);
                    }
                });
    }

    public static CurrentWeatherSourceImpl getInstance() {
        if (currentWeatherSourceImpl == null) {
            currentWeatherSourceImpl = new CurrentWeatherSourceImpl();
        }
        return currentWeatherSourceImpl;
    }


}
