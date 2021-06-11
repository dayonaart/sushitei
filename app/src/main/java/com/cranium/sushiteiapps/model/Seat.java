
package com.cranium.sushiteiapps.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Seat implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("code")
    @Expose
    private String code;
    private final static long serialVersionUID = -1345989413237974482L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Seat() {
    }

    /**
     *
     * @param id
     * @param name
     * @param code
     */
    public Seat(Integer id, String name, String code) {
        super();
        this.id = id;
        this.name = name;
        this.code = code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}