package com.cranium.sushiteiapps.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.api.WhatsNewApi;
import com.cranium.sushiteiapps.model.Article;
import com.cranium.sushiteiapps.model.response.ArticleResponse;
import com.cranium.sushiteiapps.util.SessionManager;
import com.cranium.sushiteiapps.util.TouchImageView;
import com.squareup.picasso.Picasso;

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

public class WhatsNewDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.title_bar) TextView titleBar;
    @BindView(R.id.image) TouchImageView image;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.date) TextView date;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.next) LinearLayout nextLinear;
    @BindView(R.id.previous) LinearLayout prevLinear;
    @BindView(R.id.share_image_button) ImageButton shareImageButton;

    @OnClick(R.id.image_back)
    public void doActionLeft(View view) {
        super.onBackPressed();
    }

    private Article article;
    private Retrofit retrofit;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_new_detail);
        ButterKnife.bind(this);

        article = (Article) getIntent().getSerializableExtra("article");
        sessionManager = new SessionManager(getApplicationContext());

        titleBar.setText(article.getShortName());

        Picasso.with(this).load(App.URL + "files/articles/list/" + article.getImage()).error(R.drawable.image_720x405).into(image);

        name.setText(article.getName());
        date.setText(article.getFormattedArticleDate());
        description.setText(article.getDescription());
        OkHttpClient client;

        if (sessionManager.isLoggedIn()) {
            client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
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
        }else{
            client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(newRequest);
                }
            }).readTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(App.TIMEOUT, TimeUnit.SECONDS).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(App.API)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
//        retrofit = new Retrofit.Builder()
//                .baseUrl(App.API)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
        getArticleDetail();

        shareImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, article.getName());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, article.getUrl());
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }

    private void getArticleDetail() {

        WhatsNewApi service = retrofit.create(WhatsNewApi.class);

        String queryLatitude = null;
        String queryLongitude = null;
        String queryRadius = null;

        Call<ArticleResponse> call = service.article(article.getId(), queryLatitude, queryLongitude, queryRadius);

        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                if (response.raw().isSuccessful()) {

                    Log.e(App.LOG,"success get article "+response.message()+" article id "+article.getId()+" code "+response.code());
                    if (response.body().getNext() != null) {
                        onClickNextButton(response.body().getNext());
                    }else if (response.body().getNext() == null){
                        nextLinear.setVisibility(View.GONE);
                    }

                    if (response.body().getPrevious() != null) {
                        onClickPreviousButton(response.body().getPrevious());
                    }else if (response.body().getPrevious() == null){
                        prevLinear.setVisibility(View.GONE);
                    }

                }else{
                    Log.e(App.LOG,"failed get article "+response.message()+" article id "+article.getId()+" code "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                Log.e(App.LOG,"failed get article");
            }
        });
    }

    private void onClickNextButton(final Article article) {

        nextLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(WhatsNewDetailActivity.this, WhatsNewDetailActivity.class);
                intent.putExtra("article", article);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

    }

    private void onClickPreviousButton(final Article article) {

        prevLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(WhatsNewDetailActivity.this, WhatsNewDetailActivity.class);
                intent.putExtra("article", article);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
