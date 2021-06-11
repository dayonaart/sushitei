package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;

import com.cranium.sushiteiapps.model.ForgotPassword;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ForgotPassword forgotPassword;
    private final static long serialVersionUID = 5649096673638243047L;

    /**
     * No args constructor for use in serialization
     *
     */
    public ForgotPasswordResponse() {
    }

    /**
     *
     * @param message
     * @param status
     * @param forgotPassword
     */
    public ForgotPasswordResponse(Integer status, String message, ForgotPassword forgotPassword) {
        super();
        this.status = status;
        this.message = message;
        this.forgotPassword = forgotPassword;
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

    public ForgotPassword getForgotPassword() {
        return forgotPassword;
    }

    public void setForgotPassword(ForgotPassword forgotPassword) {
        this.forgotPassword = forgotPassword;
    }

}