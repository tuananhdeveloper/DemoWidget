package com.example.demowidget.utils;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.example.demowidget.R;

/**
 * Created by Nguyen Tuan Anh on 26/07/2022.
 * FPT Software
 * tuananhprogrammer@gmail.com
 */
public class BindingAdapterUtil {
    @BindingAdapter("bind:loadImage")
    public static void loadImage(ImageView imageView, String icon) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://openweathermap.org/img/wn/");
        stringBuilder.append(icon);
        stringBuilder.append("@4x.png");

        Glide.with(imageView.getContext())
                .load(stringBuilder.toString().trim())
                .placeholder(R.drawable.ic_image)
                .into(imageView);
    }
}
