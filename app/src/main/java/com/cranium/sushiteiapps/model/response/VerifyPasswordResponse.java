
package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyPasswordResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    private final static long serialVersionUID = -3090408316723862132L;

    /**
     * No args constructor for use in serialization
     *
     */
    public VerifyPasswordResponse() {
    }

    /**
     *
     * @param message
     * @param status
     * @param success
     */
    public VerifyPasswordResponse(Integer status, Boolean success, String message) {
        super();
        this.status = status;
        this.success = success;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}