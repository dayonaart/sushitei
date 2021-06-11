
package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;

import com.cranium.sushiteiapps.model.Comment;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Comment comment;
    private final static long serialVersionUID = -6433862896731721337L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public CommentResponse() {
    }

    /**
     * 
     * @param message
     * @param status
     * @param comment
     */
    public CommentResponse(Integer status, String message, Comment comment) {
        super();
        this.status = status;
        this.message = message;
        this.comment = comment;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

}
