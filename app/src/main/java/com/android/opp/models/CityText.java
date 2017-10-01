package com.android.opp.models;

import android.text.TextUtils;

/**
 * Created by camilo on 17/6/17.
 */

public class CityText {
    String latitude;
    String longitude;
    String name;
    String country;


    public CityText(String latitude) {
        this.latitude = latitude;
    }


    public CityText() {

     }

    public CityText(String latitude, String longitude, String name, String country) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {

        if(TextUtils.isEmpty(name)){
            return getCountry();

        }else{
            return name;

        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "CityText{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }


    public String toJson(){
        return "{'latitude':'"+latitude+"','longitude':'"+longitude+"','name':'"+name+"','country':'"+country+"'}";
    }
}
