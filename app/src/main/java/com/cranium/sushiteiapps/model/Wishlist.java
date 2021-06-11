
package com.cranium.sushiteiapps.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wishlist implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("wishlist_details")
    @Expose
    private List<WishlistDetail> wishlistDetails = null;
    private final static long serialVersionUID = 8460792209551318462L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Wishlist() {
    }

    /**
     *
     * @param updatedAt
     * @param id
     * @param createdAt
     * @param name
     */
    public Wishlist(Integer id, String name, String createdAt, String updatedAt) {
        super();
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     *
     * @param updatedAt
     * @param id
     * @param createdAt
     * @param name
     * @param wishlistDetails
     */
    public Wishlist(Integer id, String name, String createdAt, String updatedAt, List<WishlistDetail> wishlistDetails) {
        super();
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.wishlistDetails = wishlistDetails;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<WishlistDetail> getWishlistDetails() {
        return wishlistDetails;
    }

    public void setWishlistDetails(List<WishlistDetail> wishlistDetails) {
        this.wishlistDetails = wishlistDetails;
    }

}