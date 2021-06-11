
package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;

import com.cranium.sushiteiapps.model.Reservation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReservationData implements Serializable
{
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Reservation reservation;
    @SerializedName("stats")
    @Expose
    private String stats;
    private final static long serialVersionUID = 9056727372714544062L;

    /**
     * No args constructor for use in serialization
     *
     */
    public ReservationData() {

    }

    /**
     *
     * @param message
     * @param status
     * @param reservation
     */
    public ReservationData(Integer status, String message, Reservation reservation) {
        super();
        this.status = status;
        this.message = message;
        this.reservation = reservation;
    }

    public String getStats() {
        return stats;
    }

    public void setStats(String stats) {
        this.stats = stats;
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

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

}