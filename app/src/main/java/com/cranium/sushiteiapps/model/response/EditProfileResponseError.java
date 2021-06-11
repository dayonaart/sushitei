
package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditProfileResponseError implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("validators")
    @Expose
    private EditProfileError editProfileError;
    private final static long serialVersionUID = 2839454215892167186L;

    /**
     * No args constructor for use in serialization
     *
     */
    public EditProfileResponseError() {
    }

    /**
     *
     * @param message
     * @param status
     * @param editProfileError
     */
    public EditProfileResponseError(Integer status, String message, EditProfileError editProfileError) {
        super();
        this.status = status;
        this.message = message;
        this.editProfileError = editProfileError;
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

    public EditProfileError getEditProfileError() {
        return editProfileError;
    }

    public void setEditProfileError(EditProfileError editProfileError) {
        this.editProfileError = editProfileError;
    }

}

