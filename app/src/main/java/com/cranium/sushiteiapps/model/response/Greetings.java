
package com.cranium.sushiteiapps.model.response;

import java.io.Serializable;
import java.util.List;

import com.cranium.sushiteiapps.model.Greeting;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Greetings implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<Greeting> greetings = null;
    private final static long serialVersionUID = 7507812939178695255L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Greetings() {
    }

    /**
     * 
     * @param status
     * @param greetings
     */
    public Greetings(Integer status, List<Greeting> greetings) {
        super();
        this.status = status;
        this.greetings = greetings;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Greeting> getGreetings() {
        return greetings;
    }

    public void setGreetings(List<Greeting> greetings) {
        this.greetings = greetings;
    }

}
