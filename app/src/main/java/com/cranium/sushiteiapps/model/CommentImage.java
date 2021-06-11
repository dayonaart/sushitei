
package com.cranium.sushiteiapps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentImage implements Serializable
{

    @SerializedName("comment_id")
    @Expose
    private Integer commentId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private Integer id;
    private final static long serialVersionUID = -3182847182971758053L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public CommentImage() {
    }

    /**
     * 
     * @param id
     * @param updatedAt
     * @param order
     * @param createdAt
     * @param image
     * @param commentId
     */
    public CommentImage(Integer commentId, String image, Integer order, String updatedAt, String createdAt, Integer id) {
        super();
        this.commentId = commentId;
        this.image = image;
        this.order = order;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.id = id;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
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

}
