package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;
import java.util.List;

import com.cranium.sushiteiapps.model.PromoBanner;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromoBanners implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<PromoBanner> promoBanners = null;
    private final static long serialVersionUID = -7732072523819719388L;

    /**
     * No args constructor for use in serialization
     *
     */
    public PromoBanners() {
    }

    /**
     *
     * @param status
     * @param promoBanners
     */
    public PromoBanners(Integer status, List<PromoBanner> promoBanners) {
        super();
        this.status = status;
        this.promoBanners = promoBanners;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<PromoBanner> getPromoBanners() {
        return promoBanners;
    }

    public void setPromoBanners(List<PromoBanner> promoBanners) {
        this.promoBanners = promoBanners;
    }

}