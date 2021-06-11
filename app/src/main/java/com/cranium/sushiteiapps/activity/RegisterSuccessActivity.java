package com.cranium.sushiteiapps.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.Config;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.api.UserApi;
import com.cranium.sushiteiapps.model.Register;
import com.cranium.sushiteiapps.model.User;
import com.cranium.sushiteiapps.model.request.LoginRequest;
import com.cranium.sushiteiapps.model.response.LoginResponse;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.SessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dayona on 6/12/17.
 */

public class RegisterSuccessActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.sign_in_button) AppCompatButton signInButton;
    @BindView(R.id.email) AutoCompleteTextView mEmailView;
    @BindView(R.id.password) EditText mPasswordView;
    @BindView(R.id.first_name) TextView firstName;
    @BindView(R.id.status_member) TextView statusMember;
    @BindView(R.id.member_code) TextView memberCode;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    ProgressDialog progressDialog;

    private Register register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);
        ButterKnife.bind(this);

        register = (Register) getIntent().getSerializableExtra("register");
        firstName.setText(register.getFirstName() + " " + register.getLastName());
        String status;
        if (register.getStatus().equals(1)) {
            status = "Permanent";
        } else {
            status = "Unknown";
        }

        statusMember.setText(getFormattedArticleDate(register.getRegisteredAt()));
        memberCode.setText(User.getFormattedMemberCode(register.getMemberCode().toString()));
        mEmailView.setText(register.getEmail());
        mPasswordView.setText(getIntent().getStringExtra("password_view"));

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

    }

    /**
     * formatting date Article
     * @param d , date from variable
     *          return formatedDate
    * */
    public String getFormattedArticleDate(String d) {
        String formattedDate;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd H:m:s", new Locale("en", "EN")).parse(d);

            SimpleDateFormat outGoing = new SimpleDateFormat("MM/yy", new Locale("en", "EN"));
            formattedDate = outGoing.format(date);
        } catch (ParseException e) {
            formattedDate = d;
            e.printStackTrace();
        }

        return formattedDate;
    }

    public void doActionLeft(View view) {
        super.onBackPressed();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {


        final ProgressBar progressLogin = (ProgressBar) findViewById(R.id.progress_bar);

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

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
            progressDialog = ProgressDialog.show(RegisterSuccessActivity.this, "Login Credentials", "Please wait ...", true);
            progressDialog.setCancelable(true);
            signInButton.setEnabled(false);
            progressLogin.setVisibility(View.VISIBLE);
            // Show a progress spinner, and kick off a background task to
            // perform the user register succes attempt.

            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(App.API)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UserApi service = retrofit.create(UserApi.class);

            LoginRequest request = new LoginRequest(email, password,
                    getFirebaseToken(),App.id(RegisterSuccessActivity.this));

            Call<LoginResponse> call = service.login(request);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                    if (response.raw().isSuccessful()) {
                        Toast.makeText(RegisterSuccessActivity.this, "Login Successful", Toast.LENGTH_LONG).show();

                        SessionManager session = new SessionManager(getApplicationContext());
                        User user = response.body().getUser();
                        session.setLogin(true, user);

                        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                        db.createUser(user);

                        Intent intent;
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                        finish();
                    } else {
                        if(response.code() == 401)
                            Toast.makeText(RegisterSuccessActivity.this, "Please check your email to verify your account", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(RegisterSuccessActivity.this, "Login is failed", Toast.LENGTH_LONG).show();
                    }
                    progressLogin.setVisibility(View.GONE);
                    signInButton.setEnabled(true);
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(RegisterSuccessActivity.this, "Login is failed", Toast.LENGTH_LONG).show();
                    progressLogin.setVisibility(View.GONE);
                    signInButton.setEnabled(true);
                    progressDialog.dismiss();
                }
            });

        }
    }

    /**
     * get firebase token from SharedPreferences
     * @return firebasetoken
    * */
    private String getFirebaseToken()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String firebaseToken = pref.getString("regId", null);

        Log.e("cranium", "Firebase reg id: " + firebaseToken);

        return firebaseToken;
    }

    /**
     * validator password length
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * validator if this is really password
     * @param email
     * @return
     */
    private boolean isValidEmail(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && email.contains(".");
    }

}
