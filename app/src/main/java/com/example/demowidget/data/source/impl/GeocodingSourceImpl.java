package com.example.demowidget.data.source.impl;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.demowidget.data.client.ApiUtils;
import com.example.demowidget.data.client.OnDataChange;
import com.example.demowidget.data.model.CurrentWeather;
import com.example.demowidget.data.model.DirectGeocoding;
import com.example.demowidget.data.source.GeocodingSource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nguyen Tuan Anh on 25/07/2022.
 * FPT Software
 * tuananhprogrammer@gmail.com
 */
public class GeocodingSourceImpl implements GeocodingSource {

    private static GeocodingSourceImpl geocodingSource;

    private GeocodingSourceImpl() {}

    @Override
    public void getCoordinatesByLocationName(OnDataChange<List<DirectGeocoding>> onDataChange,
            String locationName, int limit, String apiKey) {
        ApiUtils.getRetrofitAPIService()
                .getCoordinatesByLocationName(locationName, limit, apiKey)
                .enqueue(new Callback<List<DirectGeocoding>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<DirectGeocoding>> call,
                                           @NonNull Response<List<DirectGeocoding>> response) {
                        assert response.body() != null;
                        Log.d("GeocodingSource", response.body().toString());
                        onDataChange.onResponse(call, response);
                    }
                    @Override
                    public void onFailure(@NonNull Call<List<DirectGeocoding>> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        onDataChange.onFailure(call, t);
                    }
                });
    }

    public static GeocodingSourceImpl getInstance() {
        if (geocodingSource == null) {
            geocodingSource = new GeocodingSourceImpl();
        }
        return geocodingSource;
    }

}
