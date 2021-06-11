
package com.cranium.sushiteiapps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VoucherCode implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("device_number")
    @Expose
    private String deviceNumber;
    @SerializedName("voucher_code")
    @Expose
    private String voucherCode;

    private final static long serialVersionUID = -1345989413237974482L;

    /**
     * No args constructor for use in serialization
     *
     */
    public VoucherCode() {
    }

    public VoucherCode(String id, String deviceNumber, String voucherCode) {
        this.id = id;
        this.deviceNumber = deviceNumber;
        this.voucherCode = voucherCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }


    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }
}