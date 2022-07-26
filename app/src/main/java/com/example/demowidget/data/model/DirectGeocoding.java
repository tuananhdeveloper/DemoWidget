package com.example.demowidget.data.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DirectGeocoding implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("local_names")
    @Expose
    private LocalNames localNames;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lon")
    @Expose
    private Double lon;
    @SerializedName("country")
    @Expose
    private String country;

    /**
     * No args constructor for use in serialization
     * 
     */
    public DirectGeocoding() {
    }

    /**
     * 
     * @param localNames
     * @param country
     * @param name
     * @param lon
     * @param lat
     */
    public DirectGeocoding(String name, LocalNames localNames, Double lat, Double lon, String country) {
        super();
        this.name = name;
        this.localNames = localNames;
        this.lat = lat;
        this.lon = lon;
        this.country = country;
    }

    public String getName() {
        return "Name: " + name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DirectGeocoding withName(String name) {
        this.name = name;
        return this;
    }

    public LocalNames getLocalNames() {
        return localNames;
    }

    public void setLocalNames(LocalNames localNames) {
        this.localNames = localNames;
    }

    public DirectGeocoding withLocalNames(LocalNames localNames) {
        this.localNames = localNames;
        return this;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public DirectGeocoding withLat(Double lat) {
        this.lat = lat;
        return this;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public DirectGeocoding withLon(Double lon) {
        this.lon = lon;
        return this;
    }

    public String getCountry() {
        return "Country: " + country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public DirectGeocoding withCountry(String country) {
        this.country = country;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(DirectGeocoding.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("localNames");
        sb.append('=');
        sb.append(((this.localNames == null)?"<null>":this.localNames));
        sb.append(',');
        sb.append("lat");
        sb.append('=');
        sb.append(((this.lat == null)?"<null>":this.lat));
        sb.append(',');
        sb.append("lon");
        sb.append('=');
        sb.append(((this.lon == null)?"<null>":this.lon));
        sb.append(',');
        sb.append("country");
        sb.append('=');
        sb.append(((this.country == null)?"<null>":this.country));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
