package com.taxi.clickcar.GeoData;

/**
 * Created by Назар on 22.04.2016.
 */
public class RootObject {
    private GeoStreets geo_streets;
    public RootObject(){}
    public GeoStreets getGeoStreets() { return this.geo_streets; }

    public void setGeoStreets(GeoStreets geo_streets) { this.geo_streets = geo_streets; }

    private GeoObjects geo_objects;

    public GeoObjects getGeoObjects() { return this.geo_objects; }

    public void setGeoObjects(GeoObjects geo_objects) { this.geo_objects = geo_objects; }
}
