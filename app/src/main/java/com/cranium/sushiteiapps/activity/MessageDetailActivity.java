package com.cranium.sushiteiapps.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.api.MessageApi;
import com.cranium.sushiteiapps.model.Message;
import com.cranium.sushiteiapps.model.MessageRead;
import com.cranium.sushiteiapps.model.response.EditProfileResponseError;
import com.cranium.sushiteiapps.model.response.MessageUpdated;
import com.cranium.sushiteiapps.model.response.VoucherUpdated;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.SessionManager;
import com.cranium.sushiteiapps.util.TouchImageView;
import com.github.florent37.fiftyshadesof.FiftyShadesOf;
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
 * Created by Dayona on 6/16/17.
 */

public class MessageDetailActivity extends AppCompatActivity {

    @BindView(R.id.name) TextView name;
    @BindView(R.id.date) TextView date;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.title_bar) TextView titleBar;
    @BindView(R.id.image) TouchImageView image;

    private Message message;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private DatabaseHelper db;
    private boolean fromActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ButterKnife.bind(this);
        App.MESSAGE_COUNTER = 0;

        sessionManager = new SessionManager(this);

        sessionManager.setCountMessage(0);
        db = new DatabaseHelper(this);

        ShortcutBadger.removeCount(getApplicationContext());

        message = (Message) getIntent().getSerializableExtra("message");

        fromActivity = getIntent().getBooleanExtra("fromActivity",false);

        message.setReadAt((new Date()).toString());
        setToUpdated();
        progressDialog = new ProgressDialog(MessageDetailActivity.this);

        titleBar.setText(message.getShortName());
        name.setText(message.getName());
        date.setText(message.getFormattedStartDate());
        description.setText(message.getDescription());

        Picasso.with(this).load(App.URL + "files/messages/" + message.getImage()).error(R.drawable.image_720x405).into(image);
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

        MessageApi service = retrofit.create(MessageApi.class);

        Call<MessageUpdated> call = service.updateToRead(new MessageRead(""+message.getId(),sessionManager.getDeviceNumber()));

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

    @OnClick(R.id.image_back)
    public void doActionLeft(View view) {
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

    @Override
    public void onBackPressed() {
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

    @Override
    protected void onResume() {
        super.onResume();
        App.MESSAGE_COUNTER = 0;
    }
}
