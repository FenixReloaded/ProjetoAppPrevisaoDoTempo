ClimaSphere - Aplicativo de Previsão do Tempo (Android Nativo)

(Sugestão: Use a sua imagem image_bc5da7.png como a imagem de cabeçalho do seu repositório)

🎯 Sobre o Projeto

O ClimaSphere é um aplicativo meteorológico funcional e robusto desenvolvido em Java para a plataforma Android Nativa. O projeto vai além de uma simples exibição de dados, focando em uma arquitetura de software escalável, personalização completa do usuário e internacionalização (i18n) dinâmica.

O aplicativo consome múltiplos endpoints da API OpenWeather, gerencia o estado do usuário de forma persistente através de SharedPreferences e implementa uma arquitetura de UI responsiva e temática que se adapta às condições climáticas.

Este projeto foi desenvolvido como um trabalho acadêmico, demonstrando a implementação de fluxos de dados assíncronos complexos, arquitetura de múltiplas Activitys e gerenciamento de estado em tempo de execução.

✨ Funcionalidades Implementadas

    Módulo de Previsão Estendida (7/15 Dias): Implementação de um fluxo de API encadeado. Ao selecionar um período, o app primeiro chama a API de Geocodificação para converter o nome da cidade em coordenadas (lat/lon) e, em seguida, chama a API de Previsão Diária (16 Dias). Os resultados são exibidos em uma nova tela (ForecastActivity) com uma lista RecyclerView.

    Internacionalização (i18n) Dinâmica: O aplicativo suporta três idiomas (Português, Inglês e Espanhol). A seleção de idioma na tela de configurações salva a preferência e reinicia o aplicativo (Intent.FLAG_ACTIVITY_CLEAR_TOP), forçando o sistema a carregar os arquivos de recursos (strings.xml) corretos.

    Arquitetura BaseActivity: Para suportar a internacionalização, uma classe abstract BaseActivity foi criada. Todas as Activitys principais herdam dela, garantindo que a lógica de aplicação de idioma (AppCompatDelegate.setApplicationLocales) seja executada antes de qualquer UI ser renderizada.

    Gerenciamento de Estado Persistente: A ConfiguracaoActivity permite ao usuário definir e salvar suas preferências de Unidade de Medida (Celsius/Fahrenheit) e Idioma. Essas preferências são salvas em SharedPreferences e lidas pela MainActivity e SelecaoPeriodoActivity para modificar as chamadas de API.

    Temas de UI Dinâmicos: A tela principal (MainActivity) analisa o weatherId e o iconId (ex: "01n") recebidos da API. Com base nessas condições, ela aplica Drawables de degradê customizados (ex: gradient_noite, gradient_chuvoso, gradient_neve) ao fundo da tela, oferecendo um feedback visual imersivo.

    Seleção de Cidade (com ActivityResultLauncher): O app implementa o fluxo moderno de comunicação entre Activitys. A MainActivity registra um launcher, e a SelecaoCidadeActivity captura a cidade (seja do EditText ou das sugestões) e retorna o dado para a MainActivity usando setResult(AppCompatActivity.RESULT_OK, ...).

    Design Responsivo (LinearLayout + layout_weight): Todos os layouts XML foram refatorados para serem responsivos. A técnica utiliza layout_width="match_parent" com layout_marginHorizontal para espaçamento, e um <View ... layout_weight="1" /> invisível para empurrar os botões de navegação para a parte inferior da tela, garantindo uma boa aparência em qualquer proporção de tela.

    Código Centralizado (DRY): A lógica de mapeamento de ícones da API (ex: "10d" -> R.drawable.ic_10d2x) foi extraída para uma classe utilitária, WeatherIconMapper.java, que é usada tanto pela MainActivity quanto pelo ForecastAdapter.

📱 Arquitetura e Telas

