package com.cranium.sushiteiapps.api;

import com.cranium.sushiteiapps.model.request.EditProfileRequest;
import com.cranium.sushiteiapps.model.request.ForceLogoutRequest;
import com.cranium.sushiteiapps.model.request.ForgotPasswordRequest;
import com.cranium.sushiteiapps.model.request.LoginRequest;
import com.cranium.sushiteiapps.model.request.ProfileImageRequest;
import com.cranium.sushiteiapps.model.request.RegisterRequest;
import com.cranium.sushiteiapps.model.response.EditProfileResponse;
import com.cranium.sushiteiapps.model.response.ForceLogoutResponse;
import com.cranium.sushiteiapps.model.response.ForgotPasswordResponse;
import com.cranium.sushiteiapps.model.response.GetUser;
import com.cranium.sushiteiapps.model.response.LoginResponse;
import com.cranium.sushiteiapps.model.response.LogoutResponse;
import com.cranium.sushiteiapps.model.response.PointHistories;
import com.cranium.sushiteiapps.model.response.ProfileImageResponse;
import com.cranium.sushiteiapps.model.response.RegisterResponse;
import com.cranium.sushiteiapps.model.response.VerifyPasswordResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Dayona on 5/31/17.
 */

public interface UserApi {

    @POST("auth/login")
    @Headers({
            "Content-Type: application/json"
    })
    Call<LoginResponse> login(@Body LoginRequest body);

    @POST("force-logout")
    @Headers({
            "Content-Type: application/json"
    })
    Call<ForceLogoutResponse> forceLogout(@Body ForceLogoutRequest body);

    @POST("auth/logout")
    @Headers({
            "Content-Type: application/json"
    })
    Call<LogoutResponse> logout();

    @POST("auth/register")
    @Headers({
            "Content-Type: application/json"
    })
    Call<RegisterResponse> register(@Body RegisterRequest body);

    @POST("auth/forgot-password")
    @Headers({
            "Content-Type: application/json"
    })
    Call<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest body);

    @GET("user/{memberCode}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<GetUser> getUser(@Path("memberCode") String memberCode);

    @PUT("user/update-profile/{memberCode}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<EditProfileResponse> editProfile(@Body EditProfileRequest body, @Path("memberCode") String memberCode);

    @GET("user/reset/{memberCode}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<EditProfileResponse> resetProfile(@Path("memberCode") String memberCode);

    @POST("auth/verify-password")
    @Headers({
            "Content-Type: application/json"
    })
    Call<VerifyPasswordResponse> verifyPassword(@Body LoginRequest body);

    @GET("user/point-histories/{memberCode}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<PointHistories> getPointHistories(@Path("memberCode") String memberCode);

    @GET("user/point-histories/{memberCode}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<PointHistories> getPointHistories(
            @Path("memberCode") String memberCode,
            @Query("page") Integer page
    );

    @PUT("user/update-profile-image/{memberCode}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<ProfileImageResponse> uploadProfileImage(@Path("memberCode") String memberCode, @Body ProfileImageRequest body);


}
