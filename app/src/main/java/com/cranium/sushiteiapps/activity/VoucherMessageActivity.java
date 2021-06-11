package com.cranium.sushiteiapps.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.adapter.PaginationAdapter;
import com.cranium.sushiteiapps.adapter.PointHistoryListAdapter;
import com.cranium.sushiteiapps.api.UserApi;
import com.cranium.sushiteiapps.model.PointHistory;
import com.cranium.sushiteiapps.model.User;
import com.cranium.sushiteiapps.model.response.PointHistories;
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
 * Created by suryamudti on 25/05/18.
 */

public class VoucherMessageActivity extends AppCompatActivity {

    @BindView(R.id.voucher_count) TextView voucherCount;
    @BindView(R.id.message_count) TextView messageCount;
    @BindView(R.id.messageLayout) LinearLayout message;
    @BindView(R.id.voucherLayout) LinearLayout voucher;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_voucher);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);

        voucherCount.setText(sessionManager.getCountVoucher().toString());
        messageCount.setText(sessionManager.getCountMessage().toString());

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MessageActivity.class));
                finish();
            }
        });

        voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),VoucherActivity.class));
                finish();
            }
        });

    }

    @OnClick(R.id.image_back)
    public void doActionLeft(View view) {
        super.onBackPressed();
        Intent intent;
        intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent;
        intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}
