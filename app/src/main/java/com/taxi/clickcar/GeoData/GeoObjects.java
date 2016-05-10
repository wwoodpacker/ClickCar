package com.taxi.clickcar.GeoData;

import java.util.ArrayList;

/**
 * Created by Назар on 22.04.2016.
 */
public class GeoObjects {
    private ArrayList<GeoObject> geo_object;

    public ArrayList<GeoObject> getGeoObject() { return this.geo_object; }

    public void setGeoObject(ArrayList<GeoObject> geo_object) { this.geo_object = geo_object; }

    private String version_date;

    public String getVersionDate() { return this.version_date; }

    public void setVersionDate(String version_date) { this.version_date = version_date; }

    private Object geo_data_info;

    public Object getGeoDataInfo() { return this.geo_data_info; }

    public void setGeoDataInfo(Object geo_data_info) { this.geo_data_info = geo_data_info; }
}