O aplicativo é composto por 6 Activitys principais e uma arquitetura de Adapter customizado.

    MainActivity (Home):

        Função: Tela principal e hub de navegação.

        Lógica: Chama getCurrentWeather, exibe os dados e aplica o tema de fundo dinâmico. Gerencia o ActivityResultLauncher para a seleção de cidade.

    DetalhesActivity:

        Função: Exibe informações detalhadas do clima atual (Sensação, Umidade, Pressão, etc.).

        Lógica: Recebe o objeto WeatherResponse serializado via Intent da MainActivity. Formata e exibe os dados.

    ConfiguracaoActivity:

        Função: Tela de configurações de Unidade e Idioma.

        Lógica: Atua como escritora principal das SharedPreferences. Gerencia o reinício do aplicativo (reiniciarApp()) após a troca de idioma.

    SelecaoCidadeActivity:

        Função: Tela de busca e seleção de cidade.

        Lógica: Captura a entrada do usuário (via EditText ou sugestões de TextView) e retorna a string da cidade para a MainActivity via setResult(RESULT_OK).

    SelecaoPeriodoActivity:

        Função: Tela de seleção de período (Semanal, Quinzenal, Mensal).

        Lógica: Inicia o fluxo de API encadeado. Recebe currentCity da MainActivity. Ao clique, chama getCoordinates e, no callback de sucesso, chama getDailyForecast.

    ForecastActivity:

        Função: Exibe a lista de previsão de 7 ou 15 dias.

        Lógica: Contém um RecyclerView. Recebe a List<DailyForecast> serializada da SelecaoPeriodoActivity e a configura com o ForecastAdapter.

    ForecastAdapter:

        Função: Adapter customizado que gerencia a lista do RecyclerView.

        Lógica: Recebe a lista de dados. Em onBindViewHolder, ele formata o timestamp Unix para uma data legível (ex: "03/11, Seg") e mapeia os ícones usando WeatherIconMapper.

🛠️ Tecnologias e Bibliotecas

    Linguagem: Java

    Plataforma: Android Nativo

    Comunicação de Rede: Retrofit 2

    Parsing de JSON: GSON (via GsonConverterFactory)

    UI (Listas): RecyclerView e RecyclerView.Adapter customizado (ForecastAdapter).

    UI (Layouts): LinearLayout com layout_weight e marginHorizontal para design responsivo.

    UI (Estilo): Drawables XML customizados (<shape>, <gradient>).

    API de Dados: OpenWeatherMap (endpoints Current Weather, Geocoding API e 16-Day Daily Forecast).

    Arquitetura: BaseActivity, ActivityResultLauncher, AppCompatDelegate (para i18n).

    Gerenciamento de Estado: SharedPreferences.

🚀 Como Executar o Projeto

    Clone o Repositório:
    Bash

git clone https://[URL_DO_SEU_REPOSITORIO_AQUI].git

Obtenha a Chave da API:

    Crie uma conta no OpenWeatherMap.

    Assine o plano (ex: "Free for students") que dá acesso às APIs: Current Weather, Geocoding e Daily Forecast 16 days.

    Obtenha sua chave (APPID).

Configure a Chave:

    Abra o projeto no Android Studio.

    Navegue até o arquivo app/src/main/java/com/puc/appclimasphere/api/WeatherService.java.

    Substitua a chave API_KEY pela sua chave real:
    Java

        public interface WeatherService {
            // ...
            String API_KEY = "SUA_CHAVE_AQUI"; 
            // ...
        }

    Sincronize e Execute:

        Sincronize o projeto (Sync Gradle).

        Execute o aplicativo em um emulador ou dispositivo físico (requer conexão com a internet).

📈 Trabalhos Futuros e Roadmap

Com base nos objetivos do projeto, a arquitetura atual permite as seguintes expansões futuras:

    Localização Automática: Implementar o FusedLocationProviderClient para usar o GPS do dispositivo para a busca inicial de clima, em vez de um padrão fixo ("São Paulo").

    Salvar Cidades Favoritas: Expandir as SharedPreferences ou implementar um banco de dados (Room/SQLite) para permitir que o usuário salve uma lista de cidades favoritas.

    Mapas e Gráficos: Utilizar a API "Weather Maps" para exibir camadas de mapa (precipitação, nuvens) e usar bibliotecas de gráficos (ex: MPAndroidChart) para plotar a previsão de 15 dias na ForecastActivity.

    Área Informativa (Educacional): Criar uma nova Activity ou BottomSheet para explicar conceitos meteorológicos, como "O que é o índice UV e seus níveis de risco".

    Notificações Climáticas: Implementar um WorkManager para tarefas em segundo plano que verifiquem alertas de mudanças climáticas relevantes e enviem notificações ao usuário.

    Seleção de Período Mensal (Plano Pago): Habilitar o botão "Mensal" para chamar uma API de 30 dias, caso o aplicativo seja atualizado para um plano de API superior.

🐞 Bugs Conhecidos

    Refatoração Incompleta da DetalhesActivity: A tela DetalhesActivity atualmente não herda da BaseActivity e usa texto fixo em seu método setMockData. Isso impede que essa tela específica seja traduzida dinamicamente com o resto do aplicativo.

    Pequeno ajuste na atribuição do ícones e fundos de tela em relação a condição meteriológica atual do local.
