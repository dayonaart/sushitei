package com.cranium.sushiteiapps.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.Config;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.api.UserApi;
import com.cranium.sushiteiapps.model.User;
import com.cranium.sushiteiapps.model.request.LoginRequest;
import com.cranium.sushiteiapps.model.response.VerifyPasswordResponse;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.SessionManager;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

public class MyCardActivity extends AppCompatActivity {

    @BindView(R.id.qrcode) ImageView qrCode;
    @BindView(R.id.first_name) TextView firstName;
    @BindView(R.id.status_member) TextView statusMember;
    @BindView(R.id.member_code) TextView memberCode;
    @BindView(R.id.parent_linear) LinearLayout parentLinear;

    private SessionManager sessionManager;
    private User user;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);

        //prevent screenshot
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        user = db.getUserByMemberCode(sessionManager.getMemberCode());

        firstName.setText(user.getFirstName() + " " + user.getLastName());

        statusMember.setText(getFormattedArticleDate(user.getRegisteredAt()));

        memberCode.setText(user.getFormattedMemberCode());

        String image = user.getQrcode();
        image = image.replace("data:image/png;base64,", "");
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        qrCode.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 200, 200, false));

//        parentLinear.setVisibility(View.INVISIBLE);

//        launchPasswordVerificationDialog();

    }

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
     * @param email is string email input data
     * @param password is string password input data
     */
    private void loadVerifyPassword(String email, String password) {
        progressDialog = ProgressDialog.show(MyCardActivity.this, "Verify Password", "Please wait ...", true);
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

        LoginRequest loginRequest = new LoginRequest(email, password, getFirebaseToken(),App.id(MyCardActivity.this));

        Call<VerifyPasswordResponse> call = service.verifyPassword(loginRequest);
        call.enqueue(new Callback<VerifyPasswordResponse>() {
            @Override
            public void onResponse(Call<VerifyPasswordResponse> call, Response<VerifyPasswordResponse> response) {

                if (response.raw().isSuccessful()) {
                    progressDialog.dismiss();
                    parentLinear.setVisibility(View.VISIBLE);
                    Toast.makeText(MyCardActivity.this, "Verify password is success", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MyCardActivity.this, "Verify password is fail", Toast.LENGTH_SHORT).show();

                    Intent intent;
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<VerifyPasswordResponse> call, Throwable t) {
                Toast.makeText(MyCardActivity.this, "Verify password is fail, please try again", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.image_back)
    public void doActionLeft(View view) {

        Intent intent;
        intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

        Intent intent;
        intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        finish();
    }
}
