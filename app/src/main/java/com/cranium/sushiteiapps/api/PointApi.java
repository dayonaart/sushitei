package com.cranium.sushiteiapps.api;

import com.cranium.sushiteiapps.model.PointHistoryPage;
import com.cranium.sushiteiapps.model.request.ContactRequest;
import com.cranium.sushiteiapps.model.response.ContactResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Dayona on 6/05/17.
 */

public interface PointApi {

    @GET("user/point-histories/{member_code}")
    Call<PointHistoryPage> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int pageIndex
    );

}
