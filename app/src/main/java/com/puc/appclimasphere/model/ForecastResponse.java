package com.puc.appclimasphere.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

// classe "raiz" que receberá a resposta completa da API
public class ForecastResponse implements Serializable {

    // A resposta JSON tem um objeto "list" que é um array de previsões diárias
    @SerializedName("list")
    private List<DailyForecast> forecastList;

    public List<DailyForecast> getForecastList() {
        return forecastList;
    }
}
