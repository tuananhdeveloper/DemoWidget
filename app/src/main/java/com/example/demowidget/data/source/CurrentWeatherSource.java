package com.example.demowidget.data.source;

import androidx.lifecycle.MutableLiveData;

import com.example.demowidget.data.client.OnDataChange;
import com.example.demowidget.data.model.CurrentWeather;

import java.util.List;

/**
 * Created by Nguyen Tuan Anh on 25/07/2022.
 * FPT Software
 * tuananhprogrammer@gmail.com
 */
public interface CurrentWeatherSource {
    void getCurrentWeather(OnDataChange<CurrentWeather> onDataChange, Double lat, Double lon, String apiKey);
}
