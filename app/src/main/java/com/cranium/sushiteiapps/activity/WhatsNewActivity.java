package com.cranium.sushiteiapps.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.adapter.WhatsNewListAdapter;
import com.cranium.sushiteiapps.adapter.WhatsNewPaginationAdapter;
import com.cranium.sushiteiapps.api.PromoBannerApi;
import com.cranium.sushiteiapps.api.WhatsNewApi;
import com.cranium.sushiteiapps.model.Article;
import com.cranium.sushiteiapps.model.PromoBanner;
import com.cranium.sushiteiapps.model.response.Articles;
import com.cranium.sushiteiapps.model.response.PromoBanners;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.SessionManager;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

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

public class WhatsNewActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.whats_new_recycler_view) RecyclerView whatsNewRecyclerView;
    @BindView(R.id.progress_whats_new) ProgressBar progressWhatsNew;
    @BindView(R.id.promo_banner_slider) SliderLayout promoBannerSlider;
    @BindView(R.id.promo_banner_slider_indicator) PagerIndicator promoBannerSliderIndicator;
    @BindView(R.id.last_updated) TextView lastUpdated;
    @BindView(R.id.iv_refresh) ImageView ivRefresh;

    private ProgressDialog progressDialog;

    private WhatsNewListAdapter whatsNewListAdapter;
    private List<Article> articleList;
    private SessionManager sessionManager;
    private DatabaseHelper db;

    private Retrofit retrofit;
    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    @OnClick(R.id.image_back)
    public void doActionLeft(View view) {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_new);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);
        db = new DatabaseHelper(this);
        lastUpdated.setText(sessionManager.getKeyWhatsNewUpdated());

        articleList = new ArrayList<>();

        progressDialog = new ProgressDialog(WhatsNewActivity.this);
        whatsNewListAdapter = new WhatsNewListAdapter(this, articleList);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        whatsNewRecyclerView.setLayoutManager(layoutManager);
        whatsNewRecyclerView.setNestedScrollingEnabled(false);
        whatsNewRecyclerView.setHorizontalScrollBarEnabled(true);
        whatsNewRecyclerView.setItemAnimator(new DefaultItemAnimator());

        whatsNewRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);
                if (loading) {
                    Log.v("...", " Reached Last Item a "+dy);
                    if (dy > 0){ //check for scroll down
                        Log.v("...", " Reached Last Item b "+dy);
                        visibleItemCount = layoutManager.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            currentPage+=1;
                            Log.v("...", "Last Item Wow ! "+totalItemCount);
                            //Do pagination.. i.e. fetch new data
                            loadNextPage();
                        }
                    }
                }
            }
        });

        whatsNewRecyclerView.setAdapter(whatsNewListAdapter);

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
        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(WhatsNewActivity.this, "Reload Article", "Please wait ...", true);
                getWhatsNewList(1);
                getPromoSliders(1);
            }
        });

        getWhatsNewList(0);
        getPromoSliders(0);
    }

    /**
     * get whats new list
     * @param type is the load type (
     *             0 = load whatsnewlist from local db,
     *             otherwise load data from local db
     */
    private void getWhatsNewList(int type) {

        if (db.getAlArticles().size()==0 || type==1){
            WhatsNewApi service = retrofit.create(WhatsNewApi.class);

            String queryLatitude = null;
            String queryLongitude = null;
            String queryRadius = null;

            Call<Articles> call = service.articles(queryLatitude,  queryLongitude, queryRadius, PAGE_START);
            call.enqueue(new Callback<Articles>() {
                @Override
                public void onResponse(Call<Articles> call, Response<Articles> response) {
                    if (response.raw().isSuccessful()) {
                        articleList.clear();
                        sessionManager.setKeyWhatsNewUpdated();
                        lastUpdated.setText(sessionManager.getKeyWhatsNewUpdated());

                        db.createArticle(response.body());
                        articleList.clear();
                        articleList.addAll(response.body().getArticles());
                        Log.i("cranium", "Article is");
                        currentPage = response.body().getCurrentPage();
                        whatsNewListAdapter.notifyDataSetChanged();

                        progressWhatsNew.setVisibility(View.GONE);
                        progressDialog.dismiss();
                    }else{
                        Toast.makeText(getBaseContext(),"response "+response.code(),Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<Articles> call, Throwable t) {
                    progressWhatsNew.setVisibility(View.GONE);
                    whatsNewListAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            });
        }else{
            sessionManager.setKeyWhatsNewUpdated();
            lastUpdated.setText(sessionManager.getKeyWhatsNewUpdated());
            articleList.clear();
            articleList.addAll(db.getAlArticles());
            whatsNewListAdapter.notifyDataSetChanged();

            progressWhatsNew.setVisibility(View.GONE);
            progressDialog.dismiss();
        }
    }


    private void getPromoSliders(int type) {
        if (db.getAllPromoBanners().size()==0 || type==1){
            PromoBannerApi service = retrofit.create(PromoBannerApi.class);

            Call<PromoBanners> call = service.promoBanners();

            call.enqueue(new Callback<PromoBanners>() {
                @Override
                public void onResponse(Call<PromoBanners> call, Response<PromoBanners> response) {

                    if (response.raw().isSuccessful()) {

                        if (response.body().getPromoBanners().equals(new ArrayList<PromoBanner>())) {
                            sliderException();
                        } else if (response.body().getPromoBanners().size() != db.getAllPromoBanners().size()) {

                            for (PromoBanner promoBanner : response.body().getPromoBanners()) {

                                DefaultSliderView textSliderView = new DefaultSliderView(WhatsNewActivity.this);

                                if (promoBanner.getImage() != null) {

                                    String image = App.URL + "/files/promo-banners/" + promoBanner.getImage();
                                    textSliderView
                                            .image(image)
                                            .setScaleType(BaseSliderView.ScaleType.Fit)
                                            .setOnSliderClickListener(WhatsNewActivity.this);

                                    promoBannerSlider.addSlider(textSliderView);
                                }
                            }
                            db.deleteAlLBanner();
                            db.createPromoBanner(response.body());
                        }
                        promoBannerSlider.setDuration(4000);
                        promoBannerSlider.setPresetTransformer(SliderLayout.Transformer.Stack);
                        promoBannerSlider.setCustomIndicator(promoBannerSliderIndicator);

                    } else {
                        sliderException();
                    }
                }

                @Override
                public void onFailure(Call<PromoBanners> call, Throwable t) {
                    sliderException();
                }
            });
        }else{
            if (db.getAllPromoBanners().equals(new ArrayList<PromoBanner>())) {
                sliderException();
            } else {

                for (PromoBanner promoBanner : db.getAllPromoBanners()) {

                    DefaultSliderView textSliderView = new DefaultSliderView(WhatsNewActivity.this);

                    if (promoBanner.getImage() != null) {

                        String image = App.URL + "/files/promo-banners/" + promoBanner.getImage();
                        textSliderView
                                .image(image)
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(WhatsNewActivity.this);

                        promoBannerSlider.addSlider(textSliderView);
                    }
                }
            }
            promoBannerSlider.setDuration(4000);
            promoBannerSlider.setPresetTransformer(SliderLayout.Transformer.Stack);
            promoBannerSlider.setCustomIndicator(promoBannerSliderIndicator);
        }
    }

    private void sliderException() {
        DefaultSliderView textSliderView = new DefaultSliderView(WhatsNewActivity.this);

        textSliderView
                .image(R.drawable.image_720x405)
                .setScaleType(BaseSliderView.ScaleType.Fit)
                .setOnSliderClickListener(WhatsNewActivity.this);

        promoBannerSlider.addSlider(textSliderView);
        promoBannerSlider.setDuration(4000);
        promoBannerSlider.setPresetTransformer(SliderLayout.Transformer.Stack);
        promoBannerSlider.setCustomIndicator(promoBannerSliderIndicator);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    /*load next page pagination*/
    public void loadNextPage(){

        final ProgressDialog dialog = ProgressDialog.show(WhatsNewActivity.this, "Loading", "Please wait ...", true);

        progressWhatsNew.setVisibility(View.VISIBLE);
            retrofit = new Retrofit.Builder()
                    .baseUrl(App.API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        WhatsNewApi service = retrofit.create(WhatsNewApi.class);
        String queryLatitude = null;
        String queryLongitude = null;
        String queryRadius = null;

        Call<Articles> call = service.articles(queryLatitude, queryLongitude, queryRadius, (currentPage));
        call.enqueue(new Callback<Articles>() {
            @Override
            public void onResponse(Call<Articles> call, Response<Articles> response) {
                if (response.raw().isSuccessful()) {
                    sessionManager.setKeyWhatsNewUpdated();
                    lastUpdated.setText(sessionManager.getKeyWhatsNewUpdated());
                    Log.i("Dayona", "Size" + response.body().getArticles().size());
                    int i;
                    for (i=0;i<response.body().getArticles().size();i++) {
                        articleList.add(response.body().getArticles().get(i));
                    }
                    whatsNewListAdapter.notifyDataSetChanged();

                    progressWhatsNew.setVisibility(View.GONE);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Articles> call, Throwable t) {
                progressWhatsNew.setVisibility(View.GONE);
                whatsNewListAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });


    }


}
