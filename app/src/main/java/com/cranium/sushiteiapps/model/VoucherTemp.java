package com.cranium.sushiteiapps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VoucherTemp implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("voucher_code")
    @Expose
    private String voucherCode;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("message_id")
    @Expose
    private String messageId;
    @SerializedName("is_assigned")
    @Expose
    private Integer isAssigned;

    private final static long serialVersionUID = 8526484945950372861L;

    /**
     * No args constructor for use in serialization
     *
     */
    public VoucherTemp() {
    }


    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIsAssigned() {
        return isAssigned;
    }

    public void setIsAssigned(Integer isAssigned) {
        this.isAssigned = isAssigned;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


}