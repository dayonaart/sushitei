
package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditProfileError implements Serializable
{

    @SerializedName("first_name")
    @Expose
    private List<String> firstName = null;
    @SerializedName("dob")
    @Expose
    private List<String> dob = null;
    @SerializedName("phone")
    @Expose
    private List<String> phone = null;
    @SerializedName("email")
    @Expose
    private List<String> email = null;
    @SerializedName("city_id")
    @Expose
    private List<String> cityId = null;
    @SerializedName("old_password")
    @Expose
    private String oldPassword;
    @SerializedName("new_password")
    @Expose
    private String newPassword;
    private final static long serialVersionUID = -8193442337591524024L;

    /**
     * No args constructor for use in serialization
     *
     */
    public EditProfileError() {
    }

    /**
     *
     * @param phone
     * @param cityId
     * @param email
     * @param newPassword
     * @param dob
     * @param oldPassword
     * @param firstName
     */
    public EditProfileError(List<String> firstName, List<String> dob, List<String> phone, List<String> email, List<String> cityId, String oldPassword, String newPassword) {
        super();
        this.firstName = firstName;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
        this.cityId = cityId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public EditProfileError(String oldPassword) {
        super();
        this.oldPassword = oldPassword;
    }
    public EditProfileError(List<String> email) {
        super();
        this.email = email;
    }

    public List<String> getFirstName() {
        return firstName;
    }

    public void setFirstName(List<String> firstName) {
        this.firstName = firstName;
    }

    public List<String> getDob() {
        return dob;
    }

    public void setDob(List<String> dob) {
        this.dob = dob;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public List<String> getCityId() {
        return cityId;
    }

    public void setCityId(List<String> cityId) {
        this.cityId = cityId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}