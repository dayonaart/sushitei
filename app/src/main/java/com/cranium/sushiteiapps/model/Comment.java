
package com.cranium.sushiteiapps.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment implements Serializable
{

    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("visit_date")
    @Expose
    private String visitDate;
    @SerializedName("outlet_city_id")
    @Expose
    private String outletCityId;
    @SerializedName("outlet_id")
    @Expose
    private Integer outletId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("outlet_city")
    @Expose
    private Object outletCity;
    @SerializedName("outlet")
    @Expose
    private Outlet outlet;
    @SerializedName("comment_images")
    @Expose
    private List<CommentImage> commentImages = null;
    private final static long serialVersionUID = -6779127190411510401L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Comment() {
    }

    /**
     * 
     * @param id
     * @param updatedAt
     * @param lastName
     * @param outlet
     * @param commentImages
     * @param createdAt
     * @param outletId
     * @param outletCityId
     * @param visitDate
     * @param outletCity
     * @param comment
     * @param firstName
     * @param user
     */
    public Comment(String firstName, String lastName, String visitDate, String outletCityId, Integer outletId, String comment, String updatedAt, String createdAt, Integer id, User user, Object outletCity, Outlet outlet, List<CommentImage> commentImages) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.visitDate = visitDate;
        this.outletCityId = outletCityId;
        this.outletId = outletId;
        this.comment = comment;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.id = id;
        this.user = user;
        this.outletCity = outletCity;
        this.outlet = outlet;
        this.commentImages = commentImages;
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

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getOutletCityId() {
        return outletCityId;
    }

    public void setOutletCityId(String outletCityId) {
        this.outletCityId = outletCityId;
    }

    public Integer getOutletId() {
        return outletId;
    }

    public void setOutletId(Integer outletId) {
        this.outletId = outletId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Object getOutletCity() {
        return outletCity;
    }

    public void setOutletCity(Object outletCity) {
        this.outletCity = outletCity;
    }

    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

    public List<CommentImage> getCommentImages() {
        return commentImages;
    }

    public void setCommentImages(List<CommentImage> commentImages) {
        this.commentImages = commentImages;
    }

}
