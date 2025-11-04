package com.puc.appclimasphere.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

// Mapeia o bloco "sys" (usado para código do país)
public class Sys implements Serializable {
    @SerializedName("country")
    private String countryCode;  // Código do país (e.g., "IT", "BR")

    public String getCountryCode() {
        return countryCode;
    }
}
