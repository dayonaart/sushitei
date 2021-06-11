
package com.cranium.sushiteiapps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reservation implements Serializable
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
    private String outletCityId;
    @SerializedName("outlet_id")
    @Expose
    private String outletId;
    @SerializedName("pax")
    @Expose
    private String pax;
    @SerializedName("seat_id")
    @Expose
    private String seatId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("last_queue")
    @Expose
    private LastQueue lastQueue;
    @SerializedName("leftover_queue")
    @Expose
    private Integer leftoverQueue;
    @SerializedName("status_desc")
    @Expose
    private String statusDesc;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("outlet")
    @Expose
    private Outlet outlet;
    private final static long serialVersionUID = 4205109548743834384L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Reservation() {
    }

    /**
     *
     * @param lastName
     * @param phone
     * @param outlet
     * @param queueNumber
     * @param status
     * @param reservationDate
     * @param id
     * @param pax
     * @param outletId
     * @param lastQueue
     * @param seatId
     * @param outletCityId
     * @param leftoverQueue
     * @param firstName
     * @param user
     * @param statusDesc
     */
    public Reservation(Integer id, String queueNumber, String firstName, String lastName, String reservationDate, String phone, String outletCityId, String outletId, String pax, String seatId, String status, LastQueue lastQueue, Integer leftoverQueue, String statusDesc, User user, Outlet outlet) {
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
        this.lastQueue = lastQueue;
        this.leftoverQueue = leftoverQueue;
        this.statusDesc = statusDesc;
        this.user = user;
        this.outlet = outlet;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQueueNumber() {
        return queueNumber;
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

    public String getOutletCityId() {
        return outletCityId;
    }

    public void setOutletCityId(String outletCityId) {
        this.outletCityId = outletCityId;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getPax() {
        return pax;
    }

    public void setPax(String pax) {
        this.pax = pax;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LastQueue getLastQueue() {
        return lastQueue;
    }

    public void setLastQueue(LastQueue lastQueue) {
        this.lastQueue = lastQueue;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

}