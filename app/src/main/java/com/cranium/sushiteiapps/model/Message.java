
package com.cranium.sushiteiapps.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("code_voucher")
    @Expose
    private String codeVoucher;
    @SerializedName("latitude")
    @Expose
    private Object latitude;
    @SerializedName("longitude")
    @Expose
    private Object longitude;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("push_to")
    @Expose
    private String pushTo;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;
    @SerializedName("read_at")
    @Expose
    private String readAt;
    @SerializedName("is_voucher")
    @Expose
    private Integer isVoucher;
    @SerializedName("is_use")
    @Expose
    private Integer isUse;
    @SerializedName("voucher_code")
    @Expose
    private String voucherCode;
    @SerializedName("expired")
    @Expose
    private Integer expired;

    private final static long serialVersionUID = -431298571831562251L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Message() {
    }

    /**
     *
     * @param updatedAt
     * @param id
     * @param startDate
     * @param createdAt
     * @param description
     * @param name
     * @param image
     * @param readAt
     * @param endDate
     * @param longitude
     * @param latitude
     */
    public Message(Integer id, String name, String description, String image, Object latitude, Object longitude, String startDate, String endDate, String createdAt, Object updatedAt, String readAt) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.readAt = readAt;
    }

    public Message(Integer id, String name, String description, String image, String codeVoucher,  String startDate, String endDate, String pushTo, String createdAt, String readAt, Integer isVoucher, Integer isUse) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.codeVoucher = codeVoucher;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pushTo = pushTo;
        this.createdAt = createdAt;
        this.readAt = readAt;
        this.isVoucher = isVoucher;
        this.isUse = isUse;
    }

    public Message(Integer id, String name, String description, String image, String codeVoucher, Integer isUse) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.codeVoucher = codeVoucher;
        this.isUse = isUse;
    }

    public Message(Integer id, String name, String description, String image, String codeVoucher, Object latitude, Object longitude, String startDate, String endDate, String pushTo, String createdAt, Object updatedAt, String readAt, Integer isVoucher, Integer isUse) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.codeVoucher = codeVoucher;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pushTo = pushTo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.readAt = readAt;
        this.isVoucher = isVoucher;
        this.isUse = isUse;
    }

    public Message(Integer id, String name, String description, String image, String codeVoucher, Object latitude, Object longitude, String startDate, String endDate, String pushTo, String createdAt, Object updatedAt, String readAt, Integer isVoucher, Integer isUse, String voucherCode) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.codeVoucher = codeVoucher;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pushTo = pushTo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.readAt = readAt;
        this.isVoucher = isVoucher;
        this.isUse = isUse;
        this.voucherCode = voucherCode;
    }

    public Message(Integer id, String name, String description, String image, Object latitude, Object longitude, String startDate, String endDate, String createdAt, Object updatedAt, String readAt, Integer isVoucher) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.readAt = readAt;
        this.isVoucher = isVoucher;
    }

    public Message(Integer id, String name, String description, String image, Object latitude, Object longitude, String startDate, String endDate, String createdAt, Object updatedAt, Integer isVoucher) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isVoucher = isVoucher;
    }

    public Message(Integer id, String name, String description, String image, String startDate, String createdAt, Integer isVoucher) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.startDate = startDate;
        this.createdAt = createdAt;
        this.isVoucher = isVoucher;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Object getLatitude() {
        return latitude;
    }

    public void setLatitude(Object latitude) {
        this.latitude = latitude;
    }

    public Object getLongitude() {
        return longitude;
    }

    public void setLongitude(Object longitude) {
        this.longitude = longitude;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getReadAt() {
        return readAt;
    }

    public void setReadAt(String readAt) {
        this.readAt = readAt;
    }

    private String getCutDescription(String description, Integer cut) {
        if (description.length() > cut) {
            return description.substring(0, cut) + "...";
        }

        return description;
    }

    public String getShortDescription() {
        return this.getCutDescription(this.getDescription(), 100);
    }

    public String getShortName() {
        return this.getCutDescription(this.getName(), 24);
    }

    public String getShortStartDate() {
        String shortDate;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd H:m:s", new Locale("id", "ID")).parse(this.getStartDate());

            SimpleDateFormat outGoing = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));
            shortDate = outGoing.format(date);
        } catch (ParseException e) {
            shortDate = this.getStartDate();
            e.printStackTrace();
        }

        return shortDate;
    }

    public String getFormattedStartDate() {
        String formattedDate;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd H:m:s", new Locale("en", "EN")).parse(this.getStartDate());

            SimpleDateFormat outGoing = new SimpleDateFormat("EEEE, dd MMMM yyyy, hh:mm a", new Locale("en", "EN"));
            formattedDate = outGoing.format(date);
        } catch (ParseException e) {
            formattedDate = this.getStartDate();
            e.printStackTrace();
        }

        return formattedDate;
    }

    public Integer getIsVoucher() {
        return isVoucher;
    }

    public void setIsVoucher(Integer isVoucher) {
        this.isVoucher = isVoucher;
    }

    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }

    public String getCodeVoucher() {
        return codeVoucher;
    }

    public void setCodeVoucher(String codeVoucher) {
        this.codeVoucher = codeVoucher;
    }

    public String getPushTo() {
        return pushTo;
    }

    public void setPushTo(String pushTo) {
        this.pushTo = pushTo;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public Integer getExpired() {
        return expired;
    }

    public void setExpired(Integer expired) {
        this.expired = expired;
    }
}