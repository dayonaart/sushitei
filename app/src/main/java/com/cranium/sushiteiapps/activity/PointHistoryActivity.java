package com.cranium.sushiteiapps.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.adapter.PaginationAdapter;
import com.cranium.sushiteiapps.adapter.PointHistoryListAdapter;
import com.cranium.sushiteiapps.api.PointApi;
import com.cranium.sushiteiapps.api.PointHistoryApi;
import com.cranium.sushiteiapps.api.UserApi;
import com.cranium.sushiteiapps.model.PointHistory;
import com.cranium.sushiteiapps.model.PointHistoryPage;
import com.cranium.sushiteiapps.model.User;
import com.cranium.sushiteiapps.model.request.EditProfileRequest;
import com.cranium.sushiteiapps.model.response.EditProfileResponse;
import com.cranium.sushiteiapps.model.response.EditProfileResponseError;
import com.cranium.sushiteiapps.model.response.PointHistories;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.InputFilterMinMax;
import com.cranium.sushiteiapps.util.PaginationScrollListener;
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
 * Created by Dayona on 6/20/17.
 */

public class PointHistoryActivity extends AppCompatActivity {

    @BindView(R.id.history_recycler_view) RecyclerView historyCeRecyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.parentLineHistoryPoint) LinearLayout parentLineHistory;
    @BindView(R.id.tvExpired) TextView tvExpired;

    private PaginationAdapter adapter;

    private ProgressDialog progressDialog;

    private PointHistoryListAdapter pointHistoryListAdapter;
    private List<PointHistory> pointHistoryList;
    private SessionManager sessionManager;
    private DatabaseHelper db;
    private Retrofit retrofit;

    private int TIMEOUT = 180;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_history);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);
        db = new DatabaseHelper(this);
        user = db.getUserByMemberCode(sessionManager.getMemberCode());

        pointHistoryList = new ArrayList<>();

        pointHistoryListAdapter = new PointHistoryListAdapter(this, pointHistoryList);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        historyCeRecyclerView.setLayoutManager(layoutManager);
        historyCeRecyclerView.setNestedScrollingEnabled(false);
        historyCeRecyclerView.setHorizontalScrollBarEnabled(true);
        historyCeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        historyCeRecyclerView.setAdapter(pointHistoryListAdapter);
        historyCeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);
                if (loading) {
                    Log.e("...", " Reached Last Item a "+dy);
                    if (dy > 0){ //check for scroll down
                        Log.e("...", " Reached Last Item b "+dy);
                        visibleItemCount = layoutManager.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                        if (dy>200){
                            recyclerView.stopScroll();
                            loadNextPage();
                        }
                    }
                }
            }
        });

        String tok = "";
        if(sessionManager.isLoggedIn()) {
            tok = "Bearer " + sessionManager.getToken();
        }
        final String finalTok = tok;
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", finalTok)
                        .addHeader("Content-Type", "application/json")
                        .build();
                return chain.proceed(newRequest);
            }
        }).readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(App.API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        getPointHistories(0);
    }

    /**
     * this method is for load point histories from API
     * @param type, is load type ( 1 = for load from API, 0 = for load from local database)
    * */
    private void getPointHistories(int type) {
        if (db.getAllHistoryPoint().size()==0 || type ==1){
            progressBar.setVisibility(View.VISIBLE);
            parentLineHistory.setVisibility(View.GONE);
            UserApi service = retrofit.create(UserApi.class);
            progressDialog = ProgressDialog.show(PointHistoryActivity.this, "Loading", "Please wait ...", true);

            Call<PointHistories> call = service.getPointHistories(sessionManager.getMemberCode());

            call.enqueue(new Callback<PointHistories>() {
                @Override
                public void onResponse(Call<PointHistories> call, Response<PointHistories> response) {
                    if (response.raw().isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        pointHistoryList.clear();
                        pointHistoryList.addAll(response.body().getUser().getPointHistories());

                        pointHistoryListAdapter.notifyDataSetChanged();
                        historyCeRecyclerView.smoothScrollToPosition(0);

                        if (response.body().getUser().getPointHistories().size()>0){
                            parentLineHistory.setVisibility(View.GONE);
                            db.deleteAlLHistoryPoint();
                            db.createHistoryPoint(response.body());

                            if(user.getDel() != 14 && !user.getStatus().equals("1")) { // show expired text
                                if(user.getStatusDescription().equals("Expired")) {
                                    tvExpired.setVisibility(View.VISIBLE);
                                }
                            }
                        }else
                            parentLineHistory.setVisibility(View.VISIBLE);

                        progressBar.setVisibility(View.GONE);
                        progressDialog.dismiss();

                        Log.e("LLLL","Dalam "+response.body().getUser().getPointHistories());
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),response.message(),Toast.LENGTH_LONG).show();;
                    }
                }

                @Override
                public void onFailure(Call<PointHistories> call, Throwable t) {
                    showDialog();
                    progressBar.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
            });
        }else if(db.getAllHistoryPoint().size()>0 || type == 0){
            Log.e("LLLL","Dalam getpointhistorias lebih 1");
            progressBar.setVisibility(View.VISIBLE);
            parentLineHistory.setVisibility(View.GONE);
            pointHistoryList.clear();
            pointHistoryList.addAll(db.getAllHistoryPoint());
            pointHistoryListAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            if (progressDialog!=null) progressDialog.dismiss();
            if(user.getDel() != 14 && !user.getStatus().equals("1")) { // show expired text
                if(user.getStatusDescription().equals("Expired")) {
                    tvExpired.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
    * load next page and show progress dialog
    * */
    private void loadNextPage() {
        final ProgressDialog progressDialogs = ProgressDialog.show(PointHistoryActivity.this, "Loading", "Please wait ...", true);

        Thread timer = new Thread() {

            public void run(){
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressDialogs.dismiss();
                progressDialogs.setCancelable(true);

            }
        };
        timer.start();
    }

    @OnClick(R.id.image_back)
    public void doActionLeft(View view) {
        super.onBackPressed();
    }

    @OnClick(R.id.button_refresh)
    public void doActionRefresh(View view) {
        getPointHistories(1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showDialog(){
        android.support.v7.app.AlertDialog.Builder mDialog = new android.support.v7.app.AlertDialog.Builder(this);
        mDialog.setMessage("Connection problem");
        mDialog.setPositiveButton("Retry",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                getPointHistories(1);
            }
        });
        mDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert = mDialog.create();
        alert.show();
    }

}
