package com.taxi.clickcar.GeoData;

import java.util.ArrayList;

/**
 * Created by Назар on 22.04.2016.
 */
public class GeoStreets {
    private ArrayList<GeoStreet> geo_street;

    public ArrayList<GeoStreet> getGeoStreet() { return this.geo_street; }

    public void setGeoStreet(ArrayList<GeoStreet> geo_street) { this.geo_street = geo_street; }

    private String version_date;

    public String getVersionDate() { return this.version_date; }

    public void setVersionDate(String version_date) { this.version_date = version_date; }

    private Object geo_data_info;

    public Object getGeoDataInfo() { return this.geo_data_info; }

    public void setGeoDataInfo(Object geo_data_info) { this.geo_data_info = geo_data_info; }
}
