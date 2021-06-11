package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactResponseError implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("validators")
    @Expose
    private ContactError contactError;
    private final static long serialVersionUID = 1356635906270984616L;

    /**
     * No args constructor for use in serialization
     *
     */
    public ContactResponseError() {
    }

    /**
     *
     * @param message
     * @param contactError
     * @param status
     */
    public ContactResponseError(Integer status, String message, ContactError contactError) {
        super();
        this.status = status;
        this.message = message;
        this.contactError = contactError;
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

    public ContactError getContactError() {
        return contactError;
    }

    public void setContactError(ContactError contactError) {
        this.contactError = contactError;
    }

}