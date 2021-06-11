package com.cranium.sushiteiapps.api;

import com.cranium.sushiteiapps.model.response.PromoBanners;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by Dayona on 5/28/17.
 */

public interface PromoBannerApi {

    @GET("promo-banners")
    @Headers({
            "Content-Type: application/json"
    })
    Call<PromoBanners> promoBanners();

}
