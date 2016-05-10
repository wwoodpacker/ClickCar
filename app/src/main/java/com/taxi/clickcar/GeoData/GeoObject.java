package com.taxi.clickcar.GeoData;

/**
 * Created by Назар on 22.04.2016.
 */
public class GeoObject {
    private String name;

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    private double lat;

    public double getLat() { return this.lat; }

    public void setLat(double lat) { this.lat = lat; }

    private double lng;

    public double getLng() { return this.lng; }

    public void setLng(double lng) { this.lng = lng; }

    private String locale;

    public String getLocale() { return this.locale; }

    public void setLocale(String locale) { this.locale = locale; }
}
