package com.cranium.sushiteiapps.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FirebaseRequest implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("firebase_token")
    @Expose
    private String firebaseToken;
    @SerializedName("device_number")
    @Expose
    private String deviceNumber;
    @SerializedName("device_status")
    @Expose
    private Integer deviceStatus;

    private final static long serialVersionUID = 4708500936501419243L;

    /**
     * No args constructor for use in serialization
     *
     */
    public FirebaseRequest() {
    }

    public FirebaseRequest(String firebaseToken, String deviceNumber) {
        super();
        this.firebaseToken = firebaseToken;
        this.deviceNumber = deviceNumber;
    }

    public FirebaseRequest(String firebaseToken, String deviceNumber, Integer deviceStatus) {
        this.firebaseToken = firebaseToken;
        this.deviceNumber = deviceNumber;
        this.deviceStatus = deviceStatus;
    }

    public FirebaseRequest(Integer id, String firebaseToken, String deviceNumber) {
        super();
        this.id = id;
        this.firebaseToken = firebaseToken;
        this.deviceNumber = deviceNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }
}