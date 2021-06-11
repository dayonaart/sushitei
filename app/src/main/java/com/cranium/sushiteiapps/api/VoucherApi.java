package com.cranium.sushiteiapps.api;

import com.cranium.sushiteiapps.model.Voucher;
import com.cranium.sushiteiapps.model.VoucherCode;
import com.cranium.sushiteiapps.model.VoucherExpired;
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

/**
 * Created by Dayona on 6/05/17.
 */

public interface VoucherApi {

    @GET("voucher/d/{date}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<Messages> messages(@Path("date") String datee);

    @POST("voucher/d")
    @Headers({
            "Content-Type: application/json"
    })
    Call<Messages> voucherPost(@Body Vouchers body);

    @GET("voucher/de/{token}/{device_number}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<Messages> firstVoucher(@Path("token") String token, @Path("device_number") String deviceNumber);

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
    Call<MessageUpdated> updateToRead(@Body Voucher voucherRead);

    @POST("process_evoucher")
    @Headers({
            "Content-Type: application/json"
    })
    Call<VoucherGet> processVoucher(@Body VoucherCode body);

    @POST("message/delete/voucher")
    @Headers({
            "Content-Type: application/json"
    })
    Call<VoucherGet> deleteVoucher(@Body VoucherExpired body);

    @POST("voucher/getvouchercode")
    @Headers({
            "Content-Type: application/json"
    })
    Call<VoucherGet> getVoucher(@Body Voucher body);

    @POST("message/getvouchercode/{id}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<VoucherGet> getVoucherCode(@Path("id") Integer id);

    @POST("voucher/checkvoucher")
    @Headers({
            "Content-Type: application/json"
    })
    Call<VoucherGet> checkVoucher(@Body Voucher body);


}
