package com.cranium.sushiteiapps.model.request;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterRequest implements Serializable
{

    @SerializedName("first_name")
    @Expose
    private String firstName;

    @SerializedName("last_name")
    @Expose
    private String lastName;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("city_id")
    @Expose
    private Integer cityId;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("confirm_password")
    @Expose
    private String confirmPassword;

    @SerializedName("device_number")
    @Expose
    private String deviceNumber;

    @SerializedName("firebase_token")
    @Expose
    private String firebaseToken;

    private final static long serialVersionUID = -4658059268352611565L;

    /**
     * No args constructor for use in serialization
     *
     */
    public RegisterRequest() {
    }

    /**
     *
     * @param lastName
     * @param confirmPassword
     * @param phone
     * @param cityId
     * @param email
     * @param dob
     * @param firstName
     * @param deviceNumber
     * @param password
     */
    public RegisterRequest(String firstName, String lastName, String email, String dob, String phone, Integer cityId, String password, String confirmPassword, String deviceNumber) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = dob;
        this.phone = phone;
        this.cityId = cityId;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.deviceNumber = deviceNumber;
    }

    public RegisterRequest(String firstName, String lastName, String email, String dob, String phone, Integer cityId, String password, String confirmPassword, String deviceNumber, String firebaseToken) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = dob;
        this.phone = phone;
        this.cityId = cityId;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.deviceNumber = deviceNumber;
        this.firebaseToken = firebaseToken;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

}