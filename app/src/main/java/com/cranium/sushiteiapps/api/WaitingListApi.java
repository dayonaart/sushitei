package com.cranium.sushiteiapps.api;

import com.cranium.sushiteiapps.model.request.ReservationRequest;
import com.cranium.sushiteiapps.model.response.ReservationData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Dayona on 6/05/17.
 */

public interface WaitingListApi {

    @POST("reservation/create")
    @Headers({
            "Content-Type: application/json"
    })
    Call<ReservationData> createReservation(@Body ReservationRequest body);

    @GET("reservation/data")
    @Headers({
            "Content-Type: application/json"
    })
    Call<ReservationData> getData();

}
