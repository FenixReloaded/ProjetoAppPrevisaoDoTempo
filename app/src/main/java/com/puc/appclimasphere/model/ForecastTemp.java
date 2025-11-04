package com.puc.appclimasphere.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

// classe que armazena as diferentes temperaturas do dia
public class ForecastTemp implements Serializable {
    @SerializedName("min")
    private double minTemp;

    @SerializedName("max")
    private double maxTemp;

    public double getMinTemp() {
        return minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }
}
