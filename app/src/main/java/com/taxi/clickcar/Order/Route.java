package com.taxi.clickcar.Order;

/**
 * Created by Назар on 22.04.2016.
 */
public class Route
{
    public Route(){}
    private String name;

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    private String number;

    public String getNumber() { return this.number; }

    public void setNumber(String number) { this.number = number; }

    private Double lat;

    public Double getLat() { return this.lat; }

    public void setLat(Double lat) { this.lat = lat; }

    private Double lng;

    public Double getLng() { return this.lng; }

    public void setLng(Double lng) { this.lng = lng; }
}
