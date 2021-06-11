package com.cranium.sushiteiapps.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.activity.MessageDetailActivity;
import com.cranium.sushiteiapps.activity.VoucherActivity;
import com.cranium.sushiteiapps.activity.VoucherDetailActivity;
import com.cranium.sushiteiapps.api.MessageApi;
import com.cranium.sushiteiapps.api.VoucherApi;
import com.cranium.sushiteiapps.model.Message;
import com.cranium.sushiteiapps.model.Voucher;
import com.cranium.sushiteiapps.model.VoucherExpired;
import com.cranium.sushiteiapps.model.response.EditProfileResponseError;
import com.cranium.sushiteiapps.model.response.MessageUpdated;
import com.cranium.sushiteiapps.model.response.VoucherGet;
import com.cranium.sushiteiapps.util.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dayona on 06/16/17.
 */

public class VoucherExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Message> messageList; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;

    public VoucherExpandableListAdapter(Context context, List<Message> listDataHeader,
                                        HashMap<String, List<String>> listChildData) {
        this.context = context;
        this.messageList = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(String.valueOf(this.messageList.get(groupPosition).getId())).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);
        final Message message = (Message) messageList.get(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.voucher_detail, null);
        }

        TextView description = (TextView) convertView.findViewById(R.id.description);
        TextView readMore = (TextView) convertView.findViewById(R.id.readmore);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        Log.e(App.LOG,"code voucher from adapter "+message.getCodeVoucher());
        description.setText(message.getShortDescription());
        Picasso.with(context).load(App.URL + "files/messages/" + message.getImage()).error(R.drawable.image_315x315).into(image);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VoucherDetailActivity.class);
                intent.putExtra("message", message);
                intent.putExtra("fromActivity",true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(String.valueOf(this.messageList.get(groupPosition).getId()))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.messageList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.messageList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View view, ViewGroup parent) {
        final Message message = (Message) messageList.get(groupPosition);
        int accent = context.getResources().getColor(R.color.colorAccent);
        int white = context.getResources().getColor(R.color.colorWhite);
        int dark = context.getResources().getColor(R.color.colorDark);
        final Drawable messageOpenedDark = ContextCompat.getDrawable(context, R.drawable.ic_message_open_outline_24_dp);
        final Drawable messageOpenedWhite = ContextCompat.getDrawable(context, R.drawable.ic_message_open_white_outline_24_dp);
        final Drawable messageUnRead = ContextCompat.getDrawable(context, R.drawable.ic_message_green_outline_24_dp);

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.voucher_list, null);
        }

        TextView name = (TextView) view.findViewById(R.id.name);
        TextView date = (TextView) view.findViewById(R.id.date);
        ImageView messageImage = (ImageView) view.findViewById(R.id.message_image);
        ImageView deleteImage = (ImageView) view.findViewById(R.id.delete);

        if (message.getExpired()==1)deleteImage.setVisibility(View.VISIBLE);
        else deleteImage.setVisibility(View.INVISIBLE);

        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.e(App.LOG, "message "+message.getShortName()+" is clicked");

                sessionManager = new SessionManager(context);
                AlertDialog.Builder mDialog = new AlertDialog.Builder(context);
                mDialog.setMessage("Are you sure to delete this item ?");
                mDialog.setPositiveButton(                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              "Yes",new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog,int which) {
                        progressDialog = ProgressDialog.show(context, "Loading", "Please wait ...", true);
                        OkHttpClient client = null;
                        if (sessionManager.isLoggedIn()){
                            client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                                @Override
                                public okhttp3.Response intercept(Chain chain) throws IOException {
                                    Request newRequest  = chain.request().newBuilder()
                                            .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                                            .addHeader("Content-Type", "application/json")
                                            .build();
                                    return chain.proceed(newRequest);
                                }
                            }).build();
                        }else {
                            client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                                @Override
                                public okhttp3.Response intercept(Chain chain) throws IOException {
                                    Request newRequest  = chain.request().newBuilder()
                                            .addHeader("Content-Type", "application/json")
                                            .build();
                                    return chain.proceed(newRequest);
                                }
                            }).build();
                        }

                        Retrofit retrofit = new Retrofit.Builder()
                                .client(client)
                                .baseUrl(App.API)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        VoucherApi service = retrofit.create(VoucherApi.class);
                        Call<VoucherGet> call = service.deleteVoucher(new VoucherExpired(message.getId().toString(),sessionManager.getDeviceNumber()));
                        call.enqueue(new Callback<VoucherGet>() {
                            @Override
                            public void onResponse(Call<VoucherGet> call, Response<VoucherGet> response) {
                                if (response.raw().isSuccessful()) {
                                    Toast.makeText(context, "Delete Successfull", Toast.LENGTH_SHORT).show();
                                    ((VoucherActivity)context).loadVoucher();
                                    progressDialog.dismiss();
                                }else if(response.code()==400){
                                    try {
                                        progressDialog.dismiss();
                                        Log.e("crannnn", response.errorBody().toString());
                                        Gson gson = new GsonBuilder().create();
                                        EditProfileResponseError mError = gson.fromJson(response.errorBody().string(), EditProfileResponseError.class);
                                        Toast.makeText(context, response.message() +" "+response.code(), Toast.LENGTH_LONG).show();
                                    } catch (IOException e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(context,"Something wrong\nPlease check the network connection !",Toast.LENGTH_LONG).show();
                                        // handle failure to read error
                                    }
                                }else if(response.code()==401){
                                    progressDialog.dismiss();
                                    Toast.makeText(context,  "This voucher is already unavailable !", Toast.LENGTH_LONG).show();
                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(context, response.message() +" "+response.code(), Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<VoucherGet> call, Throwable t) {
                                Toast.makeText(context,"Please check the network connection !",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                });
                mDialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.cancel();
                            }
                        });
                mDialog.show();

            }
        });

        if (isExpanded) {
            view.setBackgroundResource(R.color.colorAccent);
            date.setTypeface(null, Typeface.NORMAL);
            name.setTypeface(null, Typeface.NORMAL);
            name.setTextColor(white);
            date.setTextColor(white);
            messageImage.setImageDrawable(messageOpenedWhite);
            if (message.getReadAt() == null) {
                date.setTypeface(null, Typeface.NORMAL);
                name.setTypeface(null, Typeface.NORMAL);
                message.setReadAt((new Date()).toString());
                setToUpdated(message);
            }
        } else {
            view.setBackgroundResource(R.color.colorWhite);
            if (message.getReadAt() != null) {
                date.setTypeface(null, Typeface.NORMAL);
                name.setTypeface(null, Typeface.NORMAL);
                name.setTextColor(dark);
                date.setTextColor(dark);
                messageImage.setImageDrawable(messageOpenedDark);
            } else {
                date.setTypeface(null, Typeface.BOLD);
                name.setTypeface(null, Typeface.BOLD);
                name.setTextColor(accent);
                date.setTextColor(accent);
                messageImage.setImageDrawable(messageUnRead);
            }
        }

        name.setText(message.getShortName());
        date.setText(message.getShortStartDate());

        return view;
    }

    private void setToUpdated(Message message) {

        final SessionManager sessionManager = new SessionManager(context);
        OkHttpClient client;

        if (sessionManager.isLoggedIn()){
            client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();
        }else{
            client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(App.API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        VoucherApi service = retrofit.create(VoucherApi.class);

        Call<MessageUpdated> call = service.updateToRead(new Voucher(""+message.getId(),sessionManager.getDeviceNumber()));
        Log.e(App.LOG,"set read voucher "+message.getId()+" "+sessionManager.getDeviceNumber());

        call.enqueue(new Callback<MessageUpdated>() {
            @Override
            public void onResponse(Call<MessageUpdated> call, Response<MessageUpdated> response) {

                if (response.raw().isSuccessful()) {
                    Log.e(App.LOG,"suskes terbaca dan terkirim ke api");
                }
            }

            @Override
            public void onFailure(Call<MessageUpdated> call, Throwable t) {
            }
        });

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
