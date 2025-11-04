package com.puc.appclimasphere.api;

import com.puc.appclimasphere.model.GeocodingResponse;
import com.puc.appclimasphere.model.ForecastResponse;
import com.puc.appclimasphere.model.WeatherResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    String BASE_URL = "https://api.openweathermap.org/";

    String API_KEY = ""; // Chave da API
    String UNITS = "metric";
    String LANG = "pt_br";

    // Endpoint de Clima Atual
    @GET("data/2.5/weather")
    Call<WeatherResponse> getCurrentWeather(
            @Query("q") String city,
            @Query("appid") String apiKey,
            @Query("units") String units,
            @Query("lang") String lang
    );

    /**
     * Converte um nome de cidade em coordenadas (Latitude/Longitude).
     * Retorna uma Lista de resultados, mas vamos usar apenas o primeiro.
     */
    // Endpoint de Geocodificação
    @GET("geo/1.0/direct")
    Call<List<GeocodingResponse>> getCoordinates(
            @Query("q") String city,
            @Query("limit") int limit, // limitado a 1 resultado
            @Query("appid") String apiKey
    );

    /**
     * Busca a previsão diária (até 16 dias).
     * Exige lat/lon, units e lang (que leremos das Configurações).
     * 'cnt' é o número de dias que queremos (7 ou 15).
     */
    // Endpoint da Previsão
    @GET("data/2.5/forecast/daily")
    Call<ForecastResponse> getDailyForecast(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("cnt") int daysCount, // Número de dias (7 ou 15)
            @Query("appid") String apiKey,
            @Query("units") String units, // "metric" ou "imperial"
            @Query("lang") String lang // "pt_br", "en", etc.
    );

}