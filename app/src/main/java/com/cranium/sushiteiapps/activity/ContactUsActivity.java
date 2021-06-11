package com.cranium.sushiteiapps.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.api.ContactApi;
import com.cranium.sushiteiapps.model.User;
import com.cranium.sushiteiapps.model.request.ContactRequest;
import com.cranium.sushiteiapps.model.response.ContactResponse;
import com.cranium.sushiteiapps.model.response.ContactResponseError;
import com.cranium.sushiteiapps.service.GMailSender;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
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
 * Created by Dayona on 6/5/17.
 */

public class ContactUsActivity extends AppCompatActivity {

    @BindView(R.id.parent_view) ScrollView parentView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.image_back) ImageButton actLeft;
    @BindView(R.id.submit_button) AppCompatButton submitButton;
    @BindView(R.id.first_name) AutoCompleteTextView mFirstNameView;
    @BindView(R.id.last_name) AutoCompleteTextView mLastNameView;
    @BindView(R.id.email) AutoCompleteTextView mEmailView;
    @BindView(R.id.phone) AutoCompleteTextView mPhoneView;
    @BindView(R.id.about) Spinner mAboutSpinner;
    @BindView(R.id.message) AutoCompleteTextView mMessageView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private DatabaseHelper databaseHelper;
    private User user;


    @OnClick(R.id.image_back)
    public void doActionLeft(View view) {
        App.hideSoftKeyboard(this);
        super.onBackPressed();
    }

    String[] aboutItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);
        databaseHelper = new DatabaseHelper(this);

        if (sessionManager.isLoggedIn()) {
            user = databaseHelper.getUserByMemberCode(sessionManager.getMemberCode());
            mFirstNameView.setText(user.getFirstName());
            mLastNameView.setText(user.getLastName());
            mEmailView.setText(user.getEmail());
            mPhoneView.setText(user.getPhone());
            mMessageView.requestFocus();
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.hideSoftKeyboard(ContactUsActivity.this);
                attemptSubmit();
            }
        });

        aboutItems = getResources().getStringArray(R.array.about_arrays);

        parentView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    App.hideSoftKeyboard(ContactUsActivity.this);
                }
            }
        });

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,aboutItems
        );
        //spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        mAboutSpinner.setAdapter(spinnerArrayAdapter);
    }

    private void setDialogBox() {
        final Dialog dialog = new Dialog(ContactUsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmation_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView description = (TextView) dialog.findViewById(R.id.description);

        title.setText("");
        title.setHeight(0);
        description.setText("Thank you for your enquiry. We will contact you as soon as possible.");

        ImageButton close = (ImageButton) dialog.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void attemptSubmit() {

        // reset errors
        mFirstNameView.setError(null);
        mLastNameView.setError(null);
        mEmailView.setError(null);
        mPhoneView.setError(null);
        mMessageView.setError(null);

        String firstName = mFirstNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();
        String email = mEmailView.getText().toString();
        final String phone = mPhoneView.getText().toString();
        String about = mAboutSpinner.getSelectedItem().toString();
        String message = mMessageView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(phone)) {
            mPhoneView.setError(getString(R.string.error_field_required));
            focusView = mPhoneView;
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

        if (TextUtils.isEmpty(firstName)) {
            mFirstNameView.setError(getString(R.string.error_field_required));
            focusView = mFirstNameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

//            progressBar.setVisibility(View.VISIBLE);

            progressDialog = ProgressDialog.show(ContactUsActivity.this, "Submit", "Please wait ...", true);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(App.API)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ContactApi service = retrofit.create(ContactApi.class);

            final ContactRequest request = new ContactRequest(firstName, lastName, email, phone, about, message);

            Call<ContactResponse> call = service.sendInquiries(request);

            call.enqueue(new Callback<ContactResponse>() {
                @Override
                public void onResponse(Call<ContactResponse> call, Response<ContactResponse> response) {

                    if (response.raw().isSuccessful()) {
                        ContactResponse contact = response.body();
                        Toast.makeText(ContactUsActivity.this, contact.getMessage(), Toast.LENGTH_LONG).show();

                        mFirstNameView.setText(null);
                        mLastNameView.setText(null);
                        mEmailView.setText(null);
                        mPhoneView.setText(null);
                        mMessageView.setText(null);


                        if (sessionManager.isLoggedIn()) {
                            user = databaseHelper.getUserByMemberCode(sessionManager.getMemberCode());
                            mFirstNameView.setText(user.getFirstName());
                            mLastNameView.setText(user.getLastName());
                            mEmailView.setText(user.getEmail());
                            mPhoneView.setText(user.getPhone());
                            mMessageView.requestFocus();
                        }
                        progressDialog.dismiss();
                        setDialogBox();


                    } else if (response.code() == 400) {
                        Gson gson = new GsonBuilder().create();
                        ContactResponseError mError =new ContactResponseError();
                        try {
                            mError = gson.fromJson(response.errorBody().string(),ContactResponseError.class);
                            Toast.makeText(ContactUsActivity.this, mError.getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        } catch (IOException e) {
                            // handle failure to read error
                        }
                    }
                    else {
                        Toast.makeText(ContactUsActivity.this, "send contact us is failed", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<ContactResponse> call, Throwable t) {
                    Toast.makeText(ContactUsActivity.this, "unable to send contact us, please try again", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

    }

    private boolean isValidEmail(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && email.contains(".");
    }

    private void sendEmail(final ContactRequest req){
        user = databaseHelper.getUserByMemberCode(sessionManager.getMemberCode());
//        final ProgressDialog dialog = new ProgressDialog(getBaseContext());
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    GMailSender sender = new GMailSender("craniumapps@gmail.com", "Ak@Tsuk1");
                    sender.sendMail("Message from "+user.getFirstName()+" ("+user.getEmail()+")",
                            " "+req.getMessage()+"\nnama : "+req.getFirstName()+" "+req.getLastName()+
                                    "\nphone : "+req.getPhone()+
                                    "\nabout : "+req.getAbout(),
                            "craniumapps@gmail.com",
                            "cs_jkt@sushitei.co.id");
                    Log.e("mylog", "Sukses ! " );
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }

}
