package com.example.demowidget.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demowidget.data.client.OnDataChange;
import com.example.demowidget.data.model.CurrentWeather;
import com.example.demowidget.data.model.DirectGeocoding;
import com.example.demowidget.data.repository.CurrentWeatherRepository;
import com.example.demowidget.data.source.impl.CurrentWeatherSourceImpl;
import com.example.demowidget.data.source.impl.GeocodingSourceImpl;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Nguyen Tuan Anh on 25/07/2022.
 * FPT Software
 * tuananhprogrammer@gmail.com
 */
public class MainViewModel extends ViewModel {

    private MutableLiveData<List<DirectGeocoding>> coordinatesLiveData = new MutableLiveData<>();

    public LiveData<List<DirectGeocoding>> getCoordinatesByLocation(String locationName) {
        CurrentWeatherRepository.getInstance(
                        CurrentWeatherSourceImpl.getInstance(),
                        GeocodingSourceImpl.getInstance()
                )
                .getCoordinatesByLocation(new OnDataChange<List<DirectGeocoding>>() {
                    @Override
                    public void onResponse(Call<List<DirectGeocoding>> call, Response<List<DirectGeocoding>> response) {
                        coordinatesLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<DirectGeocoding>> call, Throwable t) {
                        t.printStackTrace();
                    }
                }, locationName);

        return coordinatesLiveData;
    }

}
