
package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;
import java.util.List;

import com.cranium.sushiteiapps.model.OutletCity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutletCities implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<OutletCity> outletCities = null;
    private final static long serialVersionUID = 8740856831733793062L;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<OutletCity> getOutletCities() {
        return outletCities;
    }

    public void setOutletCities(List<OutletCity> outletCities) {
        this.outletCities = outletCities;
    }

}
