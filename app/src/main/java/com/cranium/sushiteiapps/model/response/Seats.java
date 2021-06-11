
package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;
import java.util.List;

import com.cranium.sushiteiapps.model.Seat;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Seats implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<Seat> seats = null;
    private final static long serialVersionUID = 8720568037275266614L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Seats() {
    }

    /**
     *
     * @param status
     * @param seats
     */
    public Seats(Integer status, List<Seat> seats) {
        super();
        this.status = status;
        this.seats = seats;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

}