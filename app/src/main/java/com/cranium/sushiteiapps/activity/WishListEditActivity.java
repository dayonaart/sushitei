package com.cranium.sushiteiapps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.api.WishlistApi;
import com.cranium.sushiteiapps.model.Wishlist;
import com.cranium.sushiteiapps.model.request.AddWishlistRequest;
import com.cranium.sushiteiapps.model.response.Wishlists;
import com.cranium.sushiteiapps.util.SessionManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by aldieemaulana on 6/21/17.
 */

public class WishListEditActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tag_name) TextView tagName;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @OnClick(R.id.image_back)
    public void doActionLeft(View view) {
        onBackPressed();
    }

    @OnClick(R.id.cancel_button)
    public void cancelButton(View view) {
        super.onBackPressed();
    }

    @OnClick(R.id.submit_button)
    public void doSave(View view) {
        saveData();
    }

    private Wishlist wishlist;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list_edit);
        ButterKnife.bind(this);

        wishlist = (Wishlist) getIntent().getSerializableExtra("wishlist");

        tagName.setText(wishlist.getName());

        sessionManager = new SessionManager(this);


    }


    protected void saveData() {
        if(!tagName.getText().equals("")) {
            progressBar.setVisibility(View.VISIBLE);

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(newRequest);
                }
            }).readTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(App.API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WishlistApi service = retrofit.create(WishlistApi.class);

            AddWishlistRequest request = new AddWishlistRequest(tagName.getText().toString());

            Call<Wishlists> call = service.updateWishlist(wishlist.getId(), request);
            call.enqueue(new Callback<Wishlists>() {
                @Override
                public void onResponse(Call<Wishlists> call, Response<Wishlists> response) {
                    if (response.raw().isSuccessful()) {
                        Toast.makeText(WishListEditActivity.this, "Saved!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(WishListEditActivity.this, WishListActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(WishListEditActivity.this, "Failed!", Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<Wishlists> call, Throwable t) {
                    Toast.makeText(WishListEditActivity.this, "Failed, Please try again.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}
