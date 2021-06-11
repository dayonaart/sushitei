package com.cranium.sushiteiapps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Register implements Serializable
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
    @SerializedName("member_code")
    @Expose
    private Long memberCode;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("registered_at")
    @Expose
    private String registeredAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    private final static long serialVersionUID = -1667647629298655768L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Register() {
    }

    /**
     *
     * @param updatedAt
     * @param lastName
     * @param phone
     * @param registeredAt
     * @param status
     * @param cityId
     * @param email
     * @param createdAt
     * @param dob
     * @param firstName
     * @param memberCode
     */
    public Register(String firstName, String lastName, String email, String dob, String phone, Integer cityId, Long memberCode, Integer status, String registeredAt, String updatedAt, String createdAt) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = dob;
        this.phone = phone;
        this.cityId = cityId;
        this.memberCode = memberCode;
        this.status = status;
        this.registeredAt = registeredAt;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
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

    public Long getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(Long memberCode) {
        this.memberCode = memberCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(String registeredAt) {
        this.registeredAt = registeredAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}