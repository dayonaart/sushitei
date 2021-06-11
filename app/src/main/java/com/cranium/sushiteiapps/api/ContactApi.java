package com.cranium.sushiteiapps.api;

import com.cranium.sushiteiapps.model.request.ContactRequest;
import com.cranium.sushiteiapps.model.response.ContactResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Dayona on 6/05/17.
 */

public interface ContactApi {

    @POST("send-inquiries")
    @Headers({
            "Content-Type: application/json"
    })
    Call<ContactResponse> sendInquiries(@Body ContactRequest body);

}
