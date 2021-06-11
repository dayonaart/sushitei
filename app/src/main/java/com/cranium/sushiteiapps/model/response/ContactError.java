package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactError implements Serializable
{

    @SerializedName("first_name")
    @Expose
    private List<String> firstName = null;
    @SerializedName("email")
    @Expose
    private List<String> email = null;
    @SerializedName("phone")
    @Expose
    private List<String> phone = null;
    @SerializedName("about")
    @Expose
    private List<String> about = null;
    @SerializedName("message")
    @Expose
    private List<String> message = null;
    private final static long serialVersionUID = -6022679338938034767L;

    /**
     * No args constructor for use in serialization
     *
     */
    public ContactError() {
    }

    /**
     *
     * @param message
     * @param phone
     * @param email
     * @param about
     * @param firstName
     */
    public ContactError(List<String> firstName, List<String> email, List<String> phone, List<String> about, List<String> message) {
        super();
        this.firstName = firstName;
        this.email = email;
        this.phone = phone;
        this.about = about;
        this.message = message;
    }

    public List<String> getFirstName() {
        return firstName;
    }

    public Boolean hasFirstName() {
        if (firstName != null) {
            return true;
        }
        return false;
    }

    public void setFirstName(List<String> firstName) {
        this.firstName = firstName;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public Boolean hasEmail() {
        if (email != null) {
            return true;
        }
        return false;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    public Boolean hasPhone() {
        if (phone != null) {
            return true;
        }
        return false;
    }

    public List<String> getAbout() {
        return about;
    }

    public void setAbout(List<String> about) {
        this.about = about;
    }

    public Boolean hasAbout() {
        if (about != null) {
            return true;
        }
        return false;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public Boolean hasMessage() {
        if (message != null) {
            return true;
        }
        return false;
    }

}