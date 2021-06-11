
package com.cranium.sushiteiapps.model.request;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRequest implements Serializable
{

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("firebase_token")
    @Expose
    private String firebaseToken;
    @SerializedName("device_number")
    @Expose
    private String deviceNumber;


    private final static long serialVersionUID = 7857999938632522340L;

    /**
     * No args constructor for use in serialization
     *
     */
    public LoginRequest() {
    }

    /**
     *
     * @param firebaseToken
     * @param email
     * @param password
     */
    public LoginRequest(String email, String password, String firebaseToken) {
        super();
        this.email = email;
        this.password = password;
        this.firebaseToken = firebaseToken;
    }

    public LoginRequest(String email, String password, String firebaseToken, String deviceNumber) {
        this.email = email;
        this.password = password;
        this.firebaseToken = firebaseToken;
        this.deviceNumber = deviceNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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