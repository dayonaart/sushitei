
package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageUpdated implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("messsage")
    @Expose
    private String messsage;
    private final static long serialVersionUID = 3862617593540678227L;

    /**
     * No args constructor for use in serialization
     *
     */
    public MessageUpdated() {
    }

    /**
     *
     * @param messsage
     * @param status
     */
    public MessageUpdated(Integer status, String messsage) {
        super();
        this.status = status;
        this.messsage = messsage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMesssage() {
        return messsage;
    }

    public void setMesssage(String messsage) {
        this.messsage = messsage;
    }

}