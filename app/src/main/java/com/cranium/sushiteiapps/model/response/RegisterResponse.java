package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;

import com.cranium.sushiteiapps.model.Register;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Register register;
    private final static long serialVersionUID = 4589407147375488365L;

    /**
     * No args constructor for use in serialization
     *
     */
    public RegisterResponse() {
    }

    /**
     *
     * @param register
     * @param message
     * @param status
     */
    public RegisterResponse(Integer status, String message, Register register) {
        super();
        this.status = status;
        this.message = message;
        this.register = register;
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

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

}