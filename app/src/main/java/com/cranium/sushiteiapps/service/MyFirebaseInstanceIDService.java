package com.cranium.sushiteiapps.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.Config;
import com.cranium.sushiteiapps.activity.MainActivity;
import com.cranium.sushiteiapps.api.FirebaseApi;
import com.cranium.sushiteiapps.api.WishlistApi;
import com.cranium.sushiteiapps.model.Firebase;
import com.cranium.sushiteiapps.model.request.FirebaseRequest;
import com.cranium.sushiteiapps.model.response.FirebaseResponse;
import com.cranium.sushiteiapps.model.response.Wishlists;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.SessionManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hendrigunawan on 03/07/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    SessionManager session;
    DatabaseHelper db;
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.e("SURYA", "tokenssssah "+refreshedToken);
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        db = new DatabaseHelper(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        db.createFirebase(new Firebase(refreshedToken, getDeviceNumber()));
        Log.e("SURYA", "tokenssssah "+refreshedToken+"   "+db.getFirebase().getFirebaseToken());

        session.setFirebaseTokenKey(refreshedToken);

        // sending reg id to your server
        storeFirebaseToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e("cranium", "sendRegistrationToServer: " + token);
    }

    private void storeRegIdInPref(String token) {
        Log.e("cranium", " tokeeeeeen " + token);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }

    /**
     * this will return device id of android phone
     * @return id = devicenumber
     */
    private String getDeviceNumber(){
        String id = "";
        if (session.getDeviceNumber().equals("") || session.getDeviceNumber()==null){
            id = Settings.Secure.getString(getApplicationContext().getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }else{
            id = session.getDeviceNumber();
        }
        return id;
    }

    private void storeFirebaseToServer(String firebaseToken){
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build();
                return chain.proceed(newRequest);
            }
        }).readTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(App.TIMEOUT, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        int deviceStatus = 0;

        if (session.isLoggedIn()) deviceStatus = 1; else deviceStatus = 0;

        Log.e(App.LOG," "+firebaseToken+"\n"+db.getFirebase().getDeviceNumber()+"\n"+ deviceStatus);

        FirebaseApi service = retrofit.create(FirebaseApi.class);
        Call<FirebaseResponse> call = service.sendToServer(new FirebaseRequest(firebaseToken,db.getFirebase().getDeviceNumber(), deviceStatus));
        call.enqueue(new Callback<FirebaseResponse>() {
            @Override
            public void onResponse(Call<FirebaseResponse> call, Response<FirebaseResponse> response) {
                if (response.raw().isSuccessful()) {

                    Log.e(App.LOG,"token refreshed success "+response.message());

                }else {

                    Log.e(App.LOG," token refreshed fail "+response.message());

                }
            }

            @Override
            public void onFailure(Call<FirebaseResponse> call, Throwable t) {
                Log.e(App.LOG," token refreshed failure ");
            }
        });
    }

}
