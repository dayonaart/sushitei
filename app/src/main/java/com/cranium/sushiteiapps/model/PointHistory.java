
package com.cranium.sushiteiapps.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PointHistory implements Serializable
{



    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("outlet_city_id")
    @Expose
    private String outletCityId;
    @SerializedName("outlet_id")
    @Expose
    private String outletId;
    @SerializedName("is_add")
    @Expose
    private String isAdd;
    @SerializedName("operation")
    @Expose
    private String operation;
    @SerializedName("point")
    @Expose
    private String point;
    @SerializedName("trans_type")
    @Expose
    private String transType;
    @SerializedName("purchase_total")
    @Expose
    private String purchaseTotal;
    @SerializedName("purchase_date")
    @Expose
    private String purchaseDate;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("transaction_at")
    @Expose
    private String transactionAt;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;
    @SerializedName("balance_point")
    @Expose
    private Integer balancePoint;
    @SerializedName("point_bonus")
    @Expose
    private Integer pointBonus;
    @SerializedName("outlet_city")
    @Expose
    private OutletCity outletCity;
    @SerializedName("outlet")
    @Expose
    private Outlet outlet;
    private final static long serialVersionUID = -7803084610028459024L;

    /**
     * No args constructor for use in serialization
     *
     */
    public PointHistory() {
    }

    /**
     *
     * @param updatedAt
     * @param id
     * @param point
     * @param operation
     * @param outlet
     * @param createdAt
     * @param purchaseDate
     * @param isAdd
     * @param outletId
     * @param outletCityId
     * @param balancePoint
     * @param purchaseTotal
     * @param outletCity
     */
    public PointHistory(Integer id, String outletCityId, String outletId, String isAdd, String operation, String point, String purchaseTotal, String purchaseDate, String createdAt, Object updatedAt, Integer balancePoint, OutletCity outletCity, Outlet outlet) {
        super();
        this.id = id;
        this.outletCityId = outletCityId;
        this.outletId = outletId;
        this.isAdd = isAdd;
        this.operation = operation;
        this.point = point;
        this.purchaseTotal = purchaseTotal;
        this.purchaseDate = purchaseDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.balancePoint = balancePoint;
        this.outletCity = outletCity;
        this.outlet = outlet;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getIsAdd() {
        return isAdd;
    }

    public void setIsAdd(String isAdd) {
        this.isAdd = isAdd;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getPurchaseTotal() {
        return purchaseTotal;
    }

    public void setPurchaseTotal(String purchaseTotal) {
        this.purchaseTotal = purchaseTotal;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
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

    public OutletCity getOutletCity() {
        return outletCity;
    }

    public void setOutletCity(OutletCity outletCity) {
        this.outletCity = outletCity;
    }

    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

    public String getFormattedPurchaseTotal() {
        Locale locale = new Locale("id", "ID");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

        return currencyFormatter.format(Double.parseDouble(this.getPurchaseTotal()));
    }

    public String getFormattedPurchaseDate() {
        String formattedDate;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", new Locale("en", "EN")).parse(this.getTransactionAt());
            SimpleDateFormat outGoing = new SimpleDateFormat("EEEE, dd MMMM yyyy, H:mm", new Locale("en", "EN"));
            formattedDate = outGoing.format(date) + " WIB";
//            formattedDate = this.getPurchaseDate() + " WIB";
        } catch (ParseException e) {
            formattedDate = this.getTransactionAt();
            e.printStackTrace();
        }

        return formattedDate;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransactionAt() {
        return transactionAt;
    }

    public void setTransactionAt(String transactionAt) {
        this.transactionAt = transactionAt;
    }

    public Integer getBalancePoint() {
        return balancePoint;
    }

    public void setBalancePoint(Integer balancePoint) {
        this.balancePoint = balancePoint;
    }
    public Integer getPointBonus() {
        return pointBonus;
    }

    public void setPointBonus(Integer pointBonus) {
        this.pointBonus = pointBonus;
    }

}