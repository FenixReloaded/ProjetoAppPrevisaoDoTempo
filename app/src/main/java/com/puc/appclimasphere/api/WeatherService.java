package com.puc.appclimasphere.api;

import com.puc.appclimasphere.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    String API_KEY = "";
    String UNITS = "metric";
    String LANG = "pt_br";


    @GET("weather")
    Call<WeatherResponse> getCurrentWeather(
            @Query("q") String city,
            @Query("appid") String apiKey,
            @Query("units") String units,
            @Query("lang") String lang
    );
}