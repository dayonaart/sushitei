
package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;
import java.util.List;

import com.cranium.sushiteiapps.model.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Messages implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Message> messages;
    @SerializedName("unread_messages")
    @Expose
    private Integer unreadMessages;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("unread")
    @Expose
    private Integer unread;

    private final static long serialVersionUID = 3223418822426478738L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Messages() {
    }

    public Messages(Integer status, String message, List<Message> messages, Integer unreadMessages) {
        this.status = status;
        this.message = message;
        this.messages = messages;
        this.unreadMessages = unreadMessages;
    }

    public Integer getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(Integer unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
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

    public Integer getUnread() {
        return unread;
    }

    public void setUnread(Integer unread) {
        this.unread = unread;
    }
}