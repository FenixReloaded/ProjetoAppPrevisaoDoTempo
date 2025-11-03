package com.puc.appclimasphere;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.puc.appclimasphere.model.WeatherResponse;

import static com.puc.appclimasphere.ConfiguracaoActivity.KEY_UNITS;
import static com.puc.appclimasphere.ConfiguracaoActivity.UNITS_METRIC;

import java.util.Locale;

public class DetalhesActivity extends AppCompatActivity {

    Button btnVoltarDet;
    public static final String EXTRA_WEATHER_DATA = "WEATHER_DATA";
    private static final String EXTRA_TEMA_FUNDO = "TEMA_FUNDO";

    private String unitSymbol = "°C"; // Padrão

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalhes);

        LinearLayout mainLayout = findViewById(R.id.detalhes_main_layout);

        // Recebe e aplica o tema dinâmico
        if (getIntent().getExtras() != null) {
            int temaFundoId = getIntent().getIntExtra(EXTRA_TEMA_FUNDO, 0);
            if (temaFundoId != 0 && mainLayout != null) {
                mainLayout.setBackgroundResource(temaFundoId);
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
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

        // Início da zona crítica

        // Define o símbolo da unidade (C ou F)
        if (extras != null) {
            String currentUnit = extras.getString(KEY_UNITS, UNITS_METRIC);
            unitSymbol = currentUnit.equals(UNITS_METRIC) ? "°C" : "°F";
        }

        if (extras != null && extras.containsKey(EXTRA_WEATHER_DATA)){

            // Tenta pegar o objeto e faz o cast. Usa Object para evitar um crash caso o tipo esteja errado
            Object serializableData = extras.getSerializable(EXTRA_WEATHER_DATA);

            if (serializableData instanceof WeatherResponse) {
                WeatherResponse data = (WeatherResponse) serializableData;

                // Proteção contra Erros de Acesso a Dados
                try {
                    // Validação básica para evitar NPE nos campos obrigatórios
                    if (data != null && data.getMain() != null && data.getWeather() != null && !data.getWeather().isEmpty() && data.getSys() != null) {

                        String cityCountry = data.getCityName() + ", " + data.getSys().getCountryCode();

                        String temp = String.format(Locale.getDefault(),"%.0f%s", data.getMain().getCurrentTemp(), unitSymbol);
                        String description = data.getWeather().get(0).getDescription();

                        double minTemp = data.getMain().getMinTemp();
                        double maxTemp = data.getMain().getMaxTemp();

                        if (tvDetalhesCidade != null) {
                            tvDetalhesCidade.setText(getString(R.string.detalhes_cidade_formato, cityCountry, temp, description));
                        }
                        if (tvSensacao != null) {
                            tvSensacao.setText(getString(R.string.label_sensacao, data.getMain().getFeelsLike(), unitSymbol));
                        }
                        if (tvUmidade != null) {
                            tvUmidade.setText(getString(R.string.label_umidade, data.getMain().getHumidity()));
                        }
                        if (tvPressao != null) {
                            tvPressao.setText(getString(R.string.label_pressao, data.getMain().getPressure()));
                        }

                        // Esta é a verificação crucial que impede o crash
                        if (tvTempMinMax != null) {

                            tvTempMinMax.setText(getString(R.string.label_min_max, minTemp, unitSymbol, maxTemp, unitSymbol));

                        } else {
                            // Se ele for nulo, saberemos pelo Logcat
                            Log.e("DetalhesActivity", "ERRO FATAL: tvTempMinMax é NULL. Verifique o ID no XML e limpe o cache.");
                        }
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
            // Caso não tenha dados na Intent usar dados mockados para teste
            setMockData(tvDetalhesCidade, tvSensacao, tvUmidade, tvPressao, tvTempMinMax);

//        tvCidade.setText(getString(R.string.mock_sem_dados_api));
//        tvSensacao.setText(String.format(Locale.getDefault(), getString(R.string.mock_sensacao), unitSymbol));
//        tvUmidade.setText(getString(R.string.mock_umidade));
//        tvPressao.setText(getString(R.string.mock_pressao));
//        tvTempMinMax.setText(String.format(Locale.getDefault(), getString(R.string.mock_min_max), unitSymbol, unitSymbol));
        }

        btnVoltarDet = findViewById(R.id.btnVoltarDet);
        btnVoltarDet.setOnClickListener(v -> finish());
    }
    // Define os dados mockados
    private void setMockData(TextView tvCidade, TextView tvSensacao, TextView tvUmidade, TextView tvPressao, TextView tvTempMinMax) {
        tvCidade.setText("São Paulo, BR (Sem dados API)");
        tvSensacao.setText(String.format("Sensação Térmica: --%s", unitSymbol));
        tvUmidade.setText("Umidade: --%");
        tvPressao.setText("Pressão: -- hPa");
        tvTempMinMax.setText(String.format("Mínima/Máxima: --%s / --%s", unitSymbol, unitSymbol));
    }
}