package com.cranium.sushiteiapps.model.response;

import com.cranium.sushiteiapps.model.Register;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegisterResponseError implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    private final static long serialVersionUID = 6530932946796725508L;

    /**
     * No args constructor for use in serialization
     *
     */
    public RegisterResponseError() {
    }

    /**
     *
     * @param message
     * @param status
     */
    public RegisterResponseError(Integer status, String message) {
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