package com.puc.appclimasphere.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Main implements Serializable {

    @SerializedName("temp")
    private double currentTemp;

    @SerializedName("feels_like")
    private double feelsLike;

    @SerializedName("temp_min")
    private Double minTemp;

    @SerializedName("temp_max")
    private Double maxTemp;

    @SerializedName("humidity")
    private int humidity;

    @SerializedName("pressure")
    private int pressure;


    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public double getCurrentTemp() {
        return currentTemp;
    }


    public double getMinTemp() {
        return minTemp != null ? minTemp : getCurrentTemp();
    }

    public double getMaxTemp() {
        return maxTemp != null ? maxTemp : getCurrentTemp();
    }
}
