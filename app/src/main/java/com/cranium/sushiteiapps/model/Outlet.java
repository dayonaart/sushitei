
package com.cranium.sushiteiapps.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Outlet implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("outlet_city_id")
    @Expose
    private String outletCityId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("order")
    @Expose
    private String order;
    @SerializedName("outlet_images")
    @Expose
    private List<OutletImage> outletImages = null;
    private final static long serialVersionUID = -4487822018717007721L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOutletCityId() {
        return outletCityId;
    }

    public void setOutletCityId(String outletCityId) {
        this.outletCityId = outletCityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public List<OutletImage> getOutletImages() {
        return outletImages;
    }

    public void setOutletImages(List<OutletImage> outletImages) {
        this.outletImages = outletImages;
    }

    public String getShortName() {
        return this.getCutDescription(this.getName(), 20);
    }
    private String getCutDescription(String description, Integer cut) {
        if (description.length() > cut) {
            return description.substring(0, cut) + "...";
        }

        return description;
    }

}
