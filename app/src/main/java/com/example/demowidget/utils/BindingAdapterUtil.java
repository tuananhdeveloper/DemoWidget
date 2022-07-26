package com.example.demowidget.utils;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

/**
 * Created by Nguyen Tuan Anh on 26/07/2022.
 * FPT Software
 * tuananhprogrammer@gmail.com
 */
public class BindingAdapterUtil {
    @BindingAdapter("bind:loadImage")
    public static void loadImage(ImageView imageView, String icon) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" http://openweathermap.org/img/wn/");
        stringBuilder.append(icon);
        stringBuilder.append("@4x.png");

        Glide.with(imageView.getContext())
                .load(stringBuilder.toString().trim())
                .into(imageView);
    }
}
