package com.taxi.clickcar.GeoData;

import java.util.ArrayList;

/**
 * Created by Назар on 22.04.2016.
 */
public class GeoStreet {
    private String name;

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    private String old_name;

    public String getOldName() { return this.old_name; }

    public void setOldName(String old_name) { this.old_name = old_name; }

    private ArrayList<House> houses;

    public ArrayList<House> getHouses() { return this.houses; }

    public void setHouses(ArrayList<House> houses) { this.houses = houses; }

    private String locale;

    public String getLocale() { return this.locale; }

    public void setLocale(String locale) { this.locale = locale; }
}
