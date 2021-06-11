
package com.cranium.sushiteiapps.model.response;

import com.cranium.sushiteiapps.model.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Messages_bak implements Serializable
{

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("per_page")
    @Expose
    private Integer perPage;
    @SerializedName("current_page")
    @Expose
    private Integer currentPage;
    @SerializedName("last_page")
    @Expose
    private Integer lastPage;
    @SerializedName("next_page_url")
    @Expose
    private Object nextPageUrl;
    @SerializedName("prev_page_url")
    @Expose
    private Object prevPageUrl;
    @SerializedName("from")
    @Expose
    private Integer from;
    @SerializedName("to")
    @Expose
    private Integer to;
    @SerializedName("data")
    @Expose
    private List<Message> messages ;
    @SerializedName("unread_messages")
    @Expose
    private Integer unreadMessages;
    @SerializedName("udud")
    @Expose
    private Integer udud;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("login_status")
    @Expose
    private Integer loginStatus;

    private final static long serialVersionUID = 3223418822426478738L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Messages_bak() {
    }

    /**
     *
     * @param to
     * @param total
     * @param unreadMessages
     * @param lastPage
     * @param nextPageUrl
     * @param status
     * @param prevPageUrl
     * @param from
     * @param currentPage
     * @param perPage
     * @param messages
     */

    public Messages_bak(Integer total, Integer perPage, Integer currentPage, Integer lastPage, Object nextPageUrl, Object prevPageUrl, Integer from, Integer to, List<Message> messages, Integer unreadMessages, Integer status) {
        super();
        this.total = total;
        this.perPage = perPage;
        this.currentPage = currentPage;
        this.lastPage = lastPage;
        this.nextPageUrl = nextPageUrl;
        this.prevPageUrl = prevPageUrl;
        this.from = from;
        this.to = to;
        this.messages = messages;
        this.unreadMessages = unreadMessages;
        this.status = status;
    }

    public Messages_bak(Integer total, Integer perPage, Integer currentPage, Integer lastPage, Object nextPageUrl, Object prevPageUrl, Integer from, Integer to, List<Message> messages, Integer unreadMessages, Integer udud, Integer status) {
        super();
        this.total = total;
        this.perPage = perPage;
        this.currentPage = currentPage;
        this.lastPage = lastPage;
        this.nextPageUrl = nextPageUrl;
        this.prevPageUrl = prevPageUrl;
        this.from = from;
        this.to = to;
        this.messages = messages;
        this.unreadMessages = unreadMessages;
        this.udud = udud;
        this.status = status;
    }

    /**
     *
     * @param to
     * @param total
     * @param unreadMessages
     * @param lastPage
     * @param nextPageUrl
     * @param status
     * @param prevPageUrl
     * @param from
     * @param currentPage
     * @param perPage
     * @param messages
     * @param udud
     * @param loginStatus
     */
    public Messages_bak(Integer total, Integer perPage, Integer currentPage, Integer lastPage, Object nextPageUrl, Object prevPageUrl, Integer from, Integer to, List<Message> messages, Integer unreadMessages, Integer udud, Integer status, Integer loginStatus) {
        this.total = total;
        this.perPage = perPage;
        this.currentPage = currentPage;
        this.lastPage = lastPage;
        this.nextPageUrl = nextPageUrl;
        this.prevPageUrl = prevPageUrl;
        this.from = from;
        this.to = to;
        this.messages = messages;
        this.unreadMessages = unreadMessages;
        this.udud = udud;
        this.status = status;
        this.loginStatus = loginStatus;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }

    public Object getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(Object nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public Object getPrevPageUrl() {
        return prevPageUrl;
    }

    public void setPrevPageUrl(Object prevPageUrl) {
        this.prevPageUrl = prevPageUrl;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Integer getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(Integer unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public Integer getUdud() {
        return udud;
    }

    public void setUdud(Integer udud) {
        this.udud = udud;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
    }
}