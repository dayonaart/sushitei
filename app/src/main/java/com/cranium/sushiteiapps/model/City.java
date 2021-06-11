package com.cranium.sushiteiapps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("is_outlet")
    @Expose
    private String isOutlet;
    @SerializedName("order")
    @Expose
    private String order;
    private final static long serialVersionUID = 4708800936501419243L;

    /**
     * No args constructor for use in serialization
     *
     */
    public City() {
    }

    /**
     *
     * @param id
     * @param order
     * @param name
     * @param longitude
     * @param latitude
     * @param isOutlet
     */
    public City(Integer id, String name, String latitude, String longitude, String isOutlet, String order) {
        super();
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isOutlet = isOutlet;
        this.order = order;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getIsOutlet() {
        return isOutlet;
    }

    public void setIsOutlet(String isOutlet) {
        this.isOutlet = isOutlet;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

}