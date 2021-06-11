package com.cranium.sushiteiapps.api;

import com.cranium.sushiteiapps.model.response.Greetings;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by Dayona on 6/06/17.
 */

public interface GreetingApi {

    @GET("greetings")
    @Headers({
            "Content-Type: application/json"
    })
    Call<Greetings> greetingsFirstOpen();

    @GET("greetings-after-first-open")
    @Headers({
            "Content-Type: application/json"
    })
    Call<Greetings> greetingsAfterFirstOpen();

}
