package com.example.weatherapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.R;
import com.google.android.material.button.MaterialButton;
import com.example.weatherapp.data.MockWeatherRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textCity = findViewById(R.id.textCity);
        TextView textCurrentWeather = findViewById(R.id.textCurrentWeather);
        MaterialButton btnDetails = findViewById(R.id.btnDetails);
        MaterialButton btnSettings = findViewById(R.id.btnSettings);

        String currentCity = "São Paulo";
        MockWeatherRepository repository = new MockWeatherRepository();

        textCity.setText(currentCity);
        textCurrentWeather.setText(repository.getCurrentWeather(currentCity));

        btnDetails.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForecastDetailActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }
}
