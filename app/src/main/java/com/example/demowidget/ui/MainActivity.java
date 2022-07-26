package com.example.demowidget.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.demowidget.R;
import com.example.demowidget.data.model.DirectGeocoding;
import com.example.demowidget.databinding.ActivityMainBinding;
import com.example.demowidget.ui.adapter.MyAdapter;
import com.example.demowidget.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener {

    private static final String QUERY_HINT = "Please, enter location's name";
    public static final String EXTRA_LOCATION_DATA = "Extra location";
    private static final int REQUEST_CODE = 44;

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private MyAdapter adapter;
    private List<DirectGeocoding> geocodingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem searchViewItem = menu.findItem(R.id.search);

        final SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setQueryHint(QUERY_HINT);
        searchView.setOnSearchClickListener(v -> {
            binding.buttonMyWeather.setVisibility(View.GONE);
            binding.myRecyclerview.setVisibility(View.VISIBLE);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                performSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchViewItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                binding.buttonMyWeather.setVisibility(View.GONE);
                binding.myRecyclerview.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                binding.buttonMyWeather.setVisibility(View.VISIBLE);
                binding.myRecyclerview.setVisibility(View.GONE);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(View view, int position, boolean isLongClick) {
        if (geocodingList != null && geocodingList.size() > 0) {
            Intent intent = new Intent(this, WeatherActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXTRA_LOCATION_DATA, geocodingList.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    getMyWeather();
                }
                break;
        }
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.myToolbar);
        adapter = new MyAdapter(this, geocodingList);
        binding.myRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.myRecyclerview.setAdapter(adapter);

        binding.buttonMyWeather.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermission();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission() {
        // Check if the permission has been granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getMyWeather();
        } else {
            // Permission is missing and must be requested.
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    @SuppressLint("MissingPermission")
    private void getMyWeather() {
        DirectGeocoding geocoding = null;
        boolean gps_enabled = false;
        boolean network_enabled = false;

        LocationManager locationManager = (LocationManager) getBaseContext()
                .getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location net_loc = null, gps_loc = null, finalLoc = null;

        if (gps_enabled) {
            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            geocoding = new DirectGeocoding();
            geocoding.setLat(gps_loc.getLatitude());
            geocoding.setLon(gps_loc.getLongitude());
        }
        if (network_enabled) {
            net_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            geocoding = new DirectGeocoding();
            geocoding.setLat(net_loc.getLatitude());
            geocoding.setLon(net_loc.getLongitude());
        }
        if (gps_loc != null && net_loc != null) {
            if (gps_loc.getAccuracy() > net_loc.getAccuracy())
                finalLoc = net_loc;
            else
                finalLoc = gps_loc;
        } else {

            if (gps_loc != null) {
                finalLoc = gps_loc;
            } else if (net_loc != null) {
                finalLoc = net_loc;
            }
        }
        
        if (geocoding != null) {
            Intent intent = new Intent(this, WeatherActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXTRA_LOCATION_DATA, geocoding);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void initData() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    private void performSearch(String query) {
        viewModel.getCoordinatesByLocation(query)
                .observe(this, geocodingList -> {
                    binding.textNothing.setVisibility(View.GONE);
                    this.geocodingList.clear();
                    this.geocodingList.addAll(geocodingList);
                    this.adapter.setGeocodingList(this.geocodingList);
                });
    }
}