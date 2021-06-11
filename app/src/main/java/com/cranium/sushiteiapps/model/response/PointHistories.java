
package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;

import com.cranium.sushiteiapps.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PointHistories implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private User user;
    private final static long serialVersionUID = 5901570457564185457L;

    /**
     * No args constructor for use in serialization
     *
     */
    public PointHistories() {
    }

    /**
     *
     * @param status
     * @param user
     */
    public PointHistories(Integer status, User user) {
        super();
        this.status = status;
        this.user = user;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}