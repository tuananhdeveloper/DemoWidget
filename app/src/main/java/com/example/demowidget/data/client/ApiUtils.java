package com.example.demowidget.data.client;

import com.example.demowidget.data.RetrofitAPIService;

/**
 * Created by Nguyen Tuan Anh on 13/07/2022.
 * FPT Software
 * tuananhprogrammer@gmail.com
 */
public class ApiUtils {
    public static RetrofitAPIService getRetrofitAPIService() {
        return RetrofitClient.getClient(RetrofitAPIService.BASE_URL)
                .create(RetrofitAPIService.class);
    }
}
