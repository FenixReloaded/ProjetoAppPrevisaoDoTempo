package com.example.weatherapp.data;

import java.util.Arrays;
import java.util.List;

public class MockWeatherRepository {
    public String getCurrentWeather(String city) {
        return "25°C, Ensolarado";
    }

    public List<String> getWeeklyForecast(String city) {
        return Arrays.asList(
                "Seg - 25°C - Ensolarado",
                "Ter - 23°C - Parcialmente nublado",
                "Qua - 22°C - Chuva leve",
                "Qui - 24°C - Ensolarado",
                "Sex - 26°C - Ensolarado",
                "Sáb - 27°C - Ensolarado",
                "Dom - 24°C - Nublado"
        );
    }
}
