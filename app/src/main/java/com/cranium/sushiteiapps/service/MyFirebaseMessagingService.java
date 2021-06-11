package com.cranium.sushiteiapps.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.Config;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.activity.MainActivity;
import com.cranium.sushiteiapps.activity.MessageActivity;
import com.cranium.sushiteiapps.activity.MessageDetailActivity;
import com.cranium.sushiteiapps.activity.VoucherActivity;
import com.cranium.sushiteiapps.activity.VoucherDetailActivity;
import com.cranium.sushiteiapps.activity.VoucherMessageActivity;
import com.cranium.sushiteiapps.adapter.MessageExpandableListAdapter;
import com.cranium.sushiteiapps.adapter.VoucherExpandableListAdapter;
import com.cranium.sushiteiapps.api.MessageApi;
import com.cranium.sushiteiapps.api.VoucherApi;
import com.cranium.sushiteiapps.model.Message;
import com.cranium.sushiteiapps.model.MessageLoad;
import com.cranium.sushiteiapps.model.Vouchers;
import com.cranium.sushiteiapps.model.response.Messages;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.Notification;
import com.cranium.sushiteiapps.util.SessionManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.leolin.shortcutbadger.ShortcutBadger;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cranium-01 on 03/07/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private Notification notificationUtils;
    SessionManager session;
    DatabaseHelper db;
    Message message;
    Intent intents;
    int id = 0 ,isVoucher, cancel,type;
    String name,description,image,enddate,startdate,createdAt,body,title;

    int voucherCount = 0;
    int messageCount = 0;

    @Override
    public void handleIntent(Intent intent) {
        Log.e(App.LOG,"HANDLE INTENT");

        session = new SessionManager(getApplicationContext());

        if (intent.getExtras() != null) {
            for (String key : intent.getExtras().keySet()) {
                Object value = intent.getExtras().get(key);
                Log.e(App.LOG, "on Handle intent Key: " + key + " Value: " + value);

                if (key.equals("gcm.notification.title")) title = value.toString();
                if (key.equals("gcm.notification.body")) body = value.toString();
                if (key.equals("name")) name = value.toString();
                if (key.equals("id")) id = Integer.valueOf(value.toString());
                if (key.equals("name")) name = value.toString();
                if (key.equals("description")) description = value.toString();
                if (key.equals("image")) image = value.toString();
                if (key.equals("start_date")) startdate = value.toString();
                if (key.equals("end_date")) enddate = value.toString();
                if (key.equals("created_at")) createdAt = value.toString();
                if (key.equals("type")) type = Integer.valueOf(value.toString());
                if (key.equals("is_voucher")) isVoucher = Integer.valueOf(value.toString());
            }

            message = new Message(id,name,description,image,startdate,createdAt,isVoucher);

            Log.e(TAG, "I'm in!!! id "+id+" name "+name+" desc "+description);
            Log.e(App.LOG,"is voucher "+isVoucher);

            if (message.getIsVoucher() == 1 ){ // message
                session.setNewMessage(true);
                session.setCountMessage(session.getCountMessage()+1);
                intents= new Intent(getApplicationContext(), MessageDetailActivity.class);
                intents.putExtra("fromNotif",true);
                loadMessage();
                Intent intent1 = new Intent(Config.PUSH_NOTIFICATION);
                intent1.putExtra("isVoucher", isVoucher);
                intent1.setAction(Config.PUSH_NOTIFICATION);
                sendBroadcast(intent1);
            }else if(message.getIsVoucher() == 2 ){  // voucher
                session.setNewVoucher(true);

                session.setCountVoucher(session.getCountVoucher()+1);
                intents = new Intent(getApplicationContext(), VoucherDetailActivity.class);
                intents.putExtra("fromNotif",true);
                loadVoucher();
                Intent intent1 = new Intent(Config.PUSH_NOTIFICATION);
                intent1.putExtra("isVoucher", isVoucher);
                intent1.setAction(Config.PUSH_NOTIFICATION);
                sendBroadcast(intent1);
            }

            if (isVoucher==0){
                Log.e(App.LOG,"is voucher masuukkk "+isVoucher);
                name = "Sushitei";
                intents = new Intent(getApplicationContext(), MainActivity.class);
                new Notification(getApplicationContext()).showNotificationMessage(name,body,new Date().toString(), intents,null);
            }
            Log.e(App.LOG,"voucher "+session.getCountVoucher()+" message "+session.getCountMessage());

            if (session.getCountMessage() > 0 && session.getCountVoucher() == 0){
                intents.putExtra("message",message);
                intents.putExtra("fromNotif",true);
                if (session.getCountMessage() == 1){
                    showNotificationMessageWithBigImage(getApplicationContext(),name,description,new Date().toString(),intents,App.URL + "files/messages/"+image);
                }else if(session.getCountMessage()>1) {
                    Notification.clearNotifications(getApplicationContext());
                    name = "Sushitei";
                    description = "You have "+session.getCountMessage()+" new messages";
                    intents= new Intent(getApplicationContext(), MessageActivity.class);
                    intents.putExtra("fromNotif",true);
                    new Notification(getApplicationContext()).showNotificationMessage(name,description,new Date().toString(), intents,null);
                }
            }
            if (session.getCountVoucher() > 0 && session.getCountMessage() == 0){
                intents.putExtra("message",message);
                if (session.getCountVoucher() == 1){
                    Log.d(TAG, "I'm in!!! id "+title+" name "+body+" desc "+description);
                    if (type == 2){
                        showNotificationMessageWithBigImage(getApplicationContext(),title,body,new Date().toString(),intents,App.URL + "files/messages/"+image);
                    }else{
                        showNotificationMessageWithBigImage(getApplicationContext(),name,description,new Date().toString(),intents,App.URL + "files/messages/"+image);
                    }
                }else if (session.getCountVoucher()>1){
                    Notification.clearNotifications(getApplicationContext());
                    name = "Sushitei";
                    description = "You have "+session.getCountVoucher()+" new vouchers";
                    intents= new Intent(getApplicationContext(), VoucherActivity.class);
                    intents.putExtra("fromNotif",true);
                    new Notification(getApplicationContext()).showNotificationMessage(name,description,new Date().toString(), intents,null);
                }
            }

            if (session.getCountVoucher() > 0 && session.getCountMessage() > 0){
                intents.putExtra("message",message);
                intents.putExtra("fromNotif",true);
                name = "Sushitei";
                description = "You have "+session.getCountMessage()+" new messages and "+session.getCountVoucher()+" new vouchers ";
                intents= new Intent(getApplicationContext(), VoucherMessageActivity.class);
                new Notification(getApplicationContext()).showNotificationMessage(name,description,new Date().toString(), intents,null);
            }
            ShortcutBadger.removeCount(getApplicationContext());
            ShortcutBadger.applyCount(getApplicationContext(), (session.getCountVoucher()+session.getCountMessage()));
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(App.LOG, "On Message Received get from : " + remoteMessage.getFrom()+" get data "+remoteMessage.getData().get("name"));

        String title = remoteMessage.getNotification().getTitle();
        String message1 = remoteMessage.getNotification().getBody();
        String clickAction = remoteMessage.getNotification().getClickAction();

        session = new SessionManager(getApplicationContext());

        Map<String, String> params = remoteMessage.getData();
        JSONObject object = new JSONObject(params);
        handleDataMessage(object);

        handleNotification(message1);

        db = new DatabaseHelper(getApplicationContext());

        if (!Notification.isAppIsInBackground(getApplicationContext())) {
            Log.e(App.LOG,"this from onreceived not in background size "+remoteMessage.getData().size());
        }else{
            Log.e(App.LOG,"this from onreceived in background size"+remoteMessage.getData().size());
        }
        if (remoteMessage.getData().size() > 0) {
                Integer id = Integer.parseInt(remoteMessage.getData().get("id"));
                String name = remoteMessage.getData().get("name");
                String description = remoteMessage.getData().get("description");
                String image = remoteMessage.getData().get("image");
                String latitude = remoteMessage.getData().get("latitude");
                String longitude = remoteMessage.getData().get("longitude");
                String startDate = remoteMessage.getData().get("start_date");
                String endDate = remoteMessage.getData().get("end_date");
                String createdAt = remoteMessage.getData().get("created_at");
                String updatedAt = remoteMessage.getData().get("updated_at");
                Integer isVoucher = Integer.valueOf(remoteMessage.getData().get("is_voucher"));
                String readAt;
                if (remoteMessage.getData().get("read_at") == null) {
                    readAt = null;
                } else {
                    readAt = remoteMessage.getData().get("read_at");
                }

                message = new Message(id, name, description, image, latitude, longitude, startDate, endDate, createdAt, updatedAt, isVoucher);

                Log.e(App.LOG, "clicked" + remoteMessage.getData().toString());

                Intent intent = null;

                if (!Notification.isAppIsInBackground(getApplicationContext())){
                    if (isVoucher == 0 ){ // message
                        session.setNewMessage(true);
                        intent = new Intent(getApplicationContext(), MessageDetailActivity.class);
                    }else{  // voucher
                        session.setNewVoucher(true);
                        intent = new Intent(getApplicationContext(), VoucherDetailActivity.class);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                }else{
                    if (isVoucher == 0 ){ // message
                        session.setNewMessage(true);
                        intent = new Intent(getApplicationContext(), MessageDetailActivity.class);
                    }else{  // voucher
                        session.setNewVoucher(true);
                        intent = new Intent(getApplicationContext(), VoucherDetailActivity.class);
                    }
                }

                Log.e(App.LOG,"MESSAGE TESTING IS VOUCHER "+isVoucher);

                intent.putExtra("message", message);

                new Notification(getApplicationContext()).showNotificationMessage(name,description,new Date().toString(), intent,null);
            /*
             * for handling new messages to update main activity view
             *
             * */

        } else {
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            new Notification(getApplicationContext()).showNotificationMessage(name,description,new Date().toString(), resultIntent,null);
        }
    }

    private void handleNotification(String message) {
        if (!Notification.isAppIsInBackground(getApplicationContext())) {

            Log.e(App.LOG,"not background");
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            new Notification(getApplicationContext()).showNotificationMessage("sushitei",message,new Date().toString(), resultIntent,null);

            // play notification sound
            Notification notificationUtils = new Notification(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            Log.e(App.LOG,"in background");
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            new Notification(getApplicationContext()).showNotificationMessage("sushitei",message,new Date().toString(), resultIntent,null);
            // If the app is in background, firebase itself handles the notification
        }
    }



    private void handleDataMessage(JSONObject json) {
        Log.e(App.LOG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            if (!Notification.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                Log.i("cranium", "ok1");
                // play notification sound
                Notification notificationUtils = new Notification(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);
                Log.i("cranium", "ok2");

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new Notification(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and big image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new Notification(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    /**
     *
    */
    private void loadMessage(){
        OkHttpClient client = null;
        db = new DatabaseHelper(getApplicationContext());

        if (session.isLoggedIn()){
            client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + session.getToken())
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(newRequest);
                }
            }).readTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(App.TIMEOUT, TimeUnit.SECONDS).build();
        }else{
            client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(newRequest);
                }
            }).readTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(App.TIMEOUT, TimeUnit.SECONDS).build();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(App.API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MessageApi service = retrofit.create(MessageApi.class);

        Call<Messages> call = service.messages(new MessageLoad(session.getLoginUpdate(), session.getDeviceNumber()));

        call.enqueue(new Callback<Messages>() {
            @Override
            public void onResponse(Call<Messages> call, Response<Messages> response) {

                if (response.raw().isSuccessful()) {

                    db.deleteAlLMessage();
                    db.createMessage(response.body());

                }else{
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            loadMessage();
                        }
                    }, 5000);

                    Log.e("SURYA", "else message : "+response.raw().message() + " "+response.code());
                }
                Log.e("Surya", " message luar  "+response.raw().message() + " "+response.code());
            }

            @Override
            public void onFailure(Call<Messages> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Please check network connection.", Toast.LENGTH_SHORT).show();
                Log.i("SURYA", t.getMessage() + "");
            }
        });
    }


    /**
    *   load vouchers and save it to db
     **/
    private void loadVoucher(){
        OkHttpClient client = null;
        db = new DatabaseHelper(getApplicationContext());
        if (session.isLoggedIn()){
            client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + session.getToken())
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(newRequest);
                }
            }).readTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(App.TIMEOUT, TimeUnit.SECONDS).build();
        }else{
            client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(newRequest);
                }
            }).readTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(App.TIMEOUT, TimeUnit.SECONDS).build();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(App.API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        VoucherApi service = retrofit.create(VoucherApi.class);
        Log.e(App.LOG,   "before load api");
        Call<Messages> call = service.voucherPost(new Vouchers(session.getDeviceNumber(), new Date().toString()));

        call.enqueue(new Callback<Messages>() {
            @Override
            public void onResponse(Call<Messages> call, Response<Messages> response) {
                if (response.raw().isSuccessful()) {
                    db.deleteAlLVoucher();
                    db.createVoucher(response.body());
                }else{
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            loadVoucher();
                        }
                    }, 5000);
                    Log.e("SURYA", response.raw().message() + " ");
                }
                Log.e("Surya", " message luar  "+response.raw().message() + " "+response.code());
            }

            @Override
            public void onFailure(Call<Messages> call, Throwable t) {

                Log.e("SURYA", "hasdhahsdhasdh "+t.getMessage());
            }
        });
    }

}
