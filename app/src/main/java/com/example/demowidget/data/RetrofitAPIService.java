package com.example.demowidget.data;

import com.example.demowidget.data.model.CurrentWeather;
import com.example.demowidget.data.model.DirectGeocoding;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitAPIService {

    String BASE_URL = "https://api.openweathermap.org/";

    @GET("data/2.5/weather")
    Call<CurrentWeather>
    getCurrentWeather(@Query("lat") Double lat, @Query("lon") Double lon, @Query("appid") String apiKey);

    @GET("geo/1.0/direct")
    Call<List<DirectGeocoding>>
    getCoordinatesByLocationName(@Query("q") String q, @Query("limit") int limit, @Query("appid") String apiKey);
}
