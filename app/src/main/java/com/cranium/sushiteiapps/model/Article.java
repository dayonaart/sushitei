
package com.cranium.sushiteiapps.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("article_date")
    @Expose
    private String articleDate;
    @SerializedName("meta_description")
    @Expose
    private String metaDescription;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;
    @SerializedName("url")
    @Expose
    private String url;
    private final static long serialVersionUID = 3146820368943638450L;

    /**
     * No args constructor for use in serialization
     */
    public Article() {
    }

    /**
     * @param updatedAt
     * @param id
     * @param createdAt
     * @param description
     * @param name
     * @param metaDescription
     * @param articleDate
     * @param image
     * @param url
     */
    public Article(Integer id, String name, String description, String image, String articleDate, String metaDescription, String createdAt, Object updatedAt, String url) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.articleDate = articleDate;
        this.metaDescription = metaDescription;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getArticleDate() {
        return articleDate;
    }

    public void setArticleDate(String articleDate) {
        this.articleDate = articleDate;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFormattedArticleDate() {
        String formattedDate;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd H:m:s", new Locale("en", "EN")).parse(this.getArticleDate());

            SimpleDateFormat outGoing = new SimpleDateFormat("EEEE, dd MMMM yyyy, hh:mm a", new Locale("en", "EN"));
            formattedDate = outGoing.format(date);
        } catch (ParseException e) {
            formattedDate = this.getArticleDate();
            e.printStackTrace();
        }

        return formattedDate;
    }

    public String getShortDescription() {
        return this.getCutDescription(this.getDescription(), 90);
    }

    public String getShortMetaDescription() {
        return this.getCutDescription(this.getMetaDescription(), 90);
    }

    public String getShortName() {
        return this.getCutDescription(this.getName(), 20);
    }

    private String getCutDescription(String description, Integer cut) {
        if (description.length() > cut) {
            return description.substring(0, cut) + "...";
        }

        return description;
    }
}
