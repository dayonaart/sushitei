package com.cranium.sushiteiapps.api;

import com.cranium.sushiteiapps.model.response.OutletCities;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by Dayona on 6/05/17.
 */

public interface OutletApi {

    @GET("outlet-cities")
    @Headers({
            "Content-Type: application/json"
    })
    Call<OutletCities> outletCities();
}
