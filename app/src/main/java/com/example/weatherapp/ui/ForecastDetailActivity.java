package com.example.weatherapp.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.data.MockWeatherRepository;

public class ForecastDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_detail);

        RecyclerView recyclerView = findViewById(R.id.recyclerForecast);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MockWeatherRepository repository = new MockWeatherRepository();
        SimpleTextAdapter adapter = new SimpleTextAdapter();
        adapter.replaceAll(repository.getWeeklyForecast("São Paulo"));
        recyclerView.setAdapter(adapter);
    }
}
