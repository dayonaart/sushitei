package com.cranium.sushiteiapps.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.Config;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.adapter.LocationSpinnerAdapter;
import com.cranium.sushiteiapps.api.UserApi;
import com.cranium.sushiteiapps.model.City;
import com.cranium.sushiteiapps.model.User;
import com.cranium.sushiteiapps.model.request.EditProfileRequest;
import com.cranium.sushiteiapps.model.request.LoginRequest;
import com.cranium.sushiteiapps.model.response.EditProfileResponse;
import com.cranium.sushiteiapps.model.response.EditProfileResponseError;
import com.cranium.sushiteiapps.model.response.VerifyPasswordResponse;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
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
 * Created by hendrigunwan on 12/06/17.
 */

public class EditProfileActivity extends AppCompatActivity {

    @BindView(R.id.first_name) AutoCompleteTextView mFirstNameView;
    @BindView(R.id.last_name) AutoCompleteTextView mLastNameView;
    @BindView(R.id.date_of_birth) AutoCompleteTextView mDobView;
    @BindView(R.id.mobile_phone) AutoCompleteTextView mMobilePhoneView;
    @BindView(R.id.email) AutoCompleteTextView mEmailView;
    @BindView(R.id.location) Spinner mLocationSpinner;
    @BindView(R.id.old_password) EditText mOldPasswordView;
    @BindView(R.id.new_password) EditText mNewPasswordView;
    @BindView(R.id.save_button) AppCompatButton saveButton;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.linear_parent) LinearLayout linear;

    private SessionManager sessionManager;
    private DatePickerDialog datePickerDialog;
    private User user;
    private List<City> cities;
    private DatabaseHelper databaseHelper;
    private Integer selectedCity;

    private ProgressDialog progressDialog;

    /**
     * handling back button events
     * @param view
     */
    @OnClick(R.id.image_back)
    public void doActionLeft(View view) {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        user = databaseHelper.getUserByMemberCode(sessionManager.getMemberCode());

        mDobView.setFocusable(false);
        mDobView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender
                final Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                java.util.Date dob = null;
                try {
                    dob = new SimpleDateFormat("yyyy-MM-dd", new Locale("id", "ID")).parse(user.getDob());
                    SimpleDateFormat year = new SimpleDateFormat("yyyy", new Locale("id", "ID"));
                    SimpleDateFormat month = new SimpleDateFormat("MM", new Locale("id", "ID"));
                    SimpleDateFormat day = new SimpleDateFormat("dd", new Locale("id", "ID"));
                    mYear = Integer.parseInt(year.format(dob));
                    mMonth = Integer.parseInt(month.format(dob)) - 1;
                    mDay = Integer.parseInt(day.format(dob));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // date picker dialog
                datePickerDialog = new DatePickerDialog(EditProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String datetime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                java.util.Date date = null;
                                try {
                                    date = new SimpleDateFormat("yyyy-MM-dd", new Locale("id", "ID")).parse(datetime);
                                    SimpleDateFormat formatted = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));
                                    mDobView.setText(formatted.format(date));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });

        mFirstNameView.setText(user.getFirstName());
        mLastNameView.setText(user.getLastName());
        java.util.Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd", new Locale("id", "ID")).parse(user.getDob());
            SimpleDateFormat formatted = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));
            mDobView.setText(formatted.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mMobilePhoneView.setText(user.getPhone());
        mEmailView.setText(user.getEmail());
        loadLocations();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSave();
            }
        });

        linear.setVisibility(View.INVISIBLE);

        launchPasswordVerificationDialog();


    }

    /**
     * this will load all outlets locations
     */
    private void loadLocations() {

        cities = databaseHelper.getAllCities();

        LocationSpinnerAdapter locationSpinnerAdapter = new LocationSpinnerAdapter(this, cities);
        mLocationSpinner.setAdapter(locationSpinnerAdapter);
        mLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = cities.get(position).getId();
                Log.i("cranium", "itme" + id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("cranium", "item nothing selected");
            }
        });

        Integer position = 0;
        for (int i=0;i<cities.size();i++) {
            Integer cityId = cities.get(i).getId();
            if (cityId == Integer.parseInt(user.getCityId())) {
                position = i;
                break;
            }
        }
        mLocationSpinner.setSelection(position);

    }

    /**
     * this will save edited profile and send request to API
     */
    private void attemptSave() {
        App.hideSoftKeyboard(this);
        // set errors null
        mFirstNameView.setError(null);
        mLastNameView.setError(null);
        mDobView.setError(null);
        mMobilePhoneView.setError(null);
        mEmailView.setError(null);
        mOldPasswordView.setError(null);
        mNewPasswordView.setError(null);

        String firstName = mFirstNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();
        String dob = mDobView.getText().toString();
        java.util.Date date = null;
        try {
            date = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID")).parse(dob);
            SimpleDateFormat formatted = new SimpleDateFormat("yyyy-MM-dd", new Locale("id", "ID"));
            dob = formatted.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String mobilePhone = mMobilePhoneView.getText().toString();
        String email = mEmailView.getText().toString();
        String oldPassword = mOldPasswordView.getText().toString();
        String newPassword = mNewPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(newPassword) && !isPasswordValid(newPassword)) {
            mNewPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mNewPasswordView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(oldPassword) && TextUtils.isEmpty(newPassword)) {
            mNewPasswordView.setError(getString(R.string.error_field_required));
            focusView = mNewPasswordView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(oldPassword) && !isPasswordValid(oldPassword)) {
            mOldPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mOldPasswordView;
            cancel = true;
        }

        if (!isValidEmail(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

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

        /*if (TextUtils.isEmpty(dob)) {
            mDobView.setError(getString(R.string.error_field_required));
            focusView = mDobView;
            cancel = true;
        }*/

        if (TextUtils.isEmpty(firstName)) {
            mFirstNameView.setError(getString(R.string.error_field_required));
            focusView = mFirstNameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            progressDialog = ProgressDialog.show(this, "Editing Profile", "Please wait ...", true);

            progressBar.setVisibility(View.VISIBLE);

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(newRequest);
                }
            }).readTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(App.API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UserApi service = retrofit.create(UserApi.class);

            EditProfileRequest request  = new EditProfileRequest(firstName, lastName, dob, email, mobilePhone, selectedCity, oldPassword, newPassword);
            Call<EditProfileResponse> call = service.editProfile(request, sessionManager.getMemberCode());
            call.enqueue(new Callback<EditProfileResponse>() {
                @Override
                public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response) {
                    if (response.raw().isSuccessful()) {
                        User user = response.body().getUser();
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                        db.createUser(user);
                        mFirstNameView.requestFocus();
                        mOldPasswordView.setText(null);
                        mNewPasswordView.setText(null);

                        /*goto main activity*/
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } else if (response.code() == 400) {
                        try {
                            Gson gson = new GsonBuilder().create();
                            EditProfileResponseError mError = gson.fromJson(response.errorBody().string(), EditProfileResponseError.class);
                            Toast.makeText(getApplicationContext(), mError.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            // handle failure to read error
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Edit Profile is failed", Toast.LENGTH_LONG).show();
                    }

                    progressBar.setVisibility(View.INVISIBLE);
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Edit Profile is failed, please try again", Toast.LENGTH_LONG).show();

                    progressBar.setVisibility(View.INVISIBLE);
                    progressDialog.dismiss();
                }
            });

        }
    }

    /**
     * this will show progress dialog after editing profile
     */
    public void launchDialog() {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Edit Profile", "Please wait ...", true);
        progressDialog.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Here you should write your time consuming task...
                    // Let the progress ring for 10 seconds...
                    Thread.sleep(2000);
                } catch (Exception e) {

                }
                progressDialog.dismiss();
            }
        }).start();
    }

    /**
     * password Validation
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

    /**
     * Email validator
     * @param email
     * @return
     */
    private boolean isValidEmail(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && email.contains(".");
    }

    /**
     * this will launch password verification for security reasons
     */
    private void launchPasswordVerificationDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.verify_password_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final AutoCompleteTextView emailView = (AutoCompleteTextView) dialog.findViewById(R.id.email);
        final EditText passwordView = (EditText) dialog.findViewById(R.id.password);
        AppCompatButton nextButton = (AppCompatButton) dialog.findViewById(R.id.next_button);
        AppCompatButton cancelButton = (AppCompatButton) dialog.findViewById(R.id.action_cancel);

        emailView.setText(user.getEmail());
        passwordView.requestFocus();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(passwordView.getText().toString())) {
                    passwordView.setError(getString(R.string.error_field_required));
                    passwordView.requestFocus();
                    return;
                }
                loadVerifyPassword(emailView.getText().toString(), passwordView.getText().toString());
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                finish();
            }
        });

        dialog.show();
    }

    /**
     * this method will request to API for password verfication for security reason
     * @param email s
     * @param password
     */
    private void loadVerifyPassword(String email, String password) {
        progressDialog = ProgressDialog.show(EditProfileActivity.this, "Verify Password", "Please wait ...", true);
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

        UserApi service = retrofit.create(UserApi.class);

        LoginRequest loginRequest = new LoginRequest(email, password, getFirebaseToken(),App.id(EditProfileActivity.this));

        Call<VerifyPasswordResponse> call = service.verifyPassword(loginRequest);
        call.enqueue(new Callback<VerifyPasswordResponse>() {
            @Override
            public void onResponse(Call<VerifyPasswordResponse> call, Response<VerifyPasswordResponse> response) {

                if (response.raw().isSuccessful()) {
                    progressDialog.dismiss();
                    linear.setVisibility(View.VISIBLE);
                    Toast.makeText(EditProfileActivity.this, "Verify password is success", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Verify password is fail", Toast.LENGTH_SHORT).show();

                    Intent intent;
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<VerifyPasswordResponse> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Verify password is fail, please try again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Intent intent;
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * this will return firebase token from shared preferences
     * @return firebasetoken = firebasetoken from FCM
     */
    private String getFirebaseToken(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String firebaseToken = pref.getString("regId", null);

        Log.e("cranium", "Firebase reg id: " + firebaseToken);

        return firebaseToken;
    }

}
