package com.puc.appclimasphere;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.puc.appclimasphere.model.DailyForecast;

import java.io.Serializable;
import java.util.List;

// Importa a chave da unidade (C ou F)
import static com.puc.appclimasphere.ConfiguracaoActivity.KEY_UNITS;
import static com.puc.appclimasphere.ConfiguracaoActivity.UNITS_METRIC;

public class ForecastActivity extends BaseActivity {

    // Chaves públicas para receber dados da Intent
    public static final String EXTRA_FORECAST_DATA = "FORECAST_DATA";
    public static final String EXTRA_FORECAST_TITLE = "FORECAST_TITLE";
    public static final String EXTRA_TEMA_FUNDO_FORECAST = "TEMA_FUNDO"; // Reutiliza a chave

    private RecyclerView rvForecast;
    private TextView tvForecastTitle;
    private Button btnVoltarForecast;
    private LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forecast);

        // Inicializa as Views
        rvForecast = findViewById(R.id.rv_forecast);
        tvForecastTitle = findViewById(R.id.tv_forecast_title);
        btnVoltarForecast = findViewById(R.id.btn_voltar_forecast);
        mainLayout = findViewById(R.id.forecast_main_layout);

        // Configura o botão Voltar
        btnVoltarForecast.setOnClickListener(v -> finish());

        // Pega os dados da Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Pega o tema de fundo (passado da SelecaoPeriodoActivity)
            int temaFundoId = extras.getInt(EXTRA_TEMA_FUNDO_FORECAST, 0);
            if (temaFundoId != 0) {
                mainLayout.setBackgroundResource(temaFundoId);
            }

            // Pega o título (ex: "Previsão Semanal")
            String title = extras.getString(EXTRA_FORECAST_TITLE);
            if (title != null) {
                tvForecastTitle.setText(title);
            }

            // Pega a unidade atual (C ou F)
            String currentUnit = extras.getString(KEY_UNITS, UNITS_METRIC);

            // Pega a LISTA de dados da previsão
            List<DailyForecast> forecastData = null;
            try {
                // Tenta pegar o Serializable e fazer o cast
                Serializable data = extras.getSerializable(EXTRA_FORECAST_DATA);
                if (data instanceof List) {
                    forecastData = (List<DailyForecast>) data;
                }
            } catch (Exception e) {
                Toast.makeText(this, "Erro ao carregar dados da previsão.", Toast.LENGTH_SHORT).show();
            }

            // Configura o RecyclerView
            if (forecastData != null && !forecastData.isEmpty()) {
                setupRecyclerView(forecastData, currentUnit);
            } else {
                Toast.makeText(this, "Nenhum dado de previsão encontrado.", Toast.LENGTH_LONG).show();
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    /**
     * Configura o RecyclerView com o Adapter.
     */
    private void setupRecyclerView(List<DailyForecast> data, String currentUnit) {
        ForecastAdapter adapter = new ForecastAdapter(this, data, currentUnit);
        rvForecast.setLayoutManager(new LinearLayoutManager(this));
        rvForecast.setAdapter(adapter);
    }
}