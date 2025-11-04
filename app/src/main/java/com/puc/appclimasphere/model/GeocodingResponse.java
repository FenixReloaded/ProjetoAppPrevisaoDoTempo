package com.puc.appclimasphere.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Mapeia a resposta da API de Geocodificação.
 * A API retorna um array JSON, que será tratado como uma Lista da classe (List<GeocodingResponse>).
 */
public class GeocodingResponse implements Serializable {
    @SerializedName("lat")
    private double latitude;

    @SerializedName("lon")
    private double longitude;

    @SerializedName("name")
    private String name;

    @SerializedName("country")
    private String country;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }
}
