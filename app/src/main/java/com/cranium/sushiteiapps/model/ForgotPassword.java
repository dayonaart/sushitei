package com.cranium.sushiteiapps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPassword implements Serializable
{

    @SerializedName("member_code")
    @Expose
    private String memberCode;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("city_id")
    @Expose
    private Integer cityId;
    @SerializedName("point")
    @Expose
    private Integer point;
    @SerializedName("point_expired_at")
    @Expose
    private String pointExpiredAt;
    @SerializedName("latest_purchase_at")
    @Expose
    private String latestPurchaseAt;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("registered_at")
    @Expose
    private String registeredAt;
    @SerializedName("last_login_at")
    @Expose
    private String lastLoginAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("new_password")
    @Expose
    private String newPassword;
    private final static long serialVersionUID = 4471319974116848238L;

    /**
     * No args constructor for use in serialization
     *
     */
    public ForgotPassword() {
    }

    /**
     *
     * @param lastName
     * @param phone
     * @param lastLoginAt
     * @param registeredAt
     * @param cityId
     * @param status
     * @param newPassword
     * @param image
     * @param updatedAt
     * @param latestPurchaseAt
     * @param point
     * @param pointExpiredAt
     * @param email
     * @param createdAt
     * @param dob
     * @param firstName
     * @param memberCode
     */
    public ForgotPassword(String memberCode, String firstName, String lastName, String email, String image, String dob, String phone, Integer cityId, Integer point, String pointExpiredAt, String latestPurchaseAt, Integer status, String registeredAt, String lastLoginAt, String createdAt, String updatedAt, String newPassword) {
        super();
        this.memberCode = memberCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.image = image;
        this.dob = dob;
        this.phone = phone;
        this.cityId = cityId;
        this.point = point;
        this.pointExpiredAt = pointExpiredAt;
        this.latestPurchaseAt = latestPurchaseAt;
        this.status = status;
        this.registeredAt = registeredAt;
        this.lastLoginAt = lastLoginAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.newPassword = newPassword;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getPointExpiredAt() {
        return pointExpiredAt;
    }

    public void setPointExpiredAt(String pointExpiredAt) {
        this.pointExpiredAt = pointExpiredAt;
    }

    public String getLatestPurchaseAt() {
        return latestPurchaseAt;
    }

    public void setLatestPurchaseAt(String latestPurchaseAt) {
        this.latestPurchaseAt = latestPurchaseAt;
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

    public String getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(String lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}