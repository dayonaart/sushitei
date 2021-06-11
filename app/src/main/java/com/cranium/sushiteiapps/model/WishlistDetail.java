
package com.cranium.sushiteiapps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WishlistDetail implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("wishlist_id")
    @Expose
    private String wishlistId;
    @SerializedName("menu_id")
    @Expose
    private String menuId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("menu")
    @Expose
    private Menu menu;
    private final static long serialVersionUID = 5510690548823948521L;

    /**
     * No args constructor for use in serialization
     *
     */
    public WishlistDetail() {
    }

    /**
     *
     * @param updatedAt
     * @param id
     * @param createdAt
     * @param menu
     * @param menuId
     * @param wishlistId
     */
    public WishlistDetail(Integer id, String wishlistId, String menuId, String createdAt, String updatedAt, Menu menu) {
        super();
        this.id = id;
        this.wishlistId = wishlistId;
        this.menuId = menuId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.menu = menu;
    }

    /**
     *
     * @param id
     * @param updatedAt
     * @param createdAt
     * @param menuId
     * @param wishlistId
     */
    public WishlistDetail(String menuId, String wishlistId, String updatedAt, String createdAt, Integer id) {
        super();
        this.menuId = menuId;
        this.wishlistId = wishlistId;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(String wishlistId) {
        this.wishlistId = wishlistId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
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

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

}