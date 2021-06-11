
package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;
import java.util.List;

import com.cranium.sushiteiapps.model.Article;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Articles implements Serializable
{

    @SerializedName("per_page")
    @Expose
    private Integer perPage;
    @SerializedName("current_page")
    @Expose
    private Integer currentPage;
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
    private List<Article> articles = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    private final static long serialVersionUID = 1825830781835364731L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Articles() {
    }

    /**
     * 
     * @param to
     * @param articles
     * @param nextPageUrl
     * @param status
     * @param prevPageUrl
     * @param from
     * @param currentPage
     * @param perPage
     */
    public Articles(Integer perPage, Integer currentPage, Object nextPageUrl, Object prevPageUrl, Integer from, Integer to, List<Article> articles, Integer status) {
        super();
        this.perPage = perPage;
        this.currentPage = currentPage;
        this.nextPageUrl = nextPageUrl;
        this.prevPageUrl = prevPageUrl;
        this.from = from;
        this.to = to;
        this.articles = articles;
        this.status = status;
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

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
