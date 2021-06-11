
package com.cranium.sushiteiapps.model;

import android.text.TextUtils;

import android.app.ProgressDialog;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Serializable
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
    @SerializedName("firebase_token")
    @Expose
    private String firebaseToken;
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
    private String cityId;
    @SerializedName("point")
    @Expose
    private String point;
    @SerializedName("point_expiring")
    @Expose
    private String pointExpiring;
    @SerializedName("point_expired_at")
    @Expose
    private String pointExpiredAt;
    @SerializedName("date_of_expiry")
    @Expose
    private String dateOfExpiry;
    @SerializedName("latest_purchase_at")
    @Expose
    private String latestPurchaseAt;
    @SerializedName("status")
    @Expose
    private String status;
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
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("qrcode")
    @Expose
    private String qrcode;
    @SerializedName("point_histories")
    @Expose
    private List<PointHistory> pointHistories = null;
    @SerializedName("registered_device_id")
    @Expose
    private String deviceId;
    @SerializedName("login_status")
    @Expose
    private Integer loginStatus;

    private final static long serialVersionUID = -3558261379391153089L;

    /**
     * No args constructor for use in serialization
     *
     */
    public User() {
    }

    /**
     *
     * @param lastName
     * @param phone
     * @param lastLoginAt
     * @param registeredAt
     * @param status
     * @param cityId
     * @param image
     * @param updatedAt
     * @param latestPurchaseAt
     * @param point
     * @param firebaseToken
     * @param pointExpiredAt
     * @param email
     * @param createdAt
     * @param qrcode
     * @param dob
     * @param firstName
     * @param memberCode
     */
    public User(String memberCode, String firstName, String lastName, String email, String firebaseToken, String image, String dob, String phone, String cityId, String point, String pointExpiredAt, String latestPurchaseAt, String status, String registeredAt, String lastLoginAt, String createdAt, String updatedAt, String qrcode) {
        super();
        this.memberCode = memberCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.firebaseToken = firebaseToken;
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
        this.qrcode = qrcode;
    }

    /**
     *
     * @param lastName
     * @param phone
     * @param lastLoginAt
     * @param registeredAt
     * @param status
     * @param cityId
     * @param image
     * @param updatedAt
     * @param latestPurchaseAt
     * @param point
     * @param firebaseToken
     * @param pointExpiredAt
     * @param token
     * @param email
     * @param createdAt
     * @param qrcode
     * @param dob
     * @param firstName
     * @param memberCode
     */
    public User(String memberCode, String firstName, String lastName, String email, String firebaseToken, String image, String dob, String phone, String cityId, String point, String pointExpiredAt, String latestPurchaseAt, String status, String registeredAt, String lastLoginAt, String createdAt, String updatedAt, String token, String qrcode) {
        super();
        this.memberCode = memberCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.firebaseToken = firebaseToken;
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
        this.token = token;
        this.qrcode = qrcode;
    }

    /**
     *
     * @param lastName
     * @param phone
     * @param lastLoginAt
     * @param registeredAt
     * @param cityId
     * @param status
     * @param image
     * @param firebaseToken
     * @param updatedAt
     * @param latestPurchaseAt
     * @param point
     * @param pointExpiredAt
     * @param email
     * @param createdAt
     * @param qrcode
     * @param dob
     * @param firstName
     * @param pointHistories
     * @param memberCode
     */
    public User(String memberCode, String firstName, String lastName, String email, String firebaseToken, String image, String dob, String phone, String cityId, String point, String pointExpiredAt, String latestPurchaseAt, String status, String registeredAt, String lastLoginAt, String createdAt, String updatedAt, List<PointHistory> pointHistories, String qrcode) {
        super();
        this.memberCode = memberCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.firebaseToken = firebaseToken;
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
        this.pointHistories = pointHistories;
        this.qrcode = qrcode;
    }

    public User(String memberCode, String firstName, String lastName, String email, String firebaseToken, String image, String dob, String phone, String cityId, String point, String pointExpiredAt, String latestPurchaseAt, String status, String registeredAt, String lastLoginAt, String createdAt, String updatedAt, String token, String qrcode, List<PointHistory> pointHistories, String deviceId) {
        this.memberCode = memberCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.firebaseToken = firebaseToken;
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
        this.token = token;
        this.qrcode = qrcode;
        this.pointHistories = pointHistories;
        this.deviceId = deviceId;
    }

    public User(String memberCode, String firstName, String lastName, String email, String firebaseToken, String image, String dob, String phone, String cityId, String point, String pointExpiredAt, String latestPurchaseAt, String status, String registeredAt, String lastLoginAt, String createdAt, String updatedAt, String token, String qrcode, List<PointHistory> pointHistories, String deviceId, Integer loginStatus) {
        this.memberCode = memberCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.firebaseToken = firebaseToken;
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
        this.token = token;
        this.qrcode = qrcode;
        this.pointHistories = pointHistories;
        this.deviceId = deviceId;
        this.loginStatus = loginStatus;
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

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
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

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getPointExpiredAt() {
        return pointExpiredAt;
    }

    public String getFormattedPointExpiredAt() {
        String formattedDate;
        if (TextUtils.isEmpty(this.getPointExpiredAt())) {
            return "-";
        }
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "EN")).parse(this.getPointExpiredAt());

            SimpleDateFormat outGoing = new SimpleDateFormat("dd MMMM yyyy", new Locale("en", "EN"));
            formattedDate = outGoing.format(date);
        } catch (ParseException e) {
            formattedDate = this.getPointExpiredAt();
            e.printStackTrace();
        }

        if(this.getStatusDescription().equals("Temporary")) {
            Calendar date = Calendar.getInstance();
            date.setTime(new Date());
            Format f = new SimpleDateFormat("yyyy");
            date.add(Calendar.YEAR,1);

            String da = f.format(date.getTime());
            String dda = da + "-06-30";

            try {
                Date datee = new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "EN")).parse(dda);

                SimpleDateFormat outGoing = new SimpleDateFormat("dd MMMM yyyy", new Locale("en", "EN"));
                formattedDate = outGoing.format(datee);
            } catch (ParseException e) {
                formattedDate = this.getPointExpiredAt();
                e.printStackTrace();
            }

            return formattedDate;
        }

        return formattedDate;
    }

    public String getFormattedDateOfExpiry() {
        String formattedDate;
        if (TextUtils.isEmpty(this.getDateOfExpiry())) {
            return "-";
        }
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "EN")).parse(this.getDateOfExpiry());

            SimpleDateFormat outGoing = new SimpleDateFormat("dd MMMM yyyy", new Locale("en", "EN"));
            formattedDate = outGoing.format(date);
        } catch (ParseException e) {
            formattedDate = this.getDateOfExpiry();
            e.printStackTrace();
        }

        if(this.getStatusDescription().equals("Temporary")) {
            Calendar date = Calendar.getInstance();
            date.setTime(new Date());
            Format f = new SimpleDateFormat("yyyy");
            date.add(Calendar.YEAR,1);

            String da = f.format(date.getTime());
            String dda = da + "-06-30";

            try {
                Date datee = new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "EN")).parse(dda);

                SimpleDateFormat outGoing = new SimpleDateFormat("dd MMMM yyyy", new Locale("en", "EN"));
                formattedDate = outGoing.format(datee);
            } catch (ParseException e) {
                formattedDate = this.getDateOfExpiry();
                e.printStackTrace();
            }

            return formattedDate;
        }

        return formattedDate;
    }

    public String getFormattedPointExpiredAtPlus() {
        String formattedDate;
        if (TextUtils.isEmpty(this.getPointExpiredAt())) {
            return "-";
        }
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "EN")).parse(this.getPointExpiredAt());

            SimpleDateFormat outGoing = new SimpleDateFormat("dd MMMM yyyy", new Locale("en", "EN"));
            formattedDate = outGoing.format(date);
        } catch (ParseException e) {
            formattedDate = this.getPointExpiredAt();
            e.printStackTrace();
        }

        if(this.getStatusDescription().equals("Temporary")) {
            Calendar date = Calendar.getInstance();
            date.setTime(new Date());
            Format f = new SimpleDateFormat("yyyy");
            date.add(Calendar.YEAR,2);

            String da = f.format(date.getTime());
            String dda = da + "-06-30";

            try {
                Date datee = new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "EN")).parse(dda);

                SimpleDateFormat outGoing = new SimpleDateFormat("dd MMMM yyyy", new Locale("en", "EN"));
                formattedDate = outGoing.format(datee);
            } catch (ParseException e) {
                formattedDate = this.getPointExpiredAt();
                e.printStackTrace();
            }

            return formattedDate;
        }

        return formattedDate;
    }

    public String getFormattedPointExpiredAtE() {
        String formattedDate;
        if (TextUtils.isEmpty(this.getPointExpiredAt())) {
            return "-";
        }
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "EN")).parse(this.getPointExpiredAt());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date nowDate = new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "EN")).parse(dateFormat.format(new Date()));

            int diffInDays = (int) ((date.getTime() - nowDate.getTime()) / (1000 * 60 * 60 * 24));

            SimpleDateFormat outGoing = new SimpleDateFormat("dd MMMM yyyy", new Locale("en", "EN"));
            formattedDate = outGoing.format(date);
        } catch (ParseException e) {
            formattedDate = this.getPointExpiredAt();
            e.printStackTrace();
        }

        return formattedDate;
    }

    public static String getFormattedMemberCode(String mCode){
        String result = "";
        String part1 = mCode.substring(0,4);
        String part2 = mCode.substring(4,8);
        String part3 = mCode.substring(8,12);
        String part4 = mCode.substring(12,16);
        result = part1+" "+part2+" "+part3+" "+part4;
        return result;
    }

    public String getFormattedMemberCode(){
        String result = "";
        if (!getMemberCode().equals("")){
            String memberCode = getMemberCode();
            String part1 = memberCode.substring(0,4);
            String part2 = memberCode.substring(4,8);
            String part3 = memberCode.substring(8,12);
            String part4 = memberCode.substring(12,16);
            result = part1+" "+part2+" "+part3+" "+part4;
        }

        return result;
    }

    public Integer getDel() {
        Integer formattedDate;


        if (TextUtils.isEmpty(this.getPointExpiredAt())) {
            return 1;
        }
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "EN")).parse(this.getPointExpiredAt());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date nowDate = new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "EN")).parse(dateFormat.format(new Date()));

            int diffInDays = (int) ((date.getTime() - nowDate.getTime()) / (1000 * 60 * 60 * 24));

