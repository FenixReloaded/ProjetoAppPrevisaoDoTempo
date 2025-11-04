package com.puc.appclimasphere;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.puc.appclimasphere.api.WeatherService;
import com.puc.appclimasphere.model.DailyForecast;
import com.puc.appclimasphere.model.ForecastResponse;
import com.puc.appclimasphere.model.GeocodingResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.puc.appclimasphere.ConfiguracaoActivity.KEY_LANG;
import static com.puc.appclimasphere.ConfiguracaoActivity.KEY_UNITS;
import static com.puc.appclimasphere.ConfiguracaoActivity.LANG_PT_BR;
import static com.puc.appclimasphere.ConfiguracaoActivity.PREFS_NAME;
import static com.puc.appclimasphere.ConfiguracaoActivity.UNITS_METRIC;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class SelecaoPeriodoActivity extends BaseActivity {

    Button btnVoltarPer;
    private Button btnSemanal, btnQuinzenal, btnMensal;
    private WeatherService weatherService;
    private SharedPreferences sharedPreferences;


    private int temaFundoId = 0; // Para guardar o ID do tema
    private static final String EXTRA_TEMA_FUNDO = "TEMA_FUNDO";
    private String currentCity = ""; // Para guardar o nome da cidade

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_selecao_periodo);

        LinearLayout mainLayout = findViewById(R.id.periodo_main_layout);

        if (getIntent().getExtras() != null) {
            temaFundoId = getIntent().getIntExtra(EXTRA_TEMA_FUNDO, 0);
            currentCity = getIntent().getStringExtra("CURRENT_CITY");

            if (temaFundoId != 0 && mainLayout != null) {
                mainLayout.setBackgroundResource(temaFundoId);
            }
        }

        // Inicializa as SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Inicializa o serviço Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WeatherService.BASE_URL) // "https://api.openweathermap.org/"
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        weatherService = retrofit.create(WeatherService.class);

        // Inicializa os botões
        btnVoltarPer= findViewById(R.id.btnVoltarPer);
        btnSemanal = findViewById(R.id.btn_semanal);
        btnQuinzenal = findViewById(R.id.btn_quinzenal);
        btnMensal = findViewById(R.id.btn_mensal);

        // Configura os Listeners
        btnVoltarPer.setOnClickListener(v -> finish());

        btnSemanal.setOnClickListener(v -> {
            // Inicia o processo para 7 dias
            fetchForecast(7);
        });

        btnQuinzenal.setOnClickListener(v -> {
            // Inicia o processo para 15 dias
            fetchForecast(15);
        });

        btnMensal.setOnClickListener(v -> {
            // O plano de estudante não cobre 30 dias
            Toast.makeText(this, "Previsão de 30 dias não disponível neste plano.", Toast.LENGTH_LONG).show();
        });

        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnVoltarPer= findViewById(R.id.btnVoltarPer);
        btnVoltarPer.setOnClickListener(v -> finish());
    }

    /**
     * Inicia o processo de busca. Primeiro, converte a cidade em coordenadas.
     */
    private void fetchForecast(int daysCount) {
        // Mostra um feedback de carregamento
        Toast.makeText(this, getString(R.string.carregando), Toast.LENGTH_SHORT).show();

        // Pega as configurações atuais de idioma e unidade
        String currentUnit = sharedPreferences.getString(KEY_UNITS, UNITS_METRIC);
        String currentLang = sharedPreferences.getString(KEY_LANG, LANG_PT_BR);

        // Chama a API de Geocodificação
        weatherService.getCoordinates(currentCity, 1, WeatherService.API_KEY)
                .enqueue(new Callback<List<GeocodingResponse>>() {
                    @Override
                    public void onResponse(Call<List<GeocodingResponse>> call, Response<List<GeocodingResponse>> response) {
                        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                            // Sucesso! Pega as coordenadas
                            GeocodingResponse geoData = response.body().get(0);
                            double lat = geoData.getLatitude();
                            double lon = geoData.getLongitude();

                            // Chama a API de Previsão com as coordenadas
                            callForecastApi(lat, lon, daysCount, currentUnit, currentLang);
                        } else {
                            // Falha na Geocodificação
                            String errorMsg = String.format(Locale.getDefault(), getString(R.string.toast_erro_buscar_coords), currentCity);
                            Toast.makeText(SelecaoPeriodoActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<GeocodingResponse>> call, Throwable t) {
                        Log.e("API_ERROR", "Falha na Geocodificação: " + t.getMessage());
                        Toast.makeText(SelecaoPeriodoActivity.this, getString(R.string.toast_erro_rede), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Com as coordenadas, busca a previsão do tempo.
     */
    private void callForecastApi(double lat, double lon, int daysCount, String currentUnit, String currentLang) {

        // Chama a API de Previsão Diária
        weatherService.getDailyForecast(lat, lon, daysCount, WeatherService.API_KEY, currentUnit, currentLang)
                .enqueue(new Callback<ForecastResponse>() {
                    @Override
                    public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getForecastList() != null) {
                            // Sucesso! Pega a lista de dados da previsão
                            List<DailyForecast> forecastData = response.body().getForecastList();

                            // Lança a nova ForecastActivity com os dados
                            launchForecastActivity(forecastData, daysCount, currentUnit);
                        } else {
                            // Falha na Previsão
                            Log.e("API_ERROR", "Falha na Previsão: Código " + response.code());
                            Toast.makeText(SelecaoPeriodoActivity.this, getString(R.string.toast_erro_buscar_previsao), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ForecastResponse> call, Throwable t) {
                        Log.e("API_ERROR", "Falha na Previsão: " + t.getMessage());
                        Toast.makeText(SelecaoPeriodoActivity.this, getString(R.string.toast_erro_rede), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Prepara e lança a tela ForecastActivity
     */
    private void launchForecastActivity(List<DailyForecast> forecastData, int daysCount, String currentUnit) {
        Intent intent = new Intent(this, ForecastActivity.class);

        // Anexa a lista de dados
        intent.putExtra(ForecastActivity.EXTRA_FORECAST_DATA, (Serializable) forecastData);

        // Define o título (Semanal ou Quinzenal)
        String title = (daysCount == 7)
                ? getString(R.string.titulo_previsao_semanal)
                : getString(R.string.titulo_previsao_quinzenal);
        intent.putExtra(ForecastActivity.EXTRA_FORECAST_TITLE, title);

        // Anexa a unidade (para o Adapter saber se é °C ou °F)
        intent.putExtra(KEY_UNITS, currentUnit);

        // Anexa o tema de fundo
        intent.putExtra(ForecastActivity.EXTRA_TEMA_FUNDO_FORECAST, temaFundoId);

        startActivity(intent);
    }
}