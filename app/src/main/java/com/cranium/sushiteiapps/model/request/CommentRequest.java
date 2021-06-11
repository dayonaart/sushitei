
package com.cranium.sushiteiapps.model.request;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentRequest implements Serializable
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
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("visit_date")
    @Expose
    private String visitDate;
    @SerializedName("outlet_id")
    @Expose
    private Integer outletId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("images")
    @Expose
    private List<String> images = null;
    private final static long serialVersionUID = -1870627005314970513L;

    /**
     * No args constructor for use in serialization
     *
     */
    public CommentRequest() {
    }

    /**
     *
     * @param lastName
     * @param outletId
     * @param images
     * @param visitDate
     * @param comment
     * @param firstName
     */
    public CommentRequest(String firstName, String lastName, String phone, String visitDate, Integer outletId, String comment, List<String> images) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.visitDate = visitDate;
        this.outletId = outletId;
        this.comment = comment;
        this.images = images;
    }

    public CommentRequest(String firstName, String lastName, String phone, String email, String visitDate, Integer outletId, String comment, List<String> images) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.visitDate = visitDate;
        this.outletId = outletId;
        this.comment = comment;
        this.images = images;
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
        return lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

}