
package com.cranium.sushiteiapps.model.request;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileImageRequest implements Serializable
{

    @SerializedName("image_base64")
    @Expose
    private String imageBase64;
    private final static long serialVersionUID = 7020295491712504710L;

    /**
     * No args constructor for use in serialization
     *
     */
    public ProfileImageRequest() {
    }

    /**
     *
     * @param imageBase64
     */
    public ProfileImageRequest(String imageBase64) {
        super();
        this.imageBase64 = imageBase64;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

}