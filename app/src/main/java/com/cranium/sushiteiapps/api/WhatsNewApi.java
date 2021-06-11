package com.cranium.sushiteiapps.api;

import com.cranium.sushiteiapps.model.response.ArticleResponse;
import com.cranium.sushiteiapps.model.response.Articles;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Dayona on 5/28/17.
 */

public interface WhatsNewApi {

    @GET("articles")
    @Headers({
            "Content-Type: application/json"
    })
    Call<Articles> articles(
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("radius") String radius,
            @Query("page") Integer page
    );

    @POST("pagination-articles")
    @Headers({"Content-Type: application/json"})
    Call<Articles> articlesLoad(
            @Query("id") Integer lastId,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("radius") String radius
    );

    @GET("article/{id}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<ArticleResponse> article(
            @Path("id") Integer id,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("radius") String radius
    );





}
