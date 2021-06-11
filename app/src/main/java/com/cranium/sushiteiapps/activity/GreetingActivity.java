package com.cranium.sushiteiapps.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.api.GreetingApi;
import com.cranium.sushiteiapps.fragment.GreetingSlideFragment;
import com.cranium.sushiteiapps.model.Greeting;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.SessionManager;
import com.github.paolorotolo.appintro.AppIntro;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dayona on 06/06/17.
 */

public class GreetingActivity extends AppIntro {

    private SessionManager sessionManager;
    private Retrofit retrofit;
    private GreetingApi greetingApi;
    private DatabaseHelper databaseHelper;
    private List<Greeting> greetings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(App.API)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        greetingApi = retrofit.create(GreetingApi.class);

        sessionManager = new SessionManager(getApplicationContext());
        databaseHelper = new DatabaseHelper(this);

        greetings = databaseHelper.getAllGreeting(sessionManager.getIsGreeting());
        if (!greetings.equals(new ArrayList<>())) {
            for (Greeting g : greetings) {
                addSlide(GreetingSlideFragment.newInstance(R.layout.fragment_greeting_slide, g.getDescription(), g.getImage()));
            }
        } else {
            addSlide(GreetingSlideFragment.newInstance(R.layout.fragment_greeting_slide, "", ""));
        }

        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x1 = 0, x2;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;
                        if (Math.abs(deltaX) > 10) {
                            Log.i("cranium", "swipe to right" + pager.getCurrentItem() + 1 + " " + greetings.size());

                            if(pager.getCurrentItem() + 1 == greetings.size()) {
                                Intent intent = new Intent(GreetingActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        } else {

                        }
                        break;
                }
                return false;
            }
        });

        showDoneButton(false);
        showSkipButton(false);
        setFadeAnimation();
        setSeparatorColor(Color.parseColor("#00FFFFFF"));

//        setFinishOnTouchOutside(true);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);

    }
}
