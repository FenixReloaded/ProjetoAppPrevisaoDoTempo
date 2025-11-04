package com.puc.appclimasphere.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WeatherResponse implements Serializable {
    @SerializedName("name")
    private String cityName;
    @SerializedName("main")
    private Main main;
    @SerializedName("weather")
    private List<Weather> weather;
    @SerializedName("sys")
    private Sys sys;

    public String getCityName() {
        return cityName;
    }

    public Main getMain() {
        return main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public Sys getSys() {
        return sys;
    }
}
