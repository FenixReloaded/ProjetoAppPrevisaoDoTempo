package com.puc.appclimasphere;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SelecaoCidadeActivity extends AppCompatActivity {

    Button btnVoltarSel;
    private static final String EXTRA_TEMA_FUNDO = "TEMA_FUNDO";
    public static final String EXTRA_NEW_CITY = "NEW_CITY";

    EditText etCidadeBusca;
    Button btnBuscar;
    TextView tvSugestao1, tvSugestao2, tvSugestao3, tvSugestao4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_selecao_cidade);

        LinearLayout mainLayout = findViewById(R.id.cidade_main_layout);

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

        // Você precisa inicializar as variáveis antes de usá-las.
        // Assumindo que os IDs no seu XML são os que discutimos:

        etCidadeBusca = findViewById(R.id.et_cidade_busca);
        btnBuscar = findViewById(R.id.btn_buscar);

        tvSugestao1 = findViewById(R.id.tv_sugestao_1);
        tvSugestao2 = findViewById(R.id.tv_sugestao_2);
        tvSugestao3 = findViewById(R.id.tv_sugestao_3);
        tvSugestao4 = findViewById(R.id.tv_sugestao_4);


        btnVoltarSel = findViewById(R.id.btnVoltarPer);
        btnVoltarSel.setOnClickListener(v -> finish());

        // Ação do botão "Buscar"
        btnBuscar.setOnClickListener(v -> {
            String cidade = etCidadeBusca.getText().toString().trim();
            if (!cidade.isEmpty()) {
                // Se o texto não estiver vazio, retorna a cidade
                retornarCidadeSelecionada(cidade);
            } else {
                // Opcional: mostrar erro se o campo estiver vazio
                etCidadeBusca.setError("Digite o nome da cidade");
            }
        });

        // 4. Ações das cidades sugeridas (com base nos textos da imagem)
        // Usamos null-checks caso você remova alguma sugestão do layout
        if (tvSugestao1 != null) {
            tvSugestao1.setOnClickListener(v -> retornarCidadeSelecionada("São Paulo"));
        }
        if (tvSugestao2 != null) {
            tvSugestao2.setOnClickListener(v -> retornarCidadeSelecionada("Rio de Janeiro"));
        }
        if (tvSugestao3 != null) {
            tvSugestao3.setOnClickListener(v -> retornarCidadeSelecionada("Porto Alegre"));
        }
        if (tvSugestao4 != null) {
            tvSugestao4.setOnClickListener(v -> retornarCidadeSelecionada("Londres"));
        }
    }

    /**
     * Prepara o resultado (a cidade selecionada) e fecha esta activity.
     * @param cidade O nome da cidade a ser retornada para a MainActivity.
     */
    private void retornarCidadeSelecionada(String cidade) {
        // Cria um Intent para guardar o resultado
        Intent resultIntent = new Intent();

        // Coloca o nome da cidade no Intent
        resultIntent.putExtra(EXTRA_NEW_CITY, cidade);

        // Define o resultado como SUCESSO (RESULT_OK) e envia o Intent
        setResult(Activity.RESULT_OK, resultIntent);

        // Fecha a SelecaoCidadeActivity e retorna para a MainActivity
        finish();
    }
}