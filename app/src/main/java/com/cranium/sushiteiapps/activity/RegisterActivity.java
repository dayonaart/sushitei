package com.cranium.sushiteiapps.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.adapter.LocationSpinnerAdapter;
import com.cranium.sushiteiapps.api.UserApi;
import com.cranium.sushiteiapps.model.City;
import com.cranium.sushiteiapps.model.request.RegisterRequest;
import com.cranium.sushiteiapps.model.response.EditProfileResponseError;
import com.cranium.sushiteiapps.model.response.RegisterResponse;
import com.cranium.sushiteiapps.model.response.RegisterResponseError;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.register_button) AppCompatButton registerButton;
    @BindView(R.id.first_name) AutoCompleteTextView mFirstNameView;
    @BindView(R.id.last_name) AutoCompleteTextView mLastNameView;
    @BindView(R.id.date_of_birth) AutoCompleteTextView mDobView;
    @BindView(R.id.mobile_phone) AutoCompleteTextView mMobilePhoneView;
    @BindView(R.id.email) AutoCompleteTextView mEmailView;
    @BindView(R.id.location) Spinner mLocationSpinner;
    @BindView(R.id.password) EditText mPasswordView;
    @BindView(R.id.re_enter_password) EditText mReEnterPasswordView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    ProgressDialog progressDialog;
    DatePickerDialog datePickerDialog;
    private Integer selectedCity;
    private List<City> cities;
    private DatabaseHelper db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        db = new DatabaseHelper(getApplicationContext());

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        loadLocations();

        mDobView.setFocusable(false);
        mDobView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender
                final Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                // date picker dialog
                datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String datetime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                java.util.Date date = null;
                                try {
                                    date = new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "EN")).parse(datetime);
                                    SimpleDateFormat formatted = new SimpleDateFormat("dd MMM yyyy", new Locale("en", "EN"));
                                    mDobView.setText(formatted.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

    }

    /**
    * load all location from local database
    * */
    private void loadLocations() {

        cities = db.getAllCities();
        LocationSpinnerAdapter locationSpinnerAdapter = new LocationSpinnerAdapter(this, cities);
        mLocationSpinner.setAdapter(locationSpinnerAdapter);
        mLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = cities.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("cranium", "item nothing selected");
            }
        });
    }

    @OnClick(R.id.action_login)
    protected void actionLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void doActionLeft(View view) {
        super.onBackPressed();
    }

    /**
     * empty all error field texts
     * */
    private void clearErrorFill() {
        // Reset errors.
        mFirstNameView.setError(null);
        mLastNameView.setError(null);
        mDobView.setError(null);
        mMobilePhoneView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mReEnterPasswordView.setError(null);

        mFirstNameView.requestFocus();
    }

    /**
     * empty all field texts
    * */
    private void clearFill() {

        mFirstNameView.setText(null);
        mLastNameView.setText(null);
        mDobView.setText(null);
        mMobilePhoneView.setText(null);
        mEmailView.setText(null);
        mPasswordView.setText(null);
        mReEnterPasswordView.setText(null);

        mFirstNameView.requestFocus();
    }

    /**
    * attemp registation store user data to API
    * */
    private void attemptRegister() {

        clearErrorFill();
        App.hideSoftKeyboard(this);

        // Store values at the time of the login attempt.
        String firstName = mFirstNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();
        String dob = mDobView.getText().toString();
        Date date = null;
        try {
            date = new SimpleDateFormat("dd MMM yyyy", new Locale("en", "EN")).parse(dob);
            SimpleDateFormat formatted = new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "EN"));
            dob = formatted.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String mobilePhone = mMobilePhoneView.getText().toString();
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        String reEnterPassword = mReEnterPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(reEnterPassword) && !isPasswordValid(reEnterPassword)) {
            mReEnterPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mReEnterPasswordView;
            cancel = true;
        }

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

        if (TextUtils.isEmpty(mobilePhone)) {
            mMobilePhoneView.setError(getString(R.string.error_field_required));
            focusView = mMobilePhoneView;
            cancel = true;
        }

        if (TextUtils.isEmpty(dob)) {
            mDobView.setError(getString(R.string.error_field_required));
            focusView = mDobView;
            cancel = true;
        }

        if (TextUtils.isEmpty(firstName)) {
            mFirstNameView.setError(getString(R.string.error_field_required));
            focusView = mFirstNameView;
            cancel = true;
        }

        if  (lastName.equals("")) lastName = "-";

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            progressDialog = ProgressDialog.show(this, "Registration", "Please wait ...", true);
            progressDialog.setCancelable(true);

            progressBar.setVisibility(View.VISIBLE);
            registerButton.setEnabled(false);

            // Show a progress spinner, and kick off a background task to
            // perform the user register attempt.
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

            String firebaseToken = db.getFirebase().getFirebaseToken();
            String deviceNumber = db.getFirebase().getDeviceNumber();

            RegisterRequest request = new RegisterRequest(firstName, lastName, email, dob, mobilePhone,
                    selectedCity, password, reEnterPassword, deviceNumber,firebaseToken);

            Call<RegisterResponse> call = service.register(request);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                    if (response.raw().isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        clearFill();
                        Intent intent;
                        intent = new Intent(getApplicationContext(), RegisterSuccessActivity.class);
                        intent.putExtra("register", response.body().getRegister());
                        intent.putExtra("password_view", password);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);

                    } else if(response.code()==400) {
                        try{
                            Gson gson = new GsonBuilder().create();
                            RegisterResponseError mError = gson.fromJson(response.errorBody().string(), RegisterResponseError.class);
                            Toast.makeText(getApplicationContext(), mError.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("Register :",mError.getMessage());
                        }catch (Exception e){

                        }
                        Log.e(App.LOG,response.message());

                    }
                    progressBar.setVisibility(View.INVISIBLE);

                    registerButton.setEnabled(true);
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Log.i("aldieemaulana", "aldieemaulana: " + t.toString());
                    Toast.makeText(RegisterActivity.this, "Registration is failed, Please try again.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    registerButton.setEnabled(true);
                    progressDialog.dismiss();
                }
            });

        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }

    private boolean isValidEmail(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && email.contains(".");
    }

}
