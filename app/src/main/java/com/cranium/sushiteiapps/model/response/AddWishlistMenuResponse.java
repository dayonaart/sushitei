
package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;

import com.cranium.sushiteiapps.model.WishlistDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddWishlistMenuResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("wishlist_detail")
    @Expose
    private WishlistDetail wishlistDetail;
    private final static long serialVersionUID = 3691375285940573477L;

    /**
     * No args constructor for use in serialization
     *
     */
    public AddWishlistMenuResponse() {
    }

    /**
     *
     * @param message
     * @param wishlistDetail
     * @param status
     */
    public AddWishlistMenuResponse(Integer status, String message, WishlistDetail wishlistDetail) {
        super();
        this.status = status;
        this.message = message;
        this.wishlistDetail = wishlistDetail;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public WishlistDetail getWishlistDetail() {
        return wishlistDetail;
    }

    public void setWishlistDetail(WishlistDetail wishlistDetail) {
        this.wishlistDetail = wishlistDetail;
    }

}