package com.cranium.sushiteiapps.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.Config;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.api.UserApi;
import com.cranium.sushiteiapps.model.User;
import com.cranium.sushiteiapps.model.request.LoginRequest;
import com.cranium.sushiteiapps.model.response.LoginResponse;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.SessionManager;
import com.sdsmdg.tastytoast.TastyToast;

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
 * Created by aldieemaulana on 5/24/17.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.action_left) ImageButton actLeft;
    @BindView(R.id.sign_in_button) AppCompatButton signInButton;
    @BindView(R.id.email) AutoCompleteTextView mEmailView;
    @BindView(R.id.password) EditText mPasswordView;

    ProgressDialog progressDialog;

    DatabaseHelper db;
    SessionManager session;

    String voucher = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DatabaseHelper(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attemptLogin();
            }
        });

        setAutoLogout();
    }

    /**
     * goto About Activity
     * @param view
     */
    @OnClick(R.id.about_sushitei)
    protected void aboutSushiteiClick(View view) {
        Intent intent = new Intent(this, AboutSushiteiActivity.class);

        startActivity(intent);
    }

    /**
     * goto Terms and Conditions Activity
     */
    @OnClick(R.id.action_register)
    protected void actionRegister() {
        Intent intent = new Intent(this, TermAndConditionActivity.class);
        startActivity(intent);
    }

    /**
     * goto Forgot Activity
     */
    @OnClick(R.id.action_need)
    protected void actionNeed() {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    /**
     * control the back events
     * @param view
     */
    public void doActionLeft(View view) {
        super.onBackPressed();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        final ProgressBar progressLogin = (ProgressBar) findViewById(R.id.login_progress);

        App.hideSoftKeyboard(LoginActivity.this);

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

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

            progressDialog = ProgressDialog.show(LoginActivity.this, "Login Credentials", "Please wait ...", true);
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(App.API)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UserApi service = retrofit.create(UserApi.class);

            LoginRequest request = new LoginRequest(email,
                    password,
                    db.getFirebase().getFirebaseToken(),
                    db.getFirebase().getDeviceNumber());

            Log.e("SURYA", ""+email+"\n"+password+"\n"+db.getFirebase().getFirebaseToken()+"\n"+db.getFirebase().getDeviceNumber());

            Call<LoginResponse> call = service.login(request);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.raw().isSuccessful()) {

                        SessionManager session = new SessionManager(getApplicationContext());
                        User user = response.body().getUser();
                        session.setLogin(true, user);
                        session.setLoginDate();
                        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                        db.createUser(user);
                        Log.e(App.LOG,"welcome "+user.getEmail()+" status "+user.getStatus()+" status desc "+user.getStatusDescription());
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                        Intent intent;
                        if (getIntent()!=null ){
                            try{
                                if (getIntent().getStringExtra("voucher").equals("voucher")){
                                    finish();
                                }else{
                                    intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }catch (NullPointerException e){
                                intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }else{
                            intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }else if (response.code() == 401) {
                        Toast.makeText(LoginActivity.this, "Your password and email is didn't match\nPlease try again.", Toast.LENGTH_LONG).show();
                    } else if (response.code() == 400) {
                        Log.e("SURYA", "" + response.errorBody().toString());
                        Toast.makeText(LoginActivity.this, "Login failed\nPlease contact the administrator. ", Toast.LENGTH_LONG).show();
                    }else{
                        Log.e("SURYA", "" + response.errorBody().toString()+" "+response.message());
                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    showDialog();
                    progressDialog.dismiss();
                }
            });

        }
    }

    /**
     * this will show dialog if forcelogout is happen
     */
    private void setAutoLogout(){
        if (getIntent()!=null ){
            try{
                if (getIntent().getStringExtra("force logout").equals("dari App") &&  session.isLoggedIn()){
                    session.isLogOut();
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Notification");
                    alertDialog.setMessage("Seem like you have logged in from another device. \nplease try again.");
                    alertDialog.setCancelable(false);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    db.deleteAlLHistoryPoint();
                    db.deleteAlLWishes();
                }
            }catch (NullPointerException e){

            }
        }
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
     * @return result email
     */
    private boolean isValidEmail(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && email.contains(".");
    }

    public void showDialog(){
        android.support.v7.app.AlertDialog.Builder mDialog = new android.support.v7.app.AlertDialog.Builder(this);
        mDialog.setMessage("Connection problem");
        mDialog.setPositiveButton("Retry",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                attemptLogin();
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
