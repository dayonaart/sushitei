package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;

import com.cranium.sushiteiapps.model.Contact;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Contact contact;
    private final static long serialVersionUID = -5896968738161141318L;

    /**
     * No args constructor for use in serialization
     *
     */
    public ContactResponse() {
    }

    /**
     *
     * @param message
     * @param status
     * @param contact
     */
    public ContactResponse(Integer status, String message, Contact contact) {
        super();
        this.status = status;
        this.message = message;
        this.contact = contact;
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

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

}