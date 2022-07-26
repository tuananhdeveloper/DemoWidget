package com.example.demowidget.data.client;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Nguyen Tuan Anh on 26/07/2022.
 * FPT Software
 * tuananhprogrammer@gmail.com
 */
public interface OnDataChange<T> {
    void onResponse (Call<T> call, Response<T> response);
    void onFailure(Call<T> call, Throwable t);
}
