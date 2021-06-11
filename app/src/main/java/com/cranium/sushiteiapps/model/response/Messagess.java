
package com.cranium.sushiteiapps.model.response;

import com.cranium.sushiteiapps.model.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Messagess implements Serializable
{
    @SerializedName("data")
    @Expose
    private List<Message> messages ;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private Integer message;

//    @SerializedName("is_voucher")
//    @Expose
//    private Integer isVoucher;


    private final static long serialVersionUID = 3223418822426478738L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Messagess() {
    }

    public Messagess(List<Message> messages, Integer status, Integer message) {
        this.messages = messages;
        this.status = status;
        this.message = message;
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

    public Integer getMessage() {
        return message;
    }

    public void setMessage(Integer message) {
        this.message = message;
    }
}