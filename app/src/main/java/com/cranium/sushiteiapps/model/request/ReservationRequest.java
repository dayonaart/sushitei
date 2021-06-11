
package com.cranium.sushiteiapps.model.request;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReservationRequest implements Serializable
{

    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("outlet_city_id")
    @Expose
    private Integer outletCityId;
    @SerializedName("outlet_id")
    @Expose
    private Integer outletId;
    @SerializedName("pax")
    @Expose
    private Integer pax;
    @SerializedName("seat_id")
    @Expose
    private Integer seatId;
    @SerializedName("firebase_token")
    @Expose
    private String firebaseToken;
    private final static long serialVersionUID = 5465516058319688362L;

    /**
     * No args constructor for use in serialization
     *
     */
    public ReservationRequest() {
    }

    /**
     *
     * @param lastName
     * @param phone
     * @param pax
     * @param outletId
     * @param seatId
     * @param outletCityId
     * @param firstName
     */
    public ReservationRequest(String firstName, String lastName, String phone, Integer outletCityId, Integer outletId, Integer pax, Integer seatId) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.outletCityId = outletCityId;
        this.outletId = outletId;
        this.pax = pax;
        this.seatId = seatId;
    }

    public ReservationRequest(String firstName, String lastName, String phone, Integer outletCityId, Integer outletId, Integer pax, Integer seatId, String firebaseToken) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.outletCityId = outletCityId;
        this.outletId = outletId;
        this.pax = pax;
        this.seatId = seatId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getOutletCityId() {
        return outletCityId;
    }

    public void setOutletCityId(Integer outletCityId) {
        this.outletCityId = outletCityId;
    }

    public Integer getOutletId() {
        return outletId;
    }

    public void setOutletId(Integer outletId) {
        this.outletId = outletId;
    }

    public Integer getPax() {
        return pax;
    }

    public void setPax(Integer pax) {
        this.pax = pax;
    }

    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }

}