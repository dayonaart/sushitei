package com.cranium.sushiteiapps.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VoucherDetail implements Serializable
{
    @SerializedName("id")
    @Expose
    private Object id;
    @SerializedName("voucher_code")
    @Expose
    private String voucherCode;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("message_id")
    @Expose
    private Integer messageId;
    @SerializedName("device_id")
    @Expose
    private Object deviceId;
    @SerializedName("is_assigned")
    @Expose
    private Integer isAssigned;
    @SerializedName("created_at")
    @Expose
    private Object createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("firebase_token")
    @Expose
    private Object firebaseToken;
    @SerializedName("device_number")
    @Expose
    private Object deviceNumber;
    @SerializedName("device_status")
    @Expose
    private Object deviceStatus;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;

    private final static long serialVersionUID = 1062650820285060009L;

    public VoucherDetail(Object id, String voucherCode, Object email, Integer messageId, Object deviceId, Integer isAssigned, Object createdAt, String updatedAt, Object firebaseToken, Object deviceNumber, Object deviceStatus, Object deletedAt) {
        this.id = id;
        this.voucherCode = voucherCode;
        this.email = email;
        this.messageId = messageId;
        this.deviceId = deviceId;
        this.isAssigned = isAssigned;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.firebaseToken = firebaseToken;
        this.deviceNumber = deviceNumber;
        this.deviceStatus = deviceStatus;
        this.deletedAt = deletedAt;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Object getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Object deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getIsAssigned() {
        return isAssigned;
    }

    public void setIsAssigned(Integer isAssigned) {
        this.isAssigned = isAssigned;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(Object firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public Object getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(Object deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public Object getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(Object deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }


}