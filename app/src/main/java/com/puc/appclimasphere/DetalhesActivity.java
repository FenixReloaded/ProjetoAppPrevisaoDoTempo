package com.puc.appclimasphere;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.puc.appclimasphere.model.WeatherResponse;

public class DetalhesActivity extends AppCompatActivity {

    Button btnVoltarDet;
    public static final String EXTRA_WEATHER_DATA = "WEATHER_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalhes);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvDetalhesCidade = findViewById(R.id.tv_detalhes_cidade);
        TextView tvSensacao = findViewById(R.id.tv_sensacao);
        TextView tvUmidade = findViewById(R.id.tv_umidade);
        TextView tvPressao = findViewById(R.id.tv_pressao);
        TextView tvTempMinMax = findViewById(R.id.tv_temp_min_max);

        Bundle extras = getIntent().getExtras();

        // INÍCIO DA ZONA CRÍTICA
        if (extras != null && extras.containsKey(EXTRA_WEATHER_DATA)){

            // Tenta pegar o objeto e faz o CAST. Usa Object para evitar um crash caso o tipo esteja errado
            Object serializableData = extras.getSerializable(EXTRA_WEATHER_DATA);

            if (serializableData instanceof WeatherResponse) {
                WeatherResponse data = (WeatherResponse) serializableData;

                // Proteção contra Erros de Acesso a Dados
                try {
                    // Validação básica para evitar NPE nos campos obrigatórios
                    if (data != null && data.getMain() != null && data.getWeather() != null && !data.getWeather().isEmpty() && data.getSys() != null) {

                        String cityCountry = data.getCityName() + ", " + data.getSys().getCountryCode();
                        String temp = String.format("%.0f°C", data.getMain().getCurrentTemp());
                        String description = data.getWeather().get(0).getDescription();

                        tvDetalhesCidade.setText(cityCountry + " - " + temp + " (" + description + ")");
                        tvSensacao.setText(String.format("Sensação Térmica: %.0f°C", data.getMain().getFeelsLike()));
                        tvUmidade.setText(String.format("Umidade: %d%%", data.getMain().getHumidity()));
                        tvPressao.setText(String.format("Pressão: %d hPa", data.getMain().getPressure()));

                        // Chamadas seguras
                        double minTemp = data.getMain().getMinTemp();
                        double maxTemp = data.getMain().getMaxTemp();
                        tvTempMinMax.setText(String.format("Mínima/Máxima: %.0f°C / %.0f°C", minTemp, maxTemp));

                    } else {
                        Toast.makeText(this, "Erro: Dados do clima incompletos.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // Loga qualquer erro inesperado durante o processamento
                    Log.e("DetalhesActivity", "Crash ao processar dados: " + e.getMessage());
                    Toast.makeText(this, "Erro crítico ao carregar detalhes. Verifique a API Key.", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this, "Erro no formato dos dados de clima.", Toast.LENGTH_LONG).show();
            }

        } else {
            tvDetalhesCidade.setText("São Paulo, BR (Sem dados API)");
            tvSensacao.setText("Sensação Térmica: --");
            tvUmidade.setText("Umidade: --");
            tvPressao.setText("Pressão: --");
            tvTempMinMax.setText("Mínima/Máxima: --");
        }

        btnVoltarDet = findViewById(R.id.btnVoltarDet);
        btnVoltarDet.setOnClickListener(v -> finish());
    }
}