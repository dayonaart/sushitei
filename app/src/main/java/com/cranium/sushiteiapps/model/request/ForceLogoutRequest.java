
package com.cranium.sushiteiapps.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ForceLogoutRequest implements Serializable
{
    @SerializedName("device_number")
    @Expose
    private String deviceNumber;
    @SerializedName("firebase_token")
    @Expose
    private String firebaseToken;


    private final static long serialVersionUID = 7857999938632522340L;

    /**
     * No args constructor for use in serialization
     *
     */
    public ForceLogoutRequest() {
    }

    /**
     *
     * @param firebaseToken
     */

    public ForceLogoutRequest(String deviceNumber, String firebaseToken) {
        this.firebaseToken = firebaseToken;
        this.deviceNumber = deviceNumber;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}