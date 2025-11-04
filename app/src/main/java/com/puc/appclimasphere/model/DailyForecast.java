package com.puc.appclimasphere.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

// A classe representa um dia na lista de previsão
public class DailyForecast implements Serializable {
    @SerializedName("dt")
    private long timestamp; // Data/hora em formato Unix UTC

    @SerializedName("temp")
    private ForecastTemp temp;

    @SerializedName("humidity")
    private int humidity;

    @SerializedName("pressure")
    private int pressure;

    @SerializedName("weather")
    private List<Weather> weather; // Reutilizando a classe Weather.java

    public long getTimestamp() {
        return timestamp;
    }

    public ForecastTemp getTemp() {
        return temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public List<Weather> getWeather() {
        return weather;
    }
}
