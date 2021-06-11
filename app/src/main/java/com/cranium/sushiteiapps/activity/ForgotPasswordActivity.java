package com.cranium.sushiteiapps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.api.UserApi;
import com.cranium.sushiteiapps.model.request.ForgotPasswordRequest;
import com.cranium.sushiteiapps.model.response.ForgotPasswordResponse;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dayona on 6/12/17.
 */

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.next_button) AppCompatButton nextButton;
    @BindView(R.id.email) AutoCompleteTextView mEmailView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptForgotPassword();
            }
        });

    }

    @OnClick(R.id.action_cancel)
    protected void actionCancel() {
        super.onBackPressed();
    }

    public void doActionLeft(View view) {
        super.onBackPressed();
    }

    private void clearErrorFill() {
        // Reset errors.
        mEmailView.setError(null);
        mEmailView.requestFocus();
    }

    private void clearFill() {

        mEmailView.setText(null);
        mEmailView.requestFocus();
    }

    private void attemptForgotPassword() {

        clearErrorFill();
        App.hideSoftKeyboard(this);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!isValidEmail(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            progressBar.setVisibility(View.VISIBLE);
            // Show a progress spinner, and kick off a background task to
            // perform the user forgot password attempt.

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(App.API)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UserApi service = retrofit.create(UserApi.class);

            ForgotPasswordRequest request = new ForgotPasswordRequest(email);

            Call<ForgotPasswordResponse> call = service.forgotPassword(request);
            call.enqueue(new Callback<ForgotPasswordResponse>() {
                @Override
                public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {

                    if (response.raw().isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        clearFill();
                        /*goto main activity*/
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Reset password is failed", Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                    Toast.makeText(ForgotPasswordActivity.this, "Reset password is failed, Please try again.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

        }
    }

    private boolean isValidEmail(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && email.contains(".");
    }

}
