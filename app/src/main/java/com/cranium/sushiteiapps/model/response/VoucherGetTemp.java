
package com.cranium.sushiteiapps.model.response;

import com.cranium.sushiteiapps.model.VoucherTemp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VoucherGetTemp implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("messsage")
    @Expose
    private String messsage;
    @SerializedName("data")
    @Expose
    private VoucherTemp data;

    private final static long serialVersionUID = 3862617593540675227L;

    /**
     * No args constructor for use in serialization
     *
     */
    public VoucherGetTemp() {
    }

    /**
     *
     * @param messsage
     * @param status
     */
    public VoucherGetTemp(Integer status, String messsage) {
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

    public VoucherTemp getData() {
        return data;
    }

    public void setData(VoucherTemp data) {
        this.data = data;
    }
}