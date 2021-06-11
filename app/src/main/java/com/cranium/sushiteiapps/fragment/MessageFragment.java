package com.cranium.sushiteiapps.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.BitmapCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.adapter.CommentDeleteAdapter;
import com.cranium.sushiteiapps.adapter.MessageExpandableListAdapter;
import com.cranium.sushiteiapps.adapter.OutletCitySpinnerAdapter;
import com.cranium.sushiteiapps.adapter.OutletSpinnerAdapter;
import com.cranium.sushiteiapps.api.CommentApi;
import com.cranium.sushiteiapps.api.MessageApi;
import com.cranium.sushiteiapps.model.Message;
import com.cranium.sushiteiapps.model.MessageLoad;
import com.cranium.sushiteiapps.model.Outlet;
import com.cranium.sushiteiapps.model.OutletCity;
import com.cranium.sushiteiapps.model.User;
import com.cranium.sushiteiapps.model.request.CommentRequest;
import com.cranium.sushiteiapps.model.response.CommentResponse;
import com.cranium.sushiteiapps.model.response.Messages;
import com.cranium.sushiteiapps.service.GMailSender;
import com.cranium.sushiteiapps.util.ClickListener;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.GalleryImage;
import com.cranium.sushiteiapps.util.RecyclerTouchListener;
import com.cranium.sushiteiapps.util.SessionManager;
import com.cranium.sushiteiapps.util.SwipeController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vlk.multimager.adapters.GalleryImagesAdapter;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindColor;
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

//import com.sangcomz.fishbun.FishBun;
//import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
//import com.sangcomz.fishbun.adapter.image.impl.PicassoAdapter;
//import com.sangcomz.fishbun.define.Define;

/**
 * Created by Dayona on 5/31/17.
 */

public class MessageFragment extends Fragment {

    @BindView(R.id.message_list_view)
    ExpandableListView messageListView;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.nothing_message) TextView tvNothing;
    @BindColor(R.color.colorAccent) int accent;

    private Retrofit retrofit;
    private List<OutletCity> outletCities;
    private List<Outlet> outlets;
    private Integer selectedCity;
    private Integer selectedOutlet;
    private DatabaseHelper db;
    private DatePickerDialog datePickerDialog;
    private SessionManager sessionManager;
    private User user;
    String Tag = getClass().getName();

    private List<Message> messageList;

    private ProgressDialog progressDialog;
    private MessageExpandableListAdapter messageExpandableListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(App.API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        db = new DatabaseHelper(getContext());

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
//        messageListView.setNestedScrollingEnabled(true);


        return view;
    }

    private void loadMessages() {

        swipeContainer.setRefreshing(true);

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

            Call<Messages> call = service.messages(new MessageLoad(sessionManager.getLoginUpdate(),sessionManager.getDeviceNumber()));

            call.enqueue(new Callback<Messages>() {
                @Override
                public void onResponse(Call<Messages> call, Response<Messages> response) {

                    if (response.raw().isSuccessful()) {
                        messageList = response.body().getMessages();

                        swipeContainer.setRefreshing(false);

                        HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
                        for (Message message : messageList) {
                            Log.e("SURYA",   ""+message.getName()+"login"+" is voucher :"+message.getIsVoucher() +" size "+messageList.size());
//                            if (message.getIsVoucher()==0){
                            List<String> description = new ArrayList<String>();
                            description.add(message.getName());
                            listDataChild.put(String.valueOf(message.getId()), description); // Header, Child data
//                            }
                        }

                        if (listDataChild.size()==0){
                            messageList.clear();
                            tvNothing.setVisibility(View.VISIBLE);
                        }
                        messageExpandableListAdapter = new MessageExpandableListAdapter(getContext(), messageList, listDataChild);
                        messageListView.setAdapter(messageExpandableListAdapter);
                        setListViewHeightBasedOnChildren(messageListView);
                    }else{
                        swipeContainer.setRefreshing(false);
                        Log.i("SURYA", response.raw().message() + "");
                    }

                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<Messages> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    swipeContainer.setRefreshing(false);
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

            Call<Messages> call = service.messages(new MessageLoad(sessionManager.getLoginUpdate(),sessionManager.getDeviceNumber()));

            call.enqueue(new Callback<Messages>() {
                @Override
                public void onResponse(Call<Messages> call, Response<Messages> response) {

                    if (response.raw().isSuccessful()) {
                        messageList = response.body().getMessages();

                        swipeContainer.setRefreshing(false);

                        HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
                        for (Message message : messageList) {
                            if (message.getIsVoucher()==0){
                                List<String> description = new ArrayList<String>();
                                description.add(message.getName());
                                listDataChild.put(String.valueOf(message.getId()), description); // Header, Child data
                            }
                        }

                        if (listDataChild.size()==0){
                            messageList.clear();
                            tvNothing.setVisibility(View.VISIBLE);
                        }

                        messageExpandableListAdapter = new MessageExpandableListAdapter(getContext(), messageList, listDataChild);
                        messageListView.setAdapter(messageExpandableListAdapter);
                        setListViewHeightBasedOnChildren(messageListView);
                    }else{
                        swipeContainer.setRefreshing(false);
                        Log.i("SURYA", response.raw().message() + "");
                    }

                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<Messages> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    swipeContainer.setRefreshing(false);
                }
            });

        }



    }



    private void setDialogBox() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmation_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView description = (TextView) dialog.findViewById(R.id.description);

        title.setText("Thank you for your feedback");
        description.setText("");
        description.setVisibility(View.GONE);

        ImageButton close = (ImageButton) dialog.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialog.show();
    }

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

        Log.i("cranium", "cranium " + height);

    }

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



}