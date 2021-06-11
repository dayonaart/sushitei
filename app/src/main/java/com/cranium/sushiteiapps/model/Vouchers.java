
package com.cranium.sushiteiapps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Vouchers implements Serializable
{

    @SerializedName("device_number")
    @Expose
    private String deviceNumber;
    @SerializedName("date")
    @Expose
    private String date;
    private final static long serialVersionUID = -1345989413237974482L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Vouchers() {
    }


    public Vouchers(String deviceNumber, String date) {
        this.deviceNumber = deviceNumber;
        this.date = date;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}