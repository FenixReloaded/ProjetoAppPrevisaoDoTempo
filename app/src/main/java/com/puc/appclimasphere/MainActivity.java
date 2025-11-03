package com.puc.appclimasphere;

import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.puc.appclimasphere.api.WeatherService;
import com.puc.appclimasphere.model.WeatherResponse;

// Importa as chaves de preferência da ConfiguracaoActivity
import static com.puc.appclimasphere.ConfiguracaoActivity.PREFS_NAME;
import static com.puc.appclimasphere.ConfiguracaoActivity.KEY_UNITS;
import static com.puc.appclimasphere.ConfiguracaoActivity.UNITS_METRIC;

import static com.puc.appclimasphere.ConfiguracaoActivity.KEY_LANG;
import static com.puc.appclimasphere.ConfiguracaoActivity.LANG_PT_BR;

import java.util.Locale;

public class MainActivity extends BaseActivity {

    Button btnDetalhes, btnConfiguracao, btnMudarCidade, btnSelecaoPeriodo;
    private TextView tvCityTemp;
    private ImageView tvWeatherIcon;
    private LinearLayout mainLayout;
    private WeatherResponse cachedWeatherData;
    private String currentCity = "São Paulo";
    private int currentBackgroundId = R.drawable.gradient_dia;

    // Chave pública para receber o resultado da outra activity
    public static final String EXTRA_NEW_CITY = "NEW_CITY";
    private ActivityResultLauncher <Intent> selecaoCidadeLauncher;

    private SharedPreferences sharedPreferences;
    private String currentUnit = UNITS_METRIC;

    private String currentLang = LANG_PT_BR; // Variável para guardar o idioma atual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        registrarLauncherSelecaoCidade();

        // Inicializa as preferências
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        setContentView(R.layout.activity_main);

        //Inicializando as Views
        tvCityTemp = findViewById(R.id.tv_city_temp);
        tvWeatherIcon = findViewById(R.id.tv_weather_icon);
        mainLayout = findViewById(R.id.main);

        tvWeatherIcon.setImageResource(R.drawable.ic_weather_loading);
        tvCityTemp.setText(getString(R.string.carregando));

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
            intent.putExtra(KEY_UNITS, currentUnit);
            startActivity(intent);
        }else{
                Toast.makeText(MainActivity.this, getString(R.string.toast_aguarde), Toast.LENGTH_SHORT).show();        }
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
            selecaoCidadeLauncher.launch(intent);
        });

        btnSelecaoPeriodo = findViewById(R.id.btn_selecao_periodo);
        btnSelecaoPeriodo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SelecaoPeriodoActivity.class);
            intent.putExtra("TEMA_FUNDO", currentBackgroundId);
            startActivity(intent);
        });
    }

    // Registra o "escutador" que vai receber a cidade de volta da SelecaoCidadeActivity
    private void registrarLauncherSelecaoCidade() {
        selecaoCidadeLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Verifica se a activity retornou com SUCESSO (RESULT_OK)
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK && result.getData() != null) {

                        // Pega a string da cidade que foi enviada de volta
                        String newCity = result.getData().getStringExtra(EXTRA_NEW_CITY);

                        // Verifica se a cidade é válida e se é diferente da atual
                        if (newCity != null && !newCity.isEmpty() && !newCity.equals(currentCity)) {

                            // Atualiza a cidade atual
                            currentCity = newCity;

                            // O método onResume() será chamado automaticamente após
                            // o retorno à MainActivity, e ele já chama o fetchWeatherData().
                            // Não é necessário chamar fetchWeatherData() aqui.
                        }
                    }
                }
        );
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadSettingsAndUpdateWeather();
        fetchWeatherData(currentCity);
    }

    /**
     * Lê as preferências salvas e chama o fetchWeatherData
     */
    private void loadSettingsAndUpdateWeather() {
        // Lê a unidade salva, usando "metric" como padrão
        String savedUnit = sharedPreferences.getString(KEY_UNITS, UNITS_METRIC);

        // Lê o idioma salvo, usando "pt_br" como padrão
        String savedLang = sharedPreferences.getString(KEY_LANG, LANG_PT_BR);
        // --- FIM CÓDIGO NOVO ---

        // --- LÓGICA ATUALIZADA ---
        // Se a unidade MUDOU, ou se o idioma MUDOU,
        // ou se for a primeira vez (cachedWeatherData == null),
        // então buscamos os dados da API.
        if (!savedUnit.equals(currentUnit) || !savedLang.equals(currentLang) || cachedWeatherData == null) {
            currentUnit = savedUnit;
            currentLang = savedLang; // Salva o idioma atual
            fetchWeatherData(currentCity);
        }
        // Se nada mudou e já temos dados, não fazemos nada,
        // pois a UI já está atualizada.
    }

    private void fetchWeatherData(String city){

        tvCityTemp.setText(getString(R.string.carregando));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WeatherService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService service = retrofit.create(WeatherService.class);

        // A linguagem ainda está fixa, mas a unidade agora é dinâmica
        //String lang = WeatherService.LANG; // "pt_br"

        service.getCurrentWeather(city, WeatherService.API_KEY, currentUnit,
                currentLang).enqueue(new Callback<WeatherResponse>() {

            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    WeatherResponse weatherData = response.body();
                    cachedWeatherData = weatherData;
                    updateUI(weatherData);
                }else{
                    tvWeatherIcon.setImageResource(R.drawable.ic_weather_error);
                    tvCityTemp.setText(getString(R.string.toast_erro_api));

                    String erroMsg = getString(R.string.toast_erro_api) + " Código: " + response.code();
                    Toast.makeText(MainActivity.this, erroMsg, Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("API_ERROR", "Falha na conexão: " + t.getMessage());
                tvWeatherIcon.setImageResource(R.drawable.ic_weather_error);
                tvCityTemp.setText(getString(R.string.toast_sem_conexao));
                Toast.makeText(MainActivity.this, getString(R.string.toast_erro_rede),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateUI(WeatherResponse data) {
        String cityName = data.getCityName();
        String countryCode = data.getSys() != null ? data.getSys().getCountryCode() : "";


        // Define o símbolo da unidade
        String unitSymbol = currentUnit.equals(UNITS_METRIC) ? "°C" : "°F";

        String temp = String.format(Locale.getDefault(), "%.0f%s", data.getMain().getCurrentTemp(), unitSymbol);

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
                backgroundDrawable = R.drawable.gradient_chuva_leve; // Chuva forte

            } else {
                backgroundDrawable = R.drawable.gradient_chuvoso;
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