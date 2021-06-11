package com.cranium.sushiteiapps.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by groun on 1/15/2018.
 */

public class PointHistoryApi {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://member.sushitei.id/api/v1/user/point-histories/")
                    .build();
        }
        return retrofit;
    }
}

