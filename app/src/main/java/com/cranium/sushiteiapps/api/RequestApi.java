package com.cranium.sushiteiapps.api;

import com.cranium.sushiteiapps.model.response.Cities;
import com.cranium.sushiteiapps.model.response.Seats;
import com.cranium.sushiteiapps.model.response.TermAndConditions;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by Dayona on 6/05/17.
 */

public interface RequestApi {

    @GET("cities")
    @Headers({
            "Content-Type: application/json"
    })
    Call<Cities> cities();

    @GET("term-condition")
    @Headers({
            "Content-Type: application/json"
    })
    Call<TermAndConditions> termAndConditions();

    @GET("disclaimer")
    @Headers({
            "Content-Type: application/json"
    })
    Call<TermAndConditions> disclaimer();

    @GET("faq")
    @Headers({
            "Content-Type: application/json"
    })
    Call<TermAndConditions> faq();

    @GET("about-sushi-tei")
    @Headers({
            "Content-Type: application/json"
    })
    Call<TermAndConditions> aboutSushitei();

    @GET("seats")
    @Headers({
            "Content-Type: application/json"
    })
    Call<Seats> seats();

}
