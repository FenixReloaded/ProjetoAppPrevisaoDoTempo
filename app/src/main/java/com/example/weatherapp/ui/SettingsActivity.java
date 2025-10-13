package com.example.weatherapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.R;
import com.google.android.material.button.MaterialButton;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        MaterialButton btnChangeCity = findViewById(R.id.btnChangeCity);
        MaterialButton btnChangePeriod = findViewById(R.id.btnChangePeriod);

        btnChangeCity.setOnClickListener(v -> {
            startActivity(new Intent(this, SelectCityActivity.class));
        });

        btnChangePeriod.setOnClickListener(v -> {
            startActivity(new Intent(this, SelectPeriodActivity.class));
        });
    }
}
