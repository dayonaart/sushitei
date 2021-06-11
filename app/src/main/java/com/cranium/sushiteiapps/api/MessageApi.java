package com.cranium.sushiteiapps.api;

import com.cranium.sushiteiapps.model.MessageLoad;
import com.cranium.sushiteiapps.model.MessageRead;
import com.cranium.sushiteiapps.model.Vouchers;
import com.cranium.sushiteiapps.model.response.MessageUpdated;
import com.cranium.sushiteiapps.model.response.Messages;
import com.cranium.sushiteiapps.model.response.Messagess;
import com.cranium.sushiteiapps.model.response.VoucherGet;
import com.cranium.sushiteiapps.model.response.VoucherUpdated;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Dayona on 6/05/17.
 */

public interface MessageApi {

    @POST("message/d")
    @Headers({
            "Content-Type: application/json"
    })
    Call<Messages> messages(@Body MessageLoad load);

    @GET(" /d/{date}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<Messagess> messagess(@Path("date") String date);

    @GET("messages/de/{token}/{device_number}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<Messages> firstMessage(@Path("token") String token, @Path("device_number") String deviceNumber);

    @GET("messages/d/{date}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<Messages> messages(@Path("date") String datee, @Body Messages body);

    @GET("logooo/{date}/{id}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<Messages> logooo(@Path("date") String datee, @Path("id") String id);

    @POST("message/read")
    @Headers({
            "Content-Type: application/json"
    })
    Call<MessageUpdated> updateToRead(@Body MessageRead messageRead);

    @POST("message/evoucher/{id}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<VoucherUpdated> processVoucher(@Path("id") Integer id);

    @POST("message/getvouchercode/{id}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<VoucherGet> getVoucherCode(@Path("id") Integer id);

}
