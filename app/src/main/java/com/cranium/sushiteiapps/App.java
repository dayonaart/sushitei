package com.cranium.sushiteiapps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.cranium.sushiteiapps.activity.LoginActivity;
import com.cranium.sushiteiapps.activity.MainActivity;
import com.cranium.sushiteiapps.adapter.MessageExpandableListAdapter;
import com.cranium.sushiteiapps.api.MessageApi;
import com.cranium.sushiteiapps.api.UserApi;
import com.cranium.sushiteiapps.api.VoucherApi;
import com.cranium.sushiteiapps.model.Message;
import com.cranium.sushiteiapps.model.MessageLoad;
import com.cranium.sushiteiapps.model.Vouchers;
import com.cranium.sushiteiapps.model.request.ForceLogoutRequest;
import com.cranium.sushiteiapps.model.response.ForceLogoutResponse;
import com.cranium.sushiteiapps.model.response.Messages;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class App extends Application {
    public static String API = "XXXXXXXXXXXXXX";
    public static String URL = "XXXXXXXXXXXXXX";


    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    public static  final int TIMEOUT = 90;

    public static final String LOG = "SURYA";
    public static int MESSAGE_COUNTER = 0;
    public static int VOUCHER_COUNTER = 0;

    public static int IMAGE_COUNTER = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/latoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static String getGoogleApiKey() {
        return "AIzaSyAD5EwI8DrnqvfE33iBA2-FdGpNnW7Troo";
    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    public static String encodeTobase64(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static void hideSoftKeyboard(Activity activity) {
        try{
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
            Log.e("Something","wrong !");
        }

    }

    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }


    /**
     * force logout for autologout
     * @param context
     */
    public static void  forceLogout(final Context context)
    {
        final boolean val = true;
        final SessionManager sessionManager = new SessionManager(context);

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

        SharedPreferences pref = context.getSharedPreferences(Config.SHARED_PREF, 0);
        String firebaseToken = pref.getString("regId", null);
        Log.e("SURYA", ""+"\n"+sessionManager.getDeviceNumber()+" "+firebaseToken);

        if(sessionManager.isLoggedIn()){
            ForceLogoutRequest forceClose = new ForceLogoutRequest(sessionManager.getDeviceNumber(),firebaseToken );
            retrofit2.Call<ForceLogoutResponse> call = service.forceLogout(forceClose);
            call.enqueue(new Callback<ForceLogoutResponse>() {
                @Override
                public void onResponse(retrofit2.Call<ForceLogoutResponse> call, Response<ForceLogoutResponse> response) {
                    Log.i("SURYA", "PESAN: app "+response.raw().code()+" Messsage "+response.raw().message());
                    if (response.raw().isSuccessful()){
                        DatabaseHelper databaseHelper = new DatabaseHelper(context);
                        databaseHelper.deleteAlLMessage();
                        databaseHelper.deleteAlLVoucher();
                        Log.e("hendrigunawan", "force logout");
                        Intent intentMain = new Intent(context, MainActivity.class);
                        intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentMain);

                        Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("force logout","dari App");
                        context.startActivity(intent);
                    }else{
                        try {
                            Log.e("SURYA", "force logout "+response.errorBody().string()+ " status : "+response.raw().code() +" message : "+response.message());
                            Toast.makeText(context, response.errorBody().string()+" "+response.message()+"asidyuaiosyaisudyaisduyaiusdyaiusyaiusyiuayd", Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            // handle failure to read error
                        }
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ForceLogoutResponse> call, Throwable t) {
                    Log.i("cranium:save", "failed");
                }
            });
        }
    }

    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        Log.e(App.LOG,"jumlah badge "+count);
        context.sendBroadcast(intent);
    }

    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }
    }