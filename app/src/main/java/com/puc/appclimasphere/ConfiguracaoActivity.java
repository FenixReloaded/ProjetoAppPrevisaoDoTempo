package com.puc.appclimasphere;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ConfiguracaoActivity extends BaseActivity {

    Button btnVoltarCon;
    private static final String EXTRA_TEMA_FUNDO = "TEMA_FUNDO";

    // Chaves públicas para que a MainActivity possa ler as preferências
    public static final String PREFS_NAME = "ClimaSpherePrefs";
    public static final String KEY_UNITS = "UNITS";
    // Valores que a API OpenWeather espera
    public static final String UNITS_METRIC = "metric"; // Celsius
    public static final String UNITS_IMPERIAL = "imperial"; // Fahrenheit

    private RadioGroup rgUnidade;
    private RadioButton rbCelsius, rbFahrenheit;
    private SharedPreferences sharedPreferences;

    public static final String KEY_LANG = "LANG";
    public static final String LANG_PT_BR = "pt_br";
    public static final String LANG_EN = "en";
    public static final String LANG_ES = "es";

    private Spinner spinnerIdioma;

    private String[] displayLanguages;
    private final String[] apiLangCodes = {LANG_PT_BR, LANG_EN, LANG_ES};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_configuracao);

        LinearLayout mainLayout = findViewById(R.id.config_main_layout);

        // Inicializa o SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // Inicializa o SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        displayLanguages = new String[] {
                getString(R.string.idioma_pt),
                getString(R.string.idioma_en),
                getString(R.string.idioma_es)
        };

        // IDs do R.layout.activity_configuracao
        rgUnidade = findViewById(R.id.rg_unidade);
        rbCelsius = findViewById(R.id.rb_celsius);
        rbFahrenheit = findViewById(R.id.rb_fahrenheit);
        spinnerIdioma = findViewById(R.id.spinner_idioma);

        // Configura o Adapter (a lista de itens) para o Spinner
        ArrayAdapter<String> langAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item, // Layout padrão
                displayLanguages // A lista de nomes
        );
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Layout padrão

        spinnerIdioma.setAdapter(langAdapter);

        // Carrega as configurações salvas e atualiza a UI
        loadSettings();

        // Adiciona o listener para salvar quando a seleção mudar
        rgUnidade.setOnCheckedChangeListener((group, checkedId) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (checkedId == R.id.rb_celsius) {
                editor.putString(KEY_UNITS, UNITS_METRIC);
            } else if (checkedId == R.id.rb_fahrenheit) {
                editor.putString(KEY_UNITS, UNITS_IMPERIAL);
            }
            editor.apply(); // Salva a alteração
        });

        // Define o Listener para salvar a escolha
        spinnerIdioma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLangCode = apiLangCodes[position];

                // Pega o idioma atualmente salvo
                String currentLang = sharedPreferences.getString(KEY_LANG, LANG_PT_BR);

                // Só reinicia se o usuário ESCOLHEU um idioma DIFERENTE
                if (!currentLang.equals(selectedLangCode)) {

                    // Salva o novo idioma
                    sharedPreferences.edit().putString(KEY_LANG, selectedLangCode).apply();

                    // Reinicia o app
                    reiniciarApp();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Recebe o tema dinâmico da intent
        if (getIntent().getExtras() != null) {

            // Pega o ID do recurso enviado pela Activity anterior
            int temaFundoId = getIntent().getIntExtra(EXTRA_TEMA_FUNDO, 0);

            // Verifica se o ID é válido antes de aplicar
            if (temaFundoId != 0 && mainLayout != null) {
                mainLayout.setBackgroundResource(temaFundoId);
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnVoltarCon = findViewById(R.id.btnVoltarCon);
        btnVoltarCon.setOnClickListener(v -> finish());

    }

    // Lê as preferências salvas e marca o RadioButton correto
    private void loadSettings() {
        // Lê a unidade salva. O valor padrão é "metric" (Celsius)
        String savedUnit = sharedPreferences.getString(KEY_UNITS, UNITS_METRIC);

        if (savedUnit.equals(UNITS_IMPERIAL)) {
            rbFahrenheit.setChecked(true);
        } else {
            rbCelsius.setChecked(true);
        }

        String savedLang = sharedPreferences.getString(KEY_LANG, LANG_PT_BR);

        // Encontra a posição do idioma salvo para marcar no Spinner
        int langPosition = 0; // Posição padrão (Português)
        for (int i = 0; i < apiLangCodes.length; i++) {
            if (apiLangCodes[i].equals(savedLang)) {
                langPosition = i;
                break;
            }
        }
        // Define o item selecionado no Spinner
        spinnerIdioma.setSelection(langPosition);
    }

    /**
     * Reinicia a aplicação para aplicar a mudança de idioma em todas as telas.
     */
    private void reiniciarApp() {
        Intent intent = new Intent(this, MainActivity.class);
        // Limpa todas as activities anteriores e inicia a MainActivity como nova
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        // Fecha esta activity e todas as outras
        finishAffinity();
    }
}