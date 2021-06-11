
package com.cranium.sushiteiapps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Greeting implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("order")
    @Expose
    private String order;
    private final static long serialVersionUID = 8211544617349630714L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Greeting() {
    }

    /**
     * 
     * @param id
     * @param order
     * @param description
     * @param image
     */
    public Greeting(Integer id, String description, String image, String order) {
        super();
        this.id = id;
        this.description = description;
        this.image = image;
        this.order = order;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

}
