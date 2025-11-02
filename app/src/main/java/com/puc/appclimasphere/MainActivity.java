package com.puc.appclimasphere;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.puc.appclimasphere.api.WeatherService;
import com.puc.appclimasphere.model.WeatherResponse;

public class MainActivity extends AppCompatActivity {

    Button btnDetalhes, btnConfiguracao, btnMudarCidade, btnSelecaoPeriodo;
    private TextView tvCityTemp;
    private ImageView tvWeatherIcon;
    private LinearLayout mainLayout;
    private WeatherResponse cachedWeatherData;
    private String currentCity = "São Paulo";
    private int currentBackgroundId = R.drawable.gradient_dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Inicializando as Views
        tvCityTemp = findViewById(R.id.tv_city_temp);
        tvWeatherIcon = findViewById(R.id.tv_weather_icon);
        mainLayout = findViewById(R.id.main);

        tvWeatherIcon.setImageResource(R.drawable.ic_weather_loading);
        tvCityTemp.setText("Carregando...");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnDetalhes = findViewById(R.id.btn_detalhes);
        btnDetalhes.setOnClickListener(v -> {
            if(cachedWeatherData != null){
            Intent intent = new Intent(MainActivity.this, DetalhesActivity.class);
            intent.putExtra("WEATHER_DATA", cachedWeatherData);
            intent.putExtra("TEMA_FUNDO", currentBackgroundId);
            startActivity(intent);
        }else{
            Toast.makeText(MainActivity.this, "Aguarde, buscando dados do clima...", Toast.LENGTH_SHORT).show();
        }
    });

        btnConfiguracao = findViewById(R.id.btn_configuracao);
        btnConfiguracao.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ConfiguracaoActivity.class);
            intent.putExtra("TEMA_FUNDO", currentBackgroundId);
            startActivity(intent);
        });

        btnMudarCidade = findViewById(R.id.btn_mudar_cidade);
        btnMudarCidade.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SelecaoCidadeActivity.class);
            intent.putExtra("TEMA_FUNDO", currentBackgroundId);
            startActivity(intent);
        });

        btnSelecaoPeriodo = findViewById(R.id.btn_selecao_periodo);
        btnSelecaoPeriodo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SelecaoPeriodoActivity.class);
            intent.putExtra("TEMA_FUNDO", currentBackgroundId);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        fetchWeatherData(currentCity);
    }

    private void fetchWeatherData(String city){

        tvCityTemp.setText("Carregando...");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WeatherService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService service = retrofit.create(WeatherService.class);

        service.getCurrentWeather(city, WeatherService.API_KEY, WeatherService.UNITS,
                WeatherService.LANG).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    WeatherResponse weatherData = response.body();
                    cachedWeatherData = weatherData;
                    updateUI(weatherData);
                }else{
                    tvWeatherIcon.setImageResource(R.drawable.ic_weather_error);
                    tvCityTemp.setText("Erro API");
                    Toast.makeText(MainActivity.this, "Erro ao buscar dados do clima. " +
                            "Código: " + response.code(), Toast.LENGTH_SHORT).show();
                            //O erro 401 ou 404 Chave/Cidade errada
                }
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("API_ERROR", "Falha na conexão: " + t.getMessage());
                tvWeatherIcon.setImageResource(R.drawable.ic_weather_error);
                tvCityTemp.setText("Sem Conexão");
                Toast.makeText(MainActivity.this, "Erro de rede. Verifique sua Conexão.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateUI(WeatherResponse data) {
        String cityName = data.getCityName();
        String countryCode = data.getSys() != null ? data.getSys().getCountryCode() : "";
        String temp = String.format("%.0f°C", data.getMain().getCurrentTemp());

        tvCityTemp.setText(cityName + ", " + countryCode + "\n" + temp);

        int weatherId = data.getWeather().get(0).getId();
        String iconId = data.getWeather().get(0).getIcon();
        setWeatherTheme(weatherId, iconId);
    }

    // Método para decidir o degradê e o ícone
    private void setWeatherTheme(int weatherId, String iconId) {

        int backgroundDrawable;
        int iconResId; // ID do recurso para o icone da condicao climatica

        boolean isNight = iconId.endsWith("n");

        // Fundo Padrão
        backgroundDrawable = isNight ? R.drawable.gradient_noite : R.drawable.gradient_dia;

        if (weatherId >= 200 && weatherId <= 232) { // Trovoadas
            backgroundDrawable = R.drawable.gradient_trovoadas;

        } else if (weatherId >= 300 && weatherId <= 321) { // Chuvisco
            backgroundDrawable = R.drawable.gradient_chuva_leve;

        } else if (weatherId >= 500 && weatherId <= 531) { // Chuva
            if (weatherId == 511) {
                backgroundDrawable = R.drawable.gradient_neve; // Chuva congelante

            } else if (weatherId >= 520 && weatherId <= 531) {
                backgroundDrawable = R.drawable.gradient_chuvoso; // Chuva forte

            } else {
                backgroundDrawable = R.drawable.gradient_chuva_leve;
            }

        } else if (weatherId >= 600 && weatherId <= 622) { // Neve
            backgroundDrawable = R.drawable.gradient_neve;

        } else if (weatherId >= 701 && weatherId <= 781) { // Névoa
            backgroundDrawable = R.drawable.gradient_nevoa;

        } else if (weatherId == 800) { // Céu Limpo
            backgroundDrawable = isNight ? R.drawable.gradient_noite : R.drawable.gradient_dia;

        } else if (weatherId >= 801 && weatherId <= 804) { // Nuvens
            backgroundDrawable = R.drawable.gradient_nublado;
        }

        // LÓGICA DO ÍCONE
        // Mapeia a string "01d", "01n",..., para o R.drawable.ic_XXd2x.png
        switch (iconId) {
            case "01d": iconResId = R.drawable.ic_01d2x; break;
            case "01n": iconResId = R.drawable.ic_01n2x; break;
            case "02d": iconResId = R.drawable.ic_02d2x; break;
            case "02n": iconResId = R.drawable.ic_02n2x; break;
            case "03d": iconResId = R.drawable.ic_03d2x; break;
            case "03n": iconResId = R.drawable.ic_03n2x; break;
            case "04d": iconResId = R.drawable.ic_04d2x; break;
            case "04n": iconResId = R.drawable.ic_04n2x; break;
            case "09d": iconResId = R.drawable.ic_09d2x; break;
            case "09n": iconResId = R.drawable.ic_09n2x; break;
            case "10d": iconResId = R.drawable.ic_10d2x; break;
            case "10n": iconResId = R.drawable.ic_10n2x; break;
            case "11d": iconResId = R.drawable.ic_11d2x; break;
            case "11n": iconResId = R.drawable.ic_11n2x; break;
            case "13d": iconResId = R.drawable.ic_13d2x; break;
            case "13n": iconResId = R.drawable.ic_13n2x; break;
            case "50d": iconResId = R.drawable.ic_50d2x; break;
            case "50n": iconResId = R.drawable.ic_50n2x; break;
            default:

                // Fallback caso a API envie um código desconhecido
                iconResId = isNight ? R.drawable.ic_01n2x : R.drawable.ic_01d2x;
                break;
        }

        currentBackgroundId = backgroundDrawable;

        // Aplica as mudanças
        if (mainLayout != null){
            mainLayout.setBackgroundResource(currentBackgroundId);
        }
        tvWeatherIcon.setImageResource(iconResId);
    }
}