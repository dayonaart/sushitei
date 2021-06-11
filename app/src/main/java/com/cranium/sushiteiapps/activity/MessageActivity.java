package com.cranium.sushiteiapps.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.Config;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.adapter.MessageExpandableListAdapter;
import com.cranium.sushiteiapps.api.MessageApi;
import com.cranium.sushiteiapps.model.Message;
import com.cranium.sushiteiapps.model.MessageLoad;
import com.cranium.sushiteiapps.model.response.Messages;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.Notification;
import com.cranium.sushiteiapps.util.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
 * Created by Dayona on 6/16/17.
 */

public class MessageActivity extends AppCompatActivity {

    @BindView(R.id.message_list_view) ExpandableListView messageListView;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.nothing_message) TextView tvNothing;
    @BindColor(R.color.colorAccent) int accent;

    private SessionManager sessionManager;
    private List<Message> messageList;
    private DatabaseHelper db;
    private MessageExpandableListAdapter messageExpandableListAdapter;
    private boolean fromNotif = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        ShortcutBadger.removeCount(getApplicationContext());
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.setCountMessage(0);
        db = new  DatabaseHelper(getApplicationContext());
        fromNotif = getIntent().getBooleanExtra("fromNotif",false);
        Log.e(App.LOG,"from notif bro "+fromNotif);
        loadMessages();

        messageListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);

                return false;
            }
        });
        messageListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                MessageExpandableListAdapter listAdapter = (MessageExpandableListAdapter) messageListView.getExpandableListAdapter();
                View groupItem = listAdapter.getGroupView(groupPosition, true, null, messageListView);
                RelativeLayout layout = (RelativeLayout) groupItem.findViewById(R.id.group_view);
                layout.setVisibility(View.GONE);
                Log.i("cranium", groupPosition + "");
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMessages();
            }
        });
    }

    /**
     * this methods is for load and handling message from API based on login status
    * */
    private void loadMessages() {
        sessionManager.setNewMessage(false);

        swipeContainer.setRefreshing(true);

        messageList = db.getAllMessages().getMessages();
        try{
            Log.e(App.LOG,"get All failure message "+messageList.size());
            for(Message message : messageList){
                Log.e(App.LOG,"get one failure message "+message.getShortName());
            }

            HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
            for (Message message : messageList) {
                Log.e(App.LOG,   ""+message.getName()+" login"+" is voucher :"+message.getIsVoucher() +" size "+messageList.size()+" Read at "+message.getReadAt());
                List<String> description = new ArrayList<String>();
                description.add(message.getName());
                listDataChild.put(String.valueOf(message.getId()), description); // Header, Child data
            }
            Log.e(App.LOG,"messages size "+messageList.size());
            if(messageList.size()==0){
                tvNothing.setVisibility(View.VISIBLE);
            }else {
                tvNothing.setVisibility(View.GONE);
            }
            messageExpandableListAdapter = new MessageExpandableListAdapter(getApplicationContext(), messageList, listDataChild);
            messageListView.setAdapter(messageExpandableListAdapter);
            setListViewHeightBasedOnChildren(messageListView);

        }catch (Exception e){
//            tvNothing.setVisibility(View.VISIBLE);
        }

        if (sessionManager.isLoggedIn()){

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
                    .connectTimeout(App.TIMEOUT, TimeUnit.SECONDS).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(App.API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MessageApi service = retrofit.create(MessageApi.class);

            Call<Messages> call = service.messages(new MessageLoad(sessionManager.getLoginUpdate(), sessionManager.getDeviceNumber()));

            call.enqueue(new Callback<Messages>() {
                @Override
                public void onResponse(Call<Messages> call, Response<Messages> response) {

                    if (response.raw().isSuccessful()) {
                        messageList = response.body().getMessages();
                        Log.e(App.LOG,"messages success broooo "+messageList.size());
                        swipeContainer.setRefreshing(false);
                        db.deleteAlLMessage();
                        db.createMessage(response.body());
                        HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
                        for (Message message : messageList) {
                            Log.e(App.LOG,   ""+message.getName()+" login"+" is voucher :"+message.getIsVoucher() +" size "+messageList.size()+" Read at "+message.getReadAt());
                            List<String> description = new ArrayList<String>();
                            description.add(message.getName());
                            listDataChild.put(String.valueOf(message.getId()), description); // Header, Child data
                        }
                        Log.e(App.LOG,"messages size "+messageList.size());
                        if(messageList.size()==0){
                            tvNothing.setVisibility(View.VISIBLE);
                        }else{
                            tvNothing.setVisibility(View.GONE);
                        }
                        messageExpandableListAdapter = new MessageExpandableListAdapter(getApplicationContext(), messageList, listDataChild);
                        messageListView.setAdapter(messageExpandableListAdapter);
                        setListViewHeightBasedOnChildren(messageListView);
                    }
                    else if (response.code()==404){
                        tvNothing.setVisibility(View.VISIBLE);
                        tvNothing.setText("There is no message");
                        swipeContainer.setRefreshing(false);
                        Log.i("SURYA", response.raw().message() + "");
                    }else{

                        messageList = db.getAllMessages().getMessages();

                        HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
                        for (Message message : messageList) {
                            Log.e(App.LOG,   ""+message.getName()+" login"+" is voucher :"+message.getIsVoucher() +" size "+messageList.size()+" Read at "+message.getReadAt());
                            List<String> description = new ArrayList<String>();
                            description.add(message.getName());
                            listDataChild.put(String.valueOf(message.getId()), description); // Header, Child data
                        }
                        Log.e(App.LOG,"messages size "+messageList.size());
                        if(messageList.size()==0){
                            tvNothing.setVisibility(View.VISIBLE);
                        }
                        messageExpandableListAdapter = new MessageExpandableListAdapter(getApplicationContext(), messageList, listDataChild);
                        messageListView.setAdapter(messageExpandableListAdapter);
                        setListViewHeightBasedOnChildren(messageListView);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                loadMessages();
                            }
                        }, 5000);

                        Log.e("SURYA", "else message : "+response.raw().message() + " "+response.code());
                    }
                    Log.e("Surya", " message luar  "+response.raw().message() + " "+response.code());

                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<Messages> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), "Please check network connection.", Toast.LENGTH_SHORT).show();

                    messageList = db.getAllMessages().getMessages();
                    try{
                        Log.e(App.LOG,"get All failure message "+messageList.size());
                        for(Message message : messageList){
                            Log.e(App.LOG,"get one failure message "+message.getShortName());
                        }

                        HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
                        for (Message message : messageList) {
                            Log.e(App.LOG,   ""+message.getName()+" login"+" is voucher :"+message.getIsVoucher() +" size "+messageList.size()+" Read at "+message.getReadAt());
                            List<String> description = new ArrayList<String>();
                            description.add(message.getName());
                            listDataChild.put(String.valueOf(message.getId()), description); // Header, Child data
                        }
                        Log.e(App.LOG,"messages size "+messageList.size());
                        if(messageList.size()==0){
                            tvNothing.setVisibility(View.VISIBLE);
                        }else {
                            tvNothing.setVisibility(View.GONE);
                        }
                        messageExpandableListAdapter = new MessageExpandableListAdapter(getApplicationContext(), messageList, listDataChild);
                        messageListView.setAdapter(messageExpandableListAdapter);
                        setListViewHeightBasedOnChildren(messageListView);
                        Log.i("SURYA", t.getMessage() + "");

                    }catch (Exception e){
                        tvNothing.setVisibility(View.VISIBLE);
                    }

                }
            });

        }else{

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(newRequest);
                }
            }).readTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(App.TIMEOUT, TimeUnit.SECONDS).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(App.API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MessageApi service = retrofit.create(MessageApi.class);

            Call<Messages> call = service.messages(new MessageLoad(sessionManager.getLoginUpdate(), sessionManager.getDeviceNumber()));

            call.enqueue(new Callback<Messages>() {
                @Override
                public void onResponse(Call<Messages> call, Response<Messages> response) {

                    if (response.raw().isSuccessful()) {
                        messageList = response.body().getMessages();
                        Log.e(App.LOG,"messages success broooo "+messageList.size());
                        swipeContainer.setRefreshing(false);
                        db.deleteAlLMessage();
                        db.createMessage(response.body());
                        HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
                        for (Message message : messageList) {
                            Log.e(App.LOG,   ""+message.getName()+" login"+" is voucher :"+message.getIsVoucher() +" size "+messageList.size()+" Read at "+message.getReadAt());
                            List<String> description = new ArrayList<String>();
                            description.add(message.getName());
                            listDataChild.put(String.valueOf(message.getId()), description); // Header, Child data
                        }
                        Log.e(App.LOG,"messages size "+messageList.size());
                        if(messageList.size()==0){
                            tvNothing.setVisibility(View.VISIBLE);
                        }else{
                            tvNothing.setVisibility(View.GONE);
                        }
                        messageExpandableListAdapter = new MessageExpandableListAdapter(getApplicationContext(), messageList, listDataChild);
                        messageListView.setAdapter(messageExpandableListAdapter);
                        setListViewHeightBasedOnChildren(messageListView);
                    }
                    else if (response.code()==404){
                        tvNothing.setVisibility(View.VISIBLE);
                        tvNothing.setText("There is no message");
                        swipeContainer.setRefreshing(false);
                        Log.i("SURYA", response.raw().message() + "");
                    }else{

                        messageList = db.getAllMessages().getMessages();

                        HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
                        for (Message message : messageList) {
                            Log.e(App.LOG,   ""+message.getName()+" login"+" is voucher :"+message.getIsVoucher() +" size "+messageList.size()+" Read at "+message.getReadAt());
                            List<String> description = new ArrayList<String>();
                            description.add(message.getName());
                            listDataChild.put(String.valueOf(message.getId()), description); // Header, Child data
                        }
                        Log.e(App.LOG,"messages size "+messageList.size());
                        if(messageList.size()==0){
                            tvNothing.setVisibility(View.VISIBLE);
                        }
                        messageExpandableListAdapter = new MessageExpandableListAdapter(getApplicationContext(), messageList, listDataChild);
                        messageListView.setAdapter(messageExpandableListAdapter);
                        setListViewHeightBasedOnChildren(messageListView);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                loadMessages();
                            }
                        }, 5000);

                        Log.e("SURYA", "else message : "+response.raw().message() + " "+response.code());
                    }
                    Log.e("Surya", " message luar  "+response.raw().message() + " "+response.code());

                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<Messages> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), "Please check network connection.", Toast.LENGTH_SHORT).show();

                    messageList = db.getAllMessages().getMessages();
                    try {
                        Log.e(App.LOG,"get All failure message "+messageList.size());
                        for(Message message : messageList){
                            Log.e(App.LOG,"get one failure message "+message.getShortName());
                        }

                        HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
                        for (Message message : messageList) {
                            Log.e(App.LOG,   ""+message.getName()+" login"+" is voucher :"+message.getIsVoucher() +" size "+messageList.size()+" Read at "+message.getReadAt());
                            List<String> description = new ArrayList<String>();
                            description.add(message.getName());
                            listDataChild.put(String.valueOf(message.getId()), description); // Header, Child data
                        }
                        Log.e(App.LOG,"messages size "+messageList.size());
                        if(messageList.size()==0){
                            tvNothing.setVisibility(View.VISIBLE);
                        }else {
                            tvNothing.setVisibility(View.GONE);
                        }
                        messageExpandableListAdapter = new MessageExpandableListAdapter(getApplicationContext(), messageList, listDataChild);
                        messageListView.setAdapter(messageExpandableListAdapter);
                        setListViewHeightBasedOnChildren(messageListView);
                    }catch (Exception e){
                        tvNothing.setVisibility(View.VISIBLE);
                    }

                    Log.i("SURYA", t.getMessage() + "");
                }
            });
        }
    }

    /**
     * this method is for set listview height
     * @param group
     * @param listView is the expandablelist view
    **/
    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        MessageExpandableListAdapter listAdapter = (MessageExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /**
     * this method is for set the expandable listview height based on children
     * @param listView is the expandablelistview view
    * */
    public static void setListViewHeightBasedOnChildren(ExpandableListView listView) {
        MessageExpandableListAdapter listAdapter = (MessageExpandableListAdapter) listView.getExpandableListAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View listItem = listAdapter.getGroupView(i, false, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @OnClick(R.id.image_back)
    public void doActionLeft(View view) {
        if (fromNotif){
            Intent intent;
            intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (fromNotif){
            Intent intent;
            intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else {
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        App.MESSAGE_COUNTER = 0;

        ShortcutBadger.removeCount(getApplicationContext());

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Config.PUSH_NOTIFICATION);
        FirebaseBroadcastReceiver receiver = new FirebaseBroadcastReceiver();
        registerReceiver(receiver, intentFilter);
        Notification.clearNotifications(getApplicationContext());

    }

    private class FirebaseBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            int state = extras.getInt("isVoucher",0);
            sessionManager = new SessionManager(getApplicationContext());
            sessionManager.setCountMessage(0);
            loadMessages();
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            Log.e(App.LOG,"broadcast state from firebase "+state);
        }
    }
}
