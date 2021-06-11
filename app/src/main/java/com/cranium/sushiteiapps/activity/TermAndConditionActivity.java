package com.cranium.sushiteiapps.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.api.RequestApi;
import com.cranium.sushiteiapps.model.TermCondition;
import com.cranium.sushiteiapps.model.response.TermAndConditions;
import com.cranium.sushiteiapps.util.DatabaseHelper;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dayona on 6/12/17.
 */

public class TermAndConditionActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.agree_img) ImageView agreeImg;
    @BindView(R.id.agree_btn) LinearLayout agreeBtn;
    @BindView(R.id.term_condition) TextView termCondition;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindColor(R.color.colorDark) int dark;

    private String tempTerm = "";

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_condition);
        ButterKnife.bind(this);

        databaseHelper = new DatabaseHelper(this);

        Drawable agreeDrawable = ContextCompat.getDrawable(this, R.drawable.ic_chevron_right_white_24dp);
        agreeDrawable.setColorFilter(dark, PorterDuff.Mode.SRC_ATOP);
        agreeImg.setImageDrawable(agreeDrawable);

        loadTermAndConditions();

        agreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadTermAndConditions() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestApi requestApi = retrofit.create(RequestApi.class);

        Call<TermAndConditions> call = requestApi.termAndConditions();
        call.enqueue(new Callback<TermAndConditions>() {
            @Override
            public void onResponse(Call<TermAndConditions> call, Response<TermAndConditions> response) {
                if (response.raw().isSuccessful()) {
                    databaseHelper.createTermCondition(response.body());
                }

                for (TermCondition term : databaseHelper.getAllTermCondition()) {
                    tempTerm += term.getDescription();
                }
                termCondition.setText(tempTerm);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TermAndConditions> call, Throwable t) {
                for (TermCondition term : databaseHelper.getAllTermCondition()) {
                    tempTerm += term.getDescription();
                }
                termCondition.setText(tempTerm);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
