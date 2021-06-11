
package com.cranium.sushiteiapps.model.response;

import com.cranium.sushiteiapps.model.Comment;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FirebaseResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    private final static long serialVersionUID = -6433862892731721337L;

    /**
     * No args constructor for use in serialization
     *
     */
    public FirebaseResponse() {
    }

    /**
     *
     * @param message
     * @param status
     * @param comment
     */
    public FirebaseResponse(Integer status, String message, Comment comment) {
        super();
        this.status = status;
        this.message = message;
    }

    public FirebaseResponse(Integer status, String message) {
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
