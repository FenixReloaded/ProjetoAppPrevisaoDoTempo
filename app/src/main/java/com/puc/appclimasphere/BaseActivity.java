package com.puc.appclimasphere;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import static com.puc.appclimasphere.ConfiguracaoActivity.PREFS_NAME;
import static com.puc.appclimasphere.ConfiguracaoActivity.KEY_LANG;
import static com.puc.appclimasphere.ConfiguracaoActivity.LANG_PT_BR;


 // Uma Activity base que aplica automaticamente o idioma salvo antes que qualquer layout seja carregado
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // Aplica o idioma antes de chamar super.onCreate() ou setContentView()
        aplicarIdiomaSalvo();
        super.onCreate(savedInstanceState);
    }

    // Lê o SharedPreferences e define o idioma do app
    private void aplicarIdiomaSalvo() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedLang = sharedPreferences.getString(KEY_LANG, LANG_PT_BR);

        // Mapeia o código da API (pt_br) para o código do Locale (pt-BR)
        if (savedLang.equals("pt_br")) {
            savedLang = "pt-BR"; // O Android usa o formato BCP 47
        }

        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(savedLang);
        AppCompatDelegate.setApplicationLocales(appLocale);
    }

}
