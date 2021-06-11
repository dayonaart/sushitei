package com.cranium.sushiteiapps.service;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.activity.MessageDetailActivity;
import com.cranium.sushiteiapps.activity.VoucherDetailActivity;
import com.cranium.sushiteiapps.model.Message;
import com.cranium.sushiteiapps.util.Notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FirebaseDataReceiver extends WakefulBroadcastReceiver {

    private final String TAG = App.LOG;
    int id = 0 ,isVoucher, cancel;
    String name,description,image,startdate,createdAt;

    public void onReceive(Context context, Intent intent) {
//        Log.e(TAG,"on receive "+context.getPackageName()+" "+intent.getPackage());
//        Log.d(TAG, "I'm in!!!");
//        List<Message> messageList = new ArrayList<>();
//
//        if (Notification.isAppIsInBackground(context)) {
//            if (intent.getExtras() != null) {
//                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.cancelAll();
//
//                for (String key : intent.getExtras().keySet()) {
//                    Object value = intent.getExtras().get(key);
////                    Log.e(App.LOG, "Key: " + key + " Value: " + value);
//
//                    if (key.equals("name")) name = value.toString();
//                    if (key.equals("id")) id = Integer.valueOf(value.toString());
//                    if (key.equals("name")) name = value.toString();
//                    if (key.equals("description")) description = value.toString();
//                    if (key.equals("image")) image = value.toString();
//                    if (key.equals("start_date")) startdate = value.toString();
//                    if (key.equals("created_at")) createdAt = value.toString();
//                    if (key.equals("is_voucher")) isVoucher = Integer.valueOf(value.toString());
//                }
//
//                messageList.add(new Message(id,name,description,image,startdate,createdAt,isVoucher));
//
//                Log.d(TAG, "I'm in!!! id "+id+" name "+name+" desc "+description);
//
//                Intent intents;
//                for (Message mes : messageList){
//                    if (mes.getIsVoucher() == 0 ){ // message
//                        intents= new Intent(context, MessageDetailActivity.class);
//                    }else{  // voucher
//                        intents = new Intent(context, VoucherDetailActivity.class);
//                    }
//                    intents.putExtra("message", mes);
//
////                    new Notification(context).showNotificationMessage(name,description,new Date().toString(), intents,null);
//                }
//            }
//        }

    }
}