//            diffInDays = 3;

            formattedDate = diffInDays;
        } catch (ParseException e) {
            formattedDate = 0;
            e.printStackTrace();
        }

        if(this.getStatus().equals("1") || this.getStatus().equals(1)) {
            return formattedDate;
        }else{
            return formattedDate;
        }
    }

    public Integer getDelH14() {
        Integer formattedDate;


        if (TextUtils.isEmpty(this.getPointExpiredAt())) {
            return 1;
        }
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "EN")).parse(this.getPointExpiredAt());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date nowDate = new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "EN")).parse(dateFormat.format(new Date()));

            int diffInDays = (int) ((date.getTime() - nowDate.getTime()) / (1000 * 60 * 60 * 24));

//            diffInDays = 3;

            formattedDate = diffInDays;
        } catch (ParseException e) {
            formattedDate = 0;
            e.printStackTrace();
        }

        if(this.getStatus().equals("1") || this.getStatus().equals(1)) {
            return formattedDate;
        }else{
            return formattedDate;
        }
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getQrcode() {
        return qrcode;
    }

    public String getStatusDescription() {
        String status = "Unknown";
        if (!TextUtils.isEmpty(this.status)) {
            if (this.status.equals("5") || this.status.equals(5)) {
                status = "Permanent";
            }else if (this.status.equals("1") || this.status.equals(1)) {
                status = "Temporary";
            }else if (this.status.equals("2") || this.status.equals(2)) {
                status = "Permanent Non Card";
            }else if (this.status.equals("3") || this.status.equals(3)) {
                status = "Expired";
            }else if (this.status.equals("4") || this.status.equals(4)) {
                status = "Blocked";
            }
        }

        return status;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public List<PointHistory> getPointHistories() {
        return pointHistories;
    }

    public void setPointHistories(List<PointHistory> pointHistories) {
        this.pointHistories = pointHistories;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getPointExpiring() {
        return pointExpiring;
    }

    public void setPointExpiring(String pointExpiring) {
        this.pointExpiring = pointExpiring;
    }

    public String getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(String dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }
}