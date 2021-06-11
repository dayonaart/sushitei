package com.cranium.sushiteiapps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.adapter.WishlistListAdapter;
import com.cranium.sushiteiapps.api.WishlistApi;
import com.cranium.sushiteiapps.model.Wishlist;
import com.cranium.sushiteiapps.model.response.Wishlists;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

public class WishListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.wish_lists_recycler_view) RecyclerView wishListRecyclerView;
    @BindView(R.id.progress_wish_list) ProgressBar progressWishList;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    private WishlistListAdapter wishlistListAdapter;
    private List<Wishlist> wishlistList;

    public SessionManager sessionManager;
    private DatabaseHelper databaseHelper;
    public Retrofit retrofit;

    @OnClick(R.id.image_back)
    public void doActionLeft(View view) {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    @OnClick(R.id.new_button)
    public void doNewTag(View view) {
        Intent intent = new Intent(WishListActivity.this, WishListAddActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);
        databaseHelper = new DatabaseHelper(this);

        wishlistList = new ArrayList<>();
        wishlistListAdapter = new WishlistListAdapter(this, wishlistList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        wishListRecyclerView.setLayoutManager(layoutManager);
        wishListRecyclerView.setNestedScrollingEnabled(false);
        wishListRecyclerView.setHorizontalScrollBarEnabled(true);
        wishListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        wishListRecyclerView.setAdapter(wishlistListAdapter);

        if (sessionManager.isLoggedIn()) {
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
                    .connectTimeout(App.TIMEOUT, TimeUnit.SECONDS).build();
            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(App.API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        } else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(App.API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        swipeContainer.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getWishListNew();

            }
        });

        getWishListNew();
    }

    /**
     * set whats new
     */
    public void getWishListNew() {
        try{
            swipeContainer.setRefreshing(false);

            WishlistApi service = retrofit.create(WishlistApi.class);

            Call<Wishlists> call = service.wishlists();

            call.enqueue(new Callback<Wishlists>() {
                @Override
                public void onResponse(Call<Wishlists> call, Response<Wishlists> response) {
                    if (response.raw().isSuccessful()) {

                        swipeContainer.setRefreshing(false);

                        wishlistList.clear();
                        wishlistList.addAll(response.body().getWishlists());
                        wishlistListAdapter.notifyDataSetChanged();

                        progressWishList.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<Wishlists> call, Throwable t) {
                    swipeContainer.setRefreshing(false);

                    progressWishList.setVisibility(View.GONE);
                    wishlistListAdapter.notifyDataSetChanged();
                }
            });
        }catch(Exception e){

        }


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

}
