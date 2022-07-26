package com.example.demowidget.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demowidget.data.client.OnDataChange;
import com.example.demowidget.data.model.CurrentWeather;
import com.example.demowidget.data.repository.CurrentWeatherRepository;
import com.example.demowidget.data.source.impl.CurrentWeatherSourceImpl;
import com.example.demowidget.data.source.impl.GeocodingSourceImpl;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Nguyen Tuan Anh on 26/07/2022.
 * FPT Software
 * tuananhprogrammer@gmail.com
 */
public class WeatherViewModel extends ViewModel {

    private MutableLiveData<CurrentWeather> currentWeatherLiveData = new MutableLiveData<>();

    public LiveData<CurrentWeather> getCurrentWeatherData(Double lat, Double lon) {
        CurrentWeatherRepository
                .getInstance(
                        CurrentWeatherSourceImpl.getInstance(),
                        GeocodingSourceImpl.getInstance()
                )
                .getCurrentWeather(new OnDataChange<CurrentWeather>() {
                    @Override
                    public void onResponse(Call<CurrentWeather> call,
                                           Response<CurrentWeather> response) {
                        currentWeatherLiveData.setValue(response.body());
                    }
                    @Override
                    public void onFailure(Call<CurrentWeather> call, Throwable t) {
                        t.printStackTrace();
                    }
                }, lat, lon);
        return currentWeatherLiveData;
    }

}
