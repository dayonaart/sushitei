package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;

import com.cranium.sushiteiapps.model.Article;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArticleResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private Article article;
    @SerializedName("next")
    @Expose
    private Article next;
    @SerializedName("previous")
    @Expose
    private Article previous;
    private final static long serialVersionUID = -795833213542024810L;

    /**
     * No args constructor for use in serialization
     *
     */
    public ArticleResponse() {
    }

    /**
     *
     * @param previous
     * @param article
     * @param status
     * @param next
     */
    public ArticleResponse(Integer status, Article article, Article next, Article previous) {
        super();
        this.status = status;
        this.article = article;
        this.next = next;
        this.previous = previous;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Article getNext() {
        return next;
    }

    public void setNext(Article next) {
        this.next = next;
    }

    public Article getPrevious() {
        return previous;
    }

    public void setPrevious(Article previous) {
        this.previous = previous;
    }

}