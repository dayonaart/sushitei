package com.cranium.sushiteiapps.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.api.RequestApi;
import com.cranium.sushiteiapps.model.response.TermAndConditions;
import com.cranium.sushiteiapps.util.DatabaseHelper;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dayona on 6/12/17.
 */

public class FaqSushiteiActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.title_bar) TextView Dee;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindColor(R.color.colorDark) int dark;

    private String tempTerm = "";

    private DatabaseHelper databaseHelper;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.image_back)
    public void doActionLeft(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_sushitei);
        ButterKnife.bind(this);
        databaseHelper = new DatabaseHelper(this);
        loadTermAndConditions();
        Dee.setText("FAQ ");
        name.setText("FAQ ");
    }

    private void loadTermAndConditions() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestApi requestApi = retrofit.create(RequestApi.class);

        Call<TermAndConditions> call = requestApi.faq();
        call.enqueue(new Callback<TermAndConditions>() {
            @Override
            public void onResponse(Call<TermAndConditions> call, Response<TermAndConditions> response) {

                description.setText(response.body().getTermConditions().get(0).getDescription());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TermAndConditions> call, Throwable t) {

                description.setText("");
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
