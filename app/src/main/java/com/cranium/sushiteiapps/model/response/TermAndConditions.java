
package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;
import java.util.List;

import com.cranium.sushiteiapps.model.TermCondition;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TermAndConditions implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<TermCondition> termConditions = null;
    private final static long serialVersionUID = 8358799738536161757L;

    /**
     * No args constructor for use in serialization
     *
     */
    public TermAndConditions() {
    }

    /**
     *
     * @param status
     * @param termConditions
     */
    public TermAndConditions(Integer status, List<TermCondition> termConditions) {
        super();
        this.status = status;
        this.termConditions = termConditions;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<TermCondition> getTermConditions() {
        return termConditions;
    }

    public void setTermConditions(List<TermCondition> termConditions) {
        this.termConditions = termConditions;
    }

}