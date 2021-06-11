
package com.cranium.sushiteiapps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

public class MenuWishlist implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("menu_category_id")
    @Expose
    private String menuCategoryId;
    @SerializedName("menu_id")
    @Expose
    private Integer menuId;
    @SerializedName("is_wishlist")
    @Expose
    private Integer isWishlist;
    private final static long serialVersionUID = 6658543176581999825L;

    /**
     * No args constructor for use in serialization
     *
     */
    public MenuWishlist() {
    }

    public MenuWishlist(Integer id, Integer menuId, Integer isWishlist) {
        this.id = id;
        this.menuId = menuId;
        this.isWishlist = isWishlist;
    }

    public MenuWishlist(Integer id, String menuCategoryId, Integer menuId, Integer isWishlist) {
        this.id = id;
        this.menuCategoryId = menuCategoryId;
        this.menuId = menuId;
        this.isWishlist = isWishlist;
    }

    public MenuWishlist(String menuCategoryId, Integer menuId, Integer isWishlist) {
        this.menuCategoryId = menuCategoryId;
        this.menuId = menuId;
        this.isWishlist = isWishlist;
    }

    public String getMenuCategoryId() {
        return menuCategoryId;
    }

    public void setMenuCategoryId(String menuCategoryId) {
        this.menuCategoryId = menuCategoryId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getIsWishlist() {
        return isWishlist;
    }

    public void setIsWishlist(Integer isWishlist) {
        this.isWishlist = isWishlist;
    }
}
