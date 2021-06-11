
package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;
import java.util.List;

import com.cranium.sushiteiapps.model.Wishlist;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wishlists implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("data")
    @Expose
    private List<Wishlist> wishlists = null;
    @SerializedName("message")
    @Expose
    private String message;

    private final static long serialVersionUID = -5877819148361969269L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Wishlists() {
    }

    /**
     *
     * @param total
     * @param status
     * @param wishlists
     */
    public Wishlists(Integer status, Integer total, List<Wishlist> wishlists) {
        super();
        this.status = status;
        this.total = total;
        this.wishlists = wishlists;
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

    public List<Wishlist> getWishlists() {
        return wishlists;
    }

    public void setWishlists(List<Wishlist> wishlists) {
        this.wishlists = wishlists;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}