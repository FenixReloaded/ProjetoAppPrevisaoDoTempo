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

public class ConfiguracaoActivity extends AppCompatActivity {

    Button btnIrParaSelecao, btnVoltarCon;
    private static final String EXTRA_TEMA_FUNDO = "TEMA_FUNDO";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_configuracao);

        LinearLayout mainLayout = findViewById(R.id.config_main_layout);

        // 1. RECEBE O TEMA DINÂMICO DA INTENT
        if (getIntent().getExtras() != null) {
            // Pega o ID do recurso (o R.drawable.xxx) enviado pela Activity anterior.
            int temaFundoId = getIntent().getIntExtra(EXTRA_TEMA_FUNDO, 0);

            // Verifica se o ID é válido (diferente de 0) antes de aplicar
            if (temaFundoId != 0 && mainLayout != null) {
                mainLayout.setBackgroundResource(temaFundoId);
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        btnIrParaSelecao = findViewById(R.id.btn_navegar_selecao_cidade);
//
//        btnIrParaSelecao.setOnClickListener(v -> {
//            Intent intent = new Intent(ConfiguracaoActivity.this, SelecaoCidadeActivity.class);
//            startActivity(intent);
//        });

        btnVoltarCon = findViewById(R.id.btnVoltarCon);
        btnVoltarCon.setOnClickListener(v -> finish());

    }
}