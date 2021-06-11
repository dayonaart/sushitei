
package com.cranium.sushiteiapps.model.response;

import com.cranium.sushiteiapps.model.VoucherDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VoucherUpdated implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("is_used")
    @Expose
    private Integer is_used;
    @SerializedName("messsage")
    private String messsage;
    @SerializedName("data")
    @Expose
    private VoucherDetail data;

    private final static long serialVersionUID = 3862617593540675227L;

    /**
     * No args constructor for use in serialization
     *
     */
    public VoucherUpdated() {
    }

    /**
     *
     * @param messsage
     * @param status
     */
    public VoucherUpdated(Integer status, String messsage) {
        super();
        this.status = status;
        this.messsage = messsage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMesssage() {
        return messsage;
    }

    public void setMesssage(String messsage) {
        this.messsage = messsage;
    }

    public Integer getIs_used() {
        return is_used;
    }

    public void setIs_used(Integer is_used) {
        this.is_used = is_used;
    }

    public VoucherDetail getData() {
        return data;
    }

    public void setData(VoucherDetail data) {
        this.data = data;
    }
}