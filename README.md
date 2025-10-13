# WeatherApp (Protótipo)

Protótipo Android (Java) com 5 telas navegáveis.

## Requisitos
- Android Studio Iguana ou mais recente
- JDK 17

## Abrir e Executar
1. Abra o Android Studio
2. File > Open > selecione a pasta `workspace`
3. Aguarde o sync do Gradle
4. Run > Run 'app' em um emulador API 24+ ou dispositivo

## Telas
- MainActivity: cidade e clima atual
- ForecastDetailActivity: detalhes da previsão (lista)
- SettingsActivity: configurações (atalho para seleção)
- SelectCityActivity: seleção de cidade
- SelectPeriodActivity: seleção de período (semanal/quinzenal/mensal)

## Estrutura
- `app/src/main/java/com/example/weatherapp/ui`: Activities e adapter
- `app/src/main/res/layout`: XMLs das telas
- `docs/`: Regras de Negócio e Outline da apresentação

## Próximos Passos (Fase 02)
- Persistência (SharedPreferences)
- Integração com API de clima
- Testes instrumentados
