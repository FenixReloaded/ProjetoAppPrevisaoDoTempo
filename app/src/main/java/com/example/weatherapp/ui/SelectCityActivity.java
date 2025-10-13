package com.example.weatherapp.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SelectCityActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        TextInputEditText inputCity = findViewById(R.id.inputCity);
        MaterialButton btnSaveCity = findViewById(R.id.btnSaveCity);

        btnSaveCity.setOnClickListener(v -> {
            String city = inputCity.getText() != null ? inputCity.getText().toString() : "";
            Toast.makeText(this, "Cidade salva: " + city, Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
