
package com.cranium.sushiteiapps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastQueue implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("queue_number")
    @Expose
    private String queueNumber;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("reservation_date")
    @Expose
    private String reservationDate;
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
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("leftover_queue")
    @Expose
    private Integer leftoverQueue;
    @SerializedName("status_desc")
    @Expose
    private String statusDesc;
    private final static long serialVersionUID = -883736926319743798L;

    /**
     * No args constructor for use in serialization
     *
     */
    public LastQueue() {
    }

    /**
     *
     * @param lastName
     * @param phone
     * @param queueNumber
     * @param status
     * @param reservationDate
     * @param id
     * @param pax
     * @param outletId
     * @param seatId
     * @param outletCityId
     * @param leftoverQueue
     * @param firstName
     * @param statusDesc
     */
    public LastQueue(Integer id, String queueNumber, String firstName, String lastName, String reservationDate, String phone, Integer outletCityId, Integer outletId, Integer pax, Integer seatId, Integer status, Integer leftoverQueue, String statusDesc) {
        super();
        this.id = id;
        this.queueNumber = queueNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.reservationDate = reservationDate;
        this.phone = phone;
        this.outletCityId = outletCityId;
        this.outletId = outletId;
        this.pax = pax;
        this.seatId = seatId;
        this.status = status;
        this.leftoverQueue = leftoverQueue;
        this.statusDesc = statusDesc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQueueNumber() {
        return (queueNumber != null) ? queueNumber : "-";
    }

    public void setQueueNumber(String queueNumber) {
        this.queueNumber = queueNumber;
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

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLeftoverQueue() {
        return leftoverQueue;
    }

    public void setLeftoverQueue(Integer leftoverQueue) {
        this.leftoverQueue = leftoverQueue;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

}