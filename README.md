# ProjetoAppPrevisaoDoTempo

☁️ ClimaSphere - Protótipo de Aplicativo de Previsão do Tempo (Android Nativo)

🎯 Sobre o Projeto

O ClimaSphere é um protótipo de aplicativo mobile desenvolvido em Java/Android Nativo focado em oferecer uma experiência visual dinâmica e uma navegação clara entre as informações de previsão do tempo.

Este projeto foi desenvolvido como parte do projeto da matéria DAM, com ênfase na arquitetura de múltiplas Activitys e na integração com serviços externos (API).

✨ Funcionalidades Destaque

    Tema Dinâmico Dia/Noite: O fundo da tela principal se adapta automaticamente ao clima e ao horário atual (dia ou noite) da cidade, utilizando degradês personalizados.

    Integração com API: Utiliza a OpenWeatherMap API para buscar dados reais e atualizar a interface do usuário em tempo real.

    Navegação Complexa: Apresenta 5 telas navegáveis, demonstrando o fluxo completo da aplicação.

    Design Customizado: Implementação de estilos personalizados (drawables) para botões e cartões de informação, superando conflitos de tema do Android.

📱 Telas e Navegação

O protótipo é composto por 5 Activitys principais:

    MainActivity (Home): Exibe o clima atual (ícone e temperatura) e gerencia a navegação para as demais telas.

    DetalhesActivity: Exibe dados meteorológicos detalhados (umidade, pressão, sensação térmica, min/máx) recebidos via serialização (Serializable) da API.

    ConfiguracaoActivity: Permite ajustes em unidades de medida e idioma (protótipo funcional).

    SelecaoCidadeActivity: Simula o campo de busca e seleção de uma nova cidade.

    SelecaoPeriodoActivity: Permite a seleção do horizonte temporal da previsão (Semanal/Quinzenal/Mensal).

🛠️ Tecnologias Utilizadas

    Linguagem: Java

    Plataforma: Android Nativo (Android Studio)

    Comunicação de Rede: Retrofit 2.x e GSON

    API de Dados: OpenWeatherMap API (Current Weather)

    Estilização: Arquivos XML drawable customizados (<shape>, <gradient>) para degradês e arredondamento.

    Organização: Uso do ciclo de vida (onResume()) para otimizar a atualização de dados e minimizar o efeito de "piscada" (flicker).

🚀 Como Rodar o Projeto

    Clone o Repositório:

    git clone [https://www.youtube.com/watch?v=6YQIWRyPxnk](https://www.youtube.com/watch?v=6YQIWRyPxnk)

    Obtenha a Chave da API:

        Crie uma conta gratuita no OpenWeatherMap.

        Obtenha sua chave (APPID).

    Configure a Chave:

        Abra o arquivo WeatherService.java (localizado em .../api/).

        Substitua "SUA_CHAVE_AQUI" pela sua chave real:

        public interface WeatherService {
            // ...
            String API_KEY = "SUA_CHAVE_AQUI"; 
            // ...
        }

    Sincronize e Execute:

        Sincronize o projeto no Android Studio (Sync Now).

        Rode o aplicativo em um emulador ou dispositivo físico (é necessário acesso à internet para a API funcionar).

🤝 Contribuições

Este é um projeto de protótipo inicial. Sugestões e melhorias são bem-vindas, especialmente em relação à:

    Refatoração para o uso de ViewModel e LiveData.

    Migração dos layouts para ConstraintLayout para maior flexibilidade.

    Implementação de uma barra de progresso (loading spinner) durante a busca da API.
