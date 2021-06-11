
package com.cranium.sushiteiapps.model.request;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddWishlistRequest implements Serializable
{

    @SerializedName("name")
    @Expose
    private String name;
    private final static long serialVersionUID = -1344179401511109694L;

    /**
     * No args constructor for use in serialization
     *
     */
    public AddWishlistRequest() {
    }

    /**
     *
     * @param name
     */
    public AddWishlistRequest(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}