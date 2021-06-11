
package com.cranium.sushiteiapps.model.response;

import com.cranium.sushiteiapps.model.VoucherDetail;
import com.cranium.sushiteiapps.model.VoucherTemp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VoucherGet implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("messsage")
    @Expose
    private String messsage;
    @SerializedName("data")
    @Expose
    private VoucherDetail data;

    private final static long serialVersionUID = 3862617593540675227L;

    /**
     * No args constructor for use in serialization
     *
     */
    public VoucherGet() {
    }

    public VoucherGet(Integer status, String messsage, VoucherDetail data) {
        this.status = status;
        this.messsage = messsage;
        this.data = data;
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

    public VoucherDetail getData() {
        return data;
    }

    public void setData(VoucherDetail data) {
        this.data = data;
    }
}