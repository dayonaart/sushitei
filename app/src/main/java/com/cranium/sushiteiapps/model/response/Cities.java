package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;
import java.util.List;

import com.cranium.sushiteiapps.model.City;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cities implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("data")
    @Expose
    private List<City> cities = null;
    private final static long serialVersionUID = 8630868750456414670L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Cities() {
    }

    /**
     *
     * @param total
     * @param cities
     * @param status
     */
    public Cities(Integer status, Integer total, List<City> cities) {
        super();
        this.status = status;
        this.total = total;
        this.cities = cities;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

}