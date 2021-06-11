package com.cranium.sushiteiapps.api;

import com.cranium.sushiteiapps.model.request.CommentRequest;
import com.cranium.sushiteiapps.model.request.FirebaseRequest;
import com.cranium.sushiteiapps.model.response.CommentResponse;
import com.cranium.sushiteiapps.model.response.FirebaseResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Dayona on 6/05/17.
 */

public interface FirebaseApi {

    @POST("store-firebase")
    @Headers({
            "Content-Type: application/json"
    })
    Call<FirebaseResponse> sendToServer(@Body FirebaseRequest body);

}
