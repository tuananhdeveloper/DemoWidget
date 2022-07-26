package com.example.demowidget.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.demowidget.data.model.DirectGeocoding;
import com.example.demowidget.databinding.ItemDirectBinding;

import java.util.List;

/**
 * Created by Nguyen Tuan Anh on 25/07/2022.
 * FPT Software
 * tuananhprogrammer@gmail.com
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<DirectGeocoding> geocodingList;
    private OnItemClickListener onItemClickListener;

    public MyAdapter(OnItemClickListener onItemClickListener, List<DirectGeocoding> geocodingList) {
        this.onItemClickListener = onItemClickListener;
        this.geocodingList = geocodingList;
    }

    public void setGeocodingList(List<DirectGeocoding> geocodingList) {
        this.geocodingList = geocodingList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDirectBinding itemDirectBinding = ItemDirectBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new MyViewHolder(itemDirectBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DirectGeocoding geocoding = geocodingList.get(position);
        holder.binding.setLocationModel(geocoding);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return geocodingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemDirectBinding binding;
        public MyViewHolder(@NonNull ItemDirectBinding binding) {
            super(binding.getRoot());
            binding.getRoot().setOnClickListener(this);
            this.binding = binding;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition(), false);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, boolean isLongClick);
    }
}
