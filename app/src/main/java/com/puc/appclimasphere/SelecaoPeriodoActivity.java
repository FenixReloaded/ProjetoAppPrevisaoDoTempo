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

public class SelecaoPeriodoActivity extends BaseActivity {

    Button btnVoltarPer;
    private static final String EXTRA_TEMA_FUNDO = "TEMA_FUNDO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_selecao_periodo);

        LinearLayout mainLayout = findViewById(R.id.periodo_main_layout);

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

        btnVoltarPer= findViewById(R.id.btnVoltarPer);
        btnVoltarPer.setOnClickListener(v -> finish());
    }
}