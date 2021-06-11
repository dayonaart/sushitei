package com.cranium.sushiteiapps.activity;

import android.app.Dialog;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.api.MessageApi;
import com.cranium.sushiteiapps.api.VoucherApi;
import com.cranium.sushiteiapps.model.Message;
import com.cranium.sushiteiapps.model.Voucher;
import com.cranium.sushiteiapps.model.VoucherCode;
import com.cranium.sushiteiapps.model.response.EditProfileResponseError;
import com.cranium.sushiteiapps.model.response.MessageUpdated;
import com.cranium.sushiteiapps.model.response.VoucherGet;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.SessionManager;
import com.cranium.sushiteiapps.util.TouchImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leolin.shortcutbadger.ShortcutBadger;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by suryamudti on 28/05/18.
 */

public class VoucherDetailActivity extends AppCompatActivity {

    @BindView(R.id.name) TextView name;
    @BindView(R.id.date) TextView date;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.title_bar) TextView titleBar;
    @BindView(R.id.code_voucher) TextView codeVoucher;
    @BindView(R.id.descVoucher) TextView descVoucher;
    @BindView(R.id.image) TouchImageView image;
    @BindView(R.id.voucher_button) AppCompatButton btnVoucher;
    @BindView(R.id.voucher_use) AppCompatButton btnUse;
    @BindView(R.id.scroll) ScrollView scroll;

    private Message message;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private DatabaseHelper db;
    private boolean isGetting = false;
    private boolean fromActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_detail);
        ButterKnife.bind(this);

        ShortcutBadger.removeCount(getApplicationContext());

        App.VOUCHER_COUNTER = 0;

        sessionManager = new SessionManager(this);
        sessionManager.setCountVoucher(0);
        db = new DatabaseHelper(this);

        message = (Message) getIntent().getSerializableExtra("message");
        Log.e(App.LOG,"messssss "+message);

        fromActivity = getIntent().getBooleanExtra("fromActivity",false);

        progressDialog = new ProgressDialog(VoucherDetailActivity.this);

        titleBar.setText(message.getShortName());
        name.setText(message.getName());
        date.setText(message.getFormattedStartDate());
        description.setText(message.getDescription());
        codeVoucher.setVisibility(View.GONE);
        descVoucher.setVisibility(View.GONE);

        Picasso.with(this).load(App.URL + "files/messages/" + message.getImage()).error(R.drawable.image_720x405).into(image);

        checkVoucher();
        setToUpdated();
        scroll.fullScroll(ScrollView.FOCUS_UP);

        if(fromActivity){
            if(message.getExpired()==1){
                btnVoucher.setEnabled(false);
                btnVoucher.setText("Expired");
            }
        }


    }

    private void setToUpdated() {

        OkHttpClient client;

        if (sessionManager.isLoggedIn()){
            client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();
        }else{
            client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(App.API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        VoucherApi service = retrofit.create(VoucherApi.class);

        Call<MessageUpdated> call = service.updateToRead(new Voucher(""+message.getId(),sessionManager.getDeviceNumber()));

        call.enqueue(new Callback<MessageUpdated>() {
            @Override
            public void onResponse(Call<MessageUpdated> call, Response<MessageUpdated> response) {

                if (response.raw().isSuccessful()) {
                    Log.e(App.LOG,"message id "+message.getId()+" is updated to read");
                }
            }

            @Override
            public void onFailure(Call<MessageUpdated> call, Throwable t) {
            }
        });

    }

    /**
     * this method is for check voucher from API based on login status
    * */
    private void checkVoucher(){
        progressDialog = ProgressDialog.show(VoucherDetailActivity.this, "Loading", "Please wait ...", true);
        OkHttpClient client = null;
        if (sessionManager.isLoggedIn()){
            client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();
        }else{
            client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(App.API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        VoucherApi service = retrofit.create(VoucherApi.class);

        Call<VoucherGet> call = service.checkVoucher(new Voucher(message.getId().toString(),sessionManager.getDeviceNumber()));
        call.enqueue(new Callback<VoucherGet>() {
            @Override
            public void onResponse(Call<VoucherGet> call, Response<VoucherGet> response) {
                if (response.raw().isSuccessful()) {
                    Log.e(App.LOG,"check voucher MESSAGE :"+ response.body().getMesssage());
                    if (response.body().getMesssage().equals("Voucher already used")){
                        btnVoucher.setVisibility(View.GONE);
                        if (sessionManager.isLoggedIn()) {
                            descVoucher.setVisibility(View.VISIBLE);
                            codeVoucher.setVisibility(View.VISIBLE);
                            codeVoucher.setText(response.body().getData().getVoucherCode());
                        }else{
                            codeVoucher.setVisibility(View.GONE);
                            descVoucher.setVisibility(View.GONE);
                        }
                        btnUse.setVisibility(View.VISIBLE);
                        btnUse.setEnabled(false);
                        btnUse.setText("This voucher has been used");
                    }else if(response.body().getMesssage().equals("Voucher can be use")){
                        descVoucher.setVisibility(View.VISIBLE);
                        codeVoucher.setVisibility(View.VISIBLE);
                        codeVoucher.setText(response.body().getData().getVoucherCode());
                        btnUse.setVisibility(View.VISIBLE);
                        btnVoucher.setVisibility(View.GONE);
                    }else{
                        btnVoucher.setVisibility(View.VISIBLE);
                    }
                    progressDialog.dismiss();
                }else if(response.code()==400){
                    try {
                        progressDialog.dismiss();
                        btnVoucher.setEnabled(false);
                        Log.e("crannnn", response.errorBody().toString());
                        Gson gson = new GsonBuilder().create();
                        EditProfileResponseError mError = gson.fromJson(response.errorBody().string(), EditProfileResponseError.class);
                        Toast.makeText(VoucherDetailActivity.this, mError.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(VoucherDetailActivity.this,"Something wrong\nPlease check the network connection !",Toast.LENGTH_LONG).show();
                        // handle failure to read error
                    }
                }else{
                    progressDialog.dismiss();
                    btnVoucher.setEnabled(false);
                    Toast.makeText(VoucherDetailActivity.this, "This voucher already not available !", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VoucherGet> call, Throwable t) {
                Toast.makeText(VoucherDetailActivity.this,"Please check the network connection !",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                btnVoucher.setEnabled(false);
            }
        });
    }

    /**
     * this method is for handle get voucher button then store the request to API based on login status
     * @param view is the get button view
     */
    @OnClick(R.id.voucher_button)
    public void doGetVoucher(final View view){
            progressDialog = ProgressDialog.show(VoucherDetailActivity.this, "Loading", "Please wait ...", true);

            OkHttpClient client;
            if(sessionManager.isLoggedIn()){
                client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request newRequest  = chain.request().newBuilder()
                                .addHeader("Content-Type", "application/json")
                                .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                                .build();
                        return chain.proceed(newRequest);
                    }
                }).build();
            }else{
                client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request newRequest  = chain.request().newBuilder()
                                .addHeader("Content-Type", "application/json")
                                .build();
                        return chain.proceed(newRequest);
                    }
                }).build();
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(App.API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            VoucherApi service = retrofit.create(VoucherApi.class);

            Call<VoucherGet> call = service.getVoucher(new Voucher(message.getId().toString(),sessionManager.getDeviceNumber()));

            call.enqueue(new Callback<VoucherGet>() {
                @Override
                public void onResponse(Call<VoucherGet> call, Response<VoucherGet> response) {
                    Log.e("SURYA", response.message());
                    if (response.raw().isSuccessful()) {
                        progressDialog.dismiss();
                        view.setVisibility(View.GONE);
                        descVoucher.setVisibility(View.VISIBLE);
                        codeVoucher.setVisibility(View.VISIBLE);
                        codeVoucher.setText(response.body().getData().getVoucherCode());
                        scroll.fullScroll(ScrollView.FOCUS_UP);

//                        db.updateVoucher(response.body().getData().getVoucherCode(),message.getId());
//                        isGetting = true;

                        btnUse.setVisibility(View.VISIBLE);
                    }else if(response.code()==400){
                        try {
                            progressDialog.dismiss();
                            Log.e("crannnn", response.errorBody().toString());
                            Gson gson = new GsonBuilder().create();
                            EditProfileResponseError mError = gson.fromJson(response.errorBody().string(), EditProfileResponseError.class);
                            Toast.makeText(VoucherDetailActivity.this, mError.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            progressDialog.dismiss();
                            Toast.makeText(VoucherDetailActivity.this,"Something wrong\nPlease check the network connection !",Toast.LENGTH_LONG).show();
                            // handle failure to read error
                        }
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(VoucherDetailActivity.this, "This voucher already not available !", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<VoucherGet> call, Throwable t) {
                    Toast.makeText(VoucherDetailActivity.this,"Please check the network connection !",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
    }

    /**
     * this method is for handle use voucher button then store the request to API based on login status
     * @param view is the use button view
     */
    @OnClick(R.id.voucher_use)
    public void doUsingVoucher(final View view){
        if (sessionManager.isLoggedIn()){
            AlertDialog.Builder mDialog = new AlertDialog.Builder(VoucherDetailActivity.this);
            mDialog.setTitle("Alert");
            mDialog.setMessage("Are you sure you want to use the voucher ?");
            mDialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog,int which) {
                    progressDialog = ProgressDialog.show(VoucherDetailActivity.this, "Loading", "Please wait ...", true);

                    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            Request newRequest  = chain.request().newBuilder()
                                    .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                                    .addHeader("Content-Type", "application/json")
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    }).build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .client(client)
                            .baseUrl(App.API)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final String voucherCode = codeVoucher.getText().toString();
                    VoucherApi service = retrofit.create(VoucherApi.class);
                    Call<VoucherGet> call = service.processVoucher(new VoucherCode(message.getId().toString(),sessionManager.getDeviceNumber(),voucherCode));
                    call.enqueue(new Callback<VoucherGet>() {
                        @Override
                        public void onResponse(Call<VoucherGet> call, Response<VoucherGet> response) {
                            Log.e("SURYA", response.message() +" "+message.getId().toString()+" "+sessionManager.getDeviceNumber()+" "+voucherCode);
                            if (response.raw().isSuccessful()) {
                                progressDialog.dismiss();
                                view.setEnabled(false);
                                setDialogBox("Thank you !\nYour voucher has been used");
                                isGetting = false;
                                scroll.fullScroll(ScrollView.FOCUS_UP);
                            }else if(response.code()==400){
                                try {
                                    progressDialog.dismiss();
                                    Log.e("crannnn", response.errorBody().toString());
                                    Gson gson = new GsonBuilder().create();
                                    EditProfileResponseError mError = gson.fromJson(response.errorBody().string(), EditProfileResponseError.class);
                                    Toast.makeText(VoucherDetailActivity.this, response.message() +" "+response.code(), Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(VoucherDetailActivity.this,"Something wrong\nPlease check the network connection !",Toast.LENGTH_LONG).show();
                                    // handle failure to read error
                                }
                            }else if(response.code()==401){
                                progressDialog.dismiss();
                                view.setEnabled(false);
                                Toast.makeText(VoucherDetailActivity.this,  "This voucher is already unavailable !", Toast.LENGTH_LONG).show();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(VoucherDetailActivity.this, response.message() +" "+response.code(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<VoucherGet> call, Throwable t) {
                            Toast.makeText(VoucherDetailActivity.this,"Please check the network connection !",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
                }

            });
            mDialog.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = mDialog.create();
            alert.show();
        }else{

            AlertDialog.Builder mDialog = new AlertDialog.Builder(VoucherDetailActivity.this);
            mDialog.setTitle("Alert");
            mDialog.setMessage("You are not logged in, \nAfter login you can use the voucher, \nContinue to login page ?");
            mDialog.setPositiveButton("Login",new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog,int which) {
                    Intent intent = new Intent(VoucherDetailActivity.this,LoginActivity.class);
                    intent.putExtra("voucher","voucher");
                    startActivity(intent);
                }

            });
            mDialog.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = mDialog.create();
            alert.show();
        }
    }

    @OnClick(R.id.image_back)
    public void doActionLeft(View view) {
        if (isGetting){
            AlertDialog.Builder mDialog = new AlertDialog.Builder(VoucherDetailActivity.this);
            mDialog.setTitle("Alert");
            mDialog.setMessage("if you leave this page you may not get the voucher again,\nAre you sure ?");
            mDialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent;
                    intent = new Intent(getApplicationContext(), VoucherActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            mDialog.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = mDialog.create();
            alert.show();
        }else{
            if (fromActivity){
                finish();
            }else{
                Intent intent;
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isGetting){
            AlertDialog.Builder mDialog = new AlertDialog.Builder(VoucherDetailActivity.this);
            mDialog.setTitle("Alert");
            mDialog.setMessage("if you leave this page you may not be able get the voucher again,\nAre you sure ?");
            mDialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent;
                    intent = new Intent(getApplicationContext(), VoucherActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            mDialog.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = mDialog.create();
            alert.show();
        }else{
            if (fromActivity){
                finish();
            }else{
                Intent intent;
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

        }

    }

    /**
     * this method is for showing adaptable dialog box with ok button
     * @param text is custome text string
    * */
    private void setDialogBox(String text) {
        final Dialog dialog = new Dialog(VoucherDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmation_dialog_voucher);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView title = (TextView) dialog.findViewById(R.id.title);
        AppCompatButton ok = (AppCompatButton) dialog.findViewById(R.id.ok_button);

        title.setText(text);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                checkVoucher();
            }
        });

        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.VOUCHER_COUNTER = 0;
    }
}
