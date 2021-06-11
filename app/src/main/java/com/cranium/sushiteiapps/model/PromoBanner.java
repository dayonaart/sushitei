package com.cranium.sushiteiapps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromoBanner implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("order")
    @Expose
    private String order;
    private final static long serialVersionUID = 8526484945950372861L;

    /**
     * No args constructor for use in serialization
     *
     */
    public PromoBanner() {
    }

    /**
     *
     * @param id
     * @param startDate
     * @param order
     * @param name
     * @param image
     * @param endDate
     */
    public PromoBanner(Integer id, String name, String image, String startDate, String endDate, String order) {
        super();
        this.id = id;
        this.name = name;
        this.image = image;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

}