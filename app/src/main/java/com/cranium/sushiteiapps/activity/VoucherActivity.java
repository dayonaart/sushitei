package com.cranium.sushiteiapps.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.cranium.sushiteiapps.adapter.VoucherExpandableListAdapter;
import com.cranium.sushiteiapps.api.VoucherApi;
import com.cranium.sushiteiapps.model.Message;
import com.cranium.sushiteiapps.model.Vouchers;
import com.cranium.sushiteiapps.model.response.Messages;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.Notification;
import com.cranium.sushiteiapps.util.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
 * Created by suryamudti on 28/05/18.
 */

public class VoucherActivity extends AppCompatActivity {

    @BindView(R.id.message_list_view) ExpandableListView messageListView;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.nothing_message) TextView tvNothing;
    @BindColor(R.color.colorAccent) int accent;

    private SessionManager sessionManager;
    private List<Message> messageList = new ArrayList<Message>();
    private DatabaseHelper db;

    private VoucherExpandableListAdapter voucherExpandableListAdapter;
    private boolean fromNotif = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ShortcutBadger.removeCount(getApplicationContext());

        setContentView(R.layout.activity_voucher);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.setCountVoucher(0);

        db = new DatabaseHelper(getApplicationContext());
        loadVoucher();
        fromNotif = getIntent().getBooleanExtra("fromNotif",false);

        messageListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);

                return false;
            }
        });
        messageListView.setGroupIndicator(null);

        messageListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                VoucherExpandableListAdapter listAdapter = (VoucherExpandableListAdapter) messageListView.getExpandableListAdapter();
                View groupItem = listAdapter.getGroupView(groupPosition, true, null, messageListView);
                RelativeLayout layout = (RelativeLayout) groupItem.findViewById(R.id.group_view);
                layout.setVisibility(View.GONE);
                Log.i("cranium", groupPosition + "");
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadVoucher();
            }
        });
    }

    /**
     * this method is for load voucher from API based on login status
     *             1 = load vouchers from API,
     *             otherwise load from local db
    * */
    public void loadVoucher() {
        sessionManager.setNewVoucher(false);
        swipeContainer.setRefreshing(true);
            OkHttpClient client = null;
            Retrofit retrofit = null;
            messageList = db.getAllVouchers().getMessages();

            HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
            List<Message> voucherList = new ArrayList<Message>();

            try{
                for (Message message : messageList) {
                    List<String> description = new ArrayList<String>();
                    description.add(message.getName());
                    listDataChild.put(String.valueOf(message.getId()), description); // Header, Child data
                    voucherList.add(message);
                }
                voucherExpandableListAdapter = new VoucherExpandableListAdapter(this, voucherList, listDataChild);
                messageListView.setAdapter(voucherExpandableListAdapter);
                setListViewHeightBasedOnChildren(messageListView);

            }catch (Exception e){

            }
            if(sessionManager.isLoggedIn()){
                client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
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

            retrofit = new Retrofit.Builder()
                    .baseUrl(App.API)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            VoucherApi service = retrofit.create(VoucherApi.class);
            Log.e(App.LOG,   "before load api");
            Call<Messages> call = service.voucherPost(new Vouchers(sessionManager.getDeviceNumber(), new Date().toString()));
            call.enqueue(new Callback<Messages>() {
                @Override
                public void onResponse(Call<Messages> call, Response<Messages> response) {
                    if (response.raw().isSuccessful()) {
                        messageList = response.body().getMessages();
                        db.deleteAlLVoucher();
                        db.createVoucher(response.body());

                        swipeContainer.setRefreshing(false);

                        HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();

                        List<Message> voucherList = new ArrayList<Message>();

                        if (messageList.size()>0){
                            for (Message message : messageList) {
                                List<String> description = new ArrayList<String>();
                                description.add(message.getName());
                                listDataChild.put(String.valueOf(message.getId()), description); // Header, Child data
                                voucherList.add(message);
                            }
                        }

                        if(messageList.size()==0){
                            tvNothing.setVisibility(View.VISIBLE);
                            messageListView.setVisibility(View.GONE);
                            tvNothing.setText("There is no voucher");
                        }else{
                            tvNothing.setVisibility(View.GONE);
                        }
                        voucherExpandableListAdapter = new VoucherExpandableListAdapter(VoucherActivity.this, voucherList, listDataChild);
                        messageListView.setAdapter(voucherExpandableListAdapter);
                        setListViewHeightBasedOnChildren(messageListView);

                        Log.e(App.LOG,"size Voucher "+messageList.size());
                    }else if (response.code()==404){
                        tvNothing.setVisibility(View.VISIBLE);
                        messageListView.setVisibility(View.GONE);
                        tvNothing.setText("There is no voucher");
                        swipeContainer.setRefreshing(false);
                        Log.e("SURYA", response.raw().message() + " ");
                    }else{

                        messageList = db.getAllVouchers().getMessages();
                        Log.e(App.LOG,"voucher size "+messageList.size());
                        HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();

                        List<Message> voucherList = new ArrayList<Message>();

                        if (messageList.size()>0){
                            for (Message message : messageList) {
                                List<String> description = new ArrayList<String>();
                                description.add(message.getName());
                                listDataChild.put(String.valueOf(message.getId()), description); // Header, Child data
                                voucherList.add(message);
                            }
                        }

                        if(messageList.size()==0){
                            tvNothing.setVisibility(View.VISIBLE);
                            messageListView.setVisibility(View.GONE);
                            tvNothing.setText("There is no voucher");
                        }else{
                            tvNothing.setVisibility(View.GONE);
                        }
                        voucherExpandableListAdapter = new VoucherExpandableListAdapter(VoucherActivity.this, voucherList, listDataChild);
                        messageListView.setAdapter(voucherExpandableListAdapter);
                        setListViewHeightBasedOnChildren(messageListView);

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
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<Messages> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    swipeContainer.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), "Please check network connection.", Toast.LENGTH_SHORT).show();

                    messageList = db.getAllVouchers().getMessages();
                    try{
                        HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
                        List<Message> voucherList = new ArrayList<Message>();

                        if (messageList.size()>0){
                            for (Message message : messageList) {
                                List<String> description = new ArrayList<String>();
                                description.add(message.getName());
                                listDataChild.put(String.valueOf(message.getId()), description); // Header, Child data
                                voucherList.add(message);
                            }
                        }

                        if(messageList.size()==0){
                            messageListView.setVisibility(View.GONE);
                            tvNothing.setVisibility(View.VISIBLE);
                        }else{
                            tvNothing.setVisibility(View.GONE);
                        }
                        voucherExpandableListAdapter = new VoucherExpandableListAdapter(VoucherActivity.this, voucherList, listDataChild);
                        messageListView.setAdapter(voucherExpandableListAdapter);
                        setListViewHeightBasedOnChildren(messageListView);

                    }catch (Exception e){
                        tvNothing.setVisibility(View.VISIBLE);
                    }

                }
            });
    }

    /**
     * this method is for set listview height
     * @param group
     * @param listView is the expandablelist view
     * */
    private void setListViewHeight(ExpandableListView listView,int group) {
        VoucherExpandableListAdapter listAdapter = (VoucherExpandableListAdapter) listView.getExpandableListAdapter();
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
            height = 100;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

        Log.i("cranium", "cranium " + height);

    }

    /**
     * this method is for set the expandable listview height based on children
     * @param listView is the expandablelistview view
     * */
    public static void setListViewHeightBasedOnChildren(ExpandableListView listView) {
        VoucherExpandableListAdapter listAdapter = (VoucherExpandableListAdapter) listView.getExpandableListAdapter();
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
        App.VOUCHER_COUNTER = 0;
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
            loadVoucher();
            sessionManager = new SessionManager(getApplicationContext());
            sessionManager.setCountVoucher(0);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            Log.e(App.LOG,"broadcast state from firebase "+state);

        }
    }
}
