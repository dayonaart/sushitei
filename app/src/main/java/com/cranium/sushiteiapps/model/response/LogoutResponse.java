package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogoutResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

//    @SerializedName("message")
//    @Expose
//    private String message;

    private final static long serialVersionUID = -8672644857404702052L;

    /**
     * No args constructor for use in serialization
     *
     */
    public LogoutResponse() {
    }

    /**
     *
     * @param message
     * @param status
     */
    public LogoutResponse(Integer status, String message) {
        super();
        this.status = status;
        this.message = message;
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

}