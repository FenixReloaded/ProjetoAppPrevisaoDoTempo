package com.example.weatherapp.ui;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.R;
import com.google.android.material.button.MaterialButton;

public class SelectPeriodActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_period);

        RadioGroup group = findViewById(R.id.radioGroupPeriod);
        MaterialButton btnConfirm = findViewById(R.id.btnConfirmPeriod);

        btnConfirm.setOnClickListener(v -> {
            int selectedId = group.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Selecione um período", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton selected = findViewById(selectedId);
            Toast.makeText(this, "Período: " + selected.getText(), Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
