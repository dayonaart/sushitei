
package com.cranium.sushiteiapps.model.request;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddWishlistMenuRequest implements Serializable
{

    @SerializedName("wishlist_id")
    @Expose
    private Integer wishlistId;
    @SerializedName("menu_id")
    @Expose
    private Integer menuId;
    private final static long serialVersionUID = -8309367238609209985L;

    /**
     * No args constructor for use in serialization
     *
     */
    public AddWishlistMenuRequest() {
    }

    /**
     *
     * @param menuId
     * @param wishlistId
     */
    public AddWishlistMenuRequest(Integer wishlistId, Integer menuId) {
        super();
        this.wishlistId = wishlistId;
        this.menuId = menuId;
    }

    public Integer getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Integer wishlistId) {
        this.wishlistId = wishlistId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

}