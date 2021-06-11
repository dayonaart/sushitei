package com.cranium.sushiteiapps.activity;
import com.cranium.sushiteiapps.BuildConfig;
import com.cranium.sushiteiapps.util.SessionManager;

import butterknife.BindView;
import me.leolin.shortcutbadger.ShortcutBadger;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.api.GreetingApi;
import com.cranium.sushiteiapps.model.response.Greetings;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.VersionChecker;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.judemanutd.autostarter.AutoStartPermissionHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by aldieemaulana on 5/24/17.
 */

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;

    private SessionManager sessionManager;
    private Retrofit retrofit;
    private GreetingApi greetingApi;
    private DatabaseHelper databaseHelper;
    private Timer timer;

    @BindView(R.id.relativeLayoutSplash) RelativeLayout relLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        timer = new Timer();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(App.API)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        sessionManager = new SessionManager(getApplicationContext());
        databaseHelper = new DatabaseHelper(this);

        autoStart();
        try{
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    setGreetings();
                }
            }, 0, 5000);
        }catch (Exception e){

        }

    }

    private void autoStart(){

    }

    private void setGreetings() {

        greetingApi = retrofit.create(GreetingApi.class);

        if (!sessionManager.getIsGreetingFirstOpenDone()) {
            Log.e(App.LOG, "first Open ");
            Call<Greetings> call = greetingApi.greetingsFirstOpen();
            call.enqueue(new Callback<Greetings>() {
                @Override
                public void onResponse(Call<Greetings> call, final Response<Greetings> response) {

                    if (response.raw().isSuccessful()) {
                        Log.e("Success","Splash Activity firstopen");
                        sessionManager.setIsGreetingFirstOpenDone(true);
                        sessionManager.setIsGreeting("firstopen");
                        databaseHelper.createGreeting(response.body(), "firstopen");
                        timer.cancel();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SessionManager session = new SessionManager(getApplicationContext());

                                Intent intent;
                                if (session.getToken() != null) {
                                    intent = new Intent(SplashActivity.this, AllowLocationActivity.class);
                                }else {
                                    intent = new Intent(SplashActivity.this, AllowLocationActivity.class);
                                }

                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                                finish();

                            }
                        }, SPLASH_TIME_OUT);
                    }

                }

                @Override
                public void onFailure(Call<Greetings> call, Throwable t) {
                    Log.e("Failure","Splash Activity firstopen");
                    showSnackbar();
                }
            });
        } else {
            Log.i("cranium", "test2");
            if (databaseHelper.getAllGreeting("afterfirstopen").size()==0 || sessionManager.getIsGreetingFirstOpenDone()){
                Log.e(App.LOG,"size "+databaseHelper.getAllGreeting("afterfirstopen").size()+" getIsGreetingFirstOpenDone"+sessionManager.getIsGreetingFirstOpenDone());
                Call<Greetings> call = greetingApi.greetingsAfterFirstOpen();
                call.enqueue(new Callback<Greetings>() {
                    @Override
                    public void onResponse(Call<Greetings> call, final Response<Greetings> response) {

                        if (response.raw().isSuccessful()) {
                            sessionManager.setIsGreetingFirstOpenDone(true);
                            sessionManager.setIsGreetingSecondOpenDone(true);
                            sessionManager.setIsGreeting("afterfirstopen");
                            databaseHelper.createGreeting(response.body(), "afterfirstopen");
                            timer.cancel();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    SessionManager session = new SessionManager(getApplicationContext());
                                    Intent intent;
                                    if (session.getToken() != null) {
                                        intent = new Intent(SplashActivity.this, AllowLocationActivity.class);
                                    }else {
                                        intent = new Intent(SplashActivity.this, AllowLocationActivity.class);
                                    }

                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                                    finish();
                                }
                            }, SPLASH_TIME_OUT);
                        }

                    }

                    @Override
                    public void onFailure(Call<Greetings> call, Throwable t) {
                        Log.e("Failure","Splash Activity afterfirstopen goto allow");
                        gotoAllowActivity();
                    }
                });
            }else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SessionManager session = new SessionManager(getApplicationContext());
                        timer.cancel();
                        Log.e(App.LOG,"Splash Activity afterfirstopen no network");
                        Intent intent;
                        if (session.getToken() != null) {
                            intent = new Intent(SplashActivity.this, AllowLocationActivity.class);
                        }else {
                            intent = new Intent(SplashActivity.this, AllowLocationActivity.class);
                        }

                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                        finish();

                    }
                }, SPLASH_TIME_OUT);
            }
        }
    }

    public void gotoAllowActivity(){
        if (sessionManager.getIsGreetingSecondOpenDone()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SessionManager session = new SessionManager(getApplicationContext());
                    timer.cancel();
                    Log.e(App.LOG,"Splash Activity afterfirstopen no network");
                    Intent intent;
                    if (session.getToken() != null) {
                        intent = new Intent(SplashActivity.this, AllowLocationActivity.class);
                    }else {
                        intent = new Intent(SplashActivity.this, AllowLocationActivity.class);
                    }

                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }else{
            showSnackbar();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void showSnackbar(){
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), "Connection problem", Snackbar.LENGTH_INDEFINITE)
                .setAction("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismissDialog(android.R.id.content);
                    }
                })
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setGreetings();
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.GREEN);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
}
