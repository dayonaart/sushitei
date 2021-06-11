package com.cranium.sushiteiapps.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.BuildConfig;
import com.cranium.sushiteiapps.Config;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.adapter.MessagetTypeAdapter;
import com.cranium.sushiteiapps.adapter.PagerAdapter;
import com.cranium.sushiteiapps.adapter.StoreLocationAdapter;
import com.cranium.sushiteiapps.api.MessageApi;
import com.cranium.sushiteiapps.api.OutletApi;
import com.cranium.sushiteiapps.api.RequestApi;
import com.cranium.sushiteiapps.api.UserApi;
import com.cranium.sushiteiapps.model.Firebase;
import com.cranium.sushiteiapps.model.Message;
import com.cranium.sushiteiapps.model.MessageLoad;
import com.cranium.sushiteiapps.model.Outlet;
import com.cranium.sushiteiapps.model.OutletCity;
import com.cranium.sushiteiapps.model.User;
import com.cranium.sushiteiapps.model.request.EditProfileRequest;
import com.cranium.sushiteiapps.model.request.LoginRequest;
import com.cranium.sushiteiapps.model.request.ProfileImageRequest;
import com.cranium.sushiteiapps.model.response.Cities;
import com.cranium.sushiteiapps.model.response.EditProfileResponse;
import com.cranium.sushiteiapps.model.response.EditProfileResponseError;
import com.cranium.sushiteiapps.model.response.GetUser;
import com.cranium.sushiteiapps.model.response.LoginResponse;
import com.cranium.sushiteiapps.model.response.LogoutResponse;
import com.cranium.sushiteiapps.model.response.Messages;
import com.cranium.sushiteiapps.model.response.OutletCities;
import com.cranium.sushiteiapps.model.response.ProfileImageResponse;
import com.cranium.sushiteiapps.model.response.Seats;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.Notification;
import com.cranium.sushiteiapps.util.SessionManager;
import com.github.florent37.fiftyshadesof.FiftyShadesOf;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;
import com.sw926.imagefileselector.ErrorResult;
import com.sw926.imagefileselector.ImageFileSelector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
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
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.action_left) ImageButton actLeft;
    @BindView(R.id.pager) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_left_view) NavigationView leftNavigationView;
    @BindView(R.id.nav_right_view) NavigationView rightNavigationView;
    @BindView(R.id.count_message) TextView countMessage;
    @BindView(R.id.count_voucher) TextView countVoucher;

    @BindColor(R.color.colorBlack) int black;
    @BindColor(R.color.colorDark) int dark;

    ProgressDialog progressDialog;
    boolean messageTypeOpen = false;
    View viewMes = null;

    private int PICK_IMAGE_REQUEST = 1;
    private String pathImage = "";
    private String imageText = "";
    ImageFileSelector mImageFileSelector;
    CircularImageView imageUpload;
    SpannableString spannableString = new SpannableString("");
    ProgressBar imageUploadProgressBar;
    private CountDownTimer timer;

    @OnClick(R.id.action_left)
    public void doActionLeft(View view) {
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
        } else {
            drawer.openDrawer(Gravity.LEFT);
        }
    }

    SessionManager session;
    PagerAdapter pagerAdapter;
    User user;
    DatabaseHelper db;
    BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = new SessionManager(getApplicationContext());

        ShortcutBadger.removeCount(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

        db = new DatabaseHelper(getApplicationContext());
        Log.e("SURYA","device number main "+session.getDeviceNumber()+" "+db.getFirebase().getFirebaseToken());

        leftNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle Left navigation view item clicks here.
                int id = item.getItemId();

                Intent intent;

                if (id == R.id.nav_whatsnew) {
                    intent = new Intent(MainActivity.this, WhatsNewActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_message) {
                    leftNavigationView.getMenu().setGroupVisible(R.id.group,false);
                    leftNavigationView.getHeaderView(0).setVisibility(View.GONE);
                    messageTypeOpen = true;
                    manageMessageTypeMenu();
                    return true;
                } else if (id == R.id.nav_menu) {
                    intent = new Intent(MainActivity.this, MenuCategoryListActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.nav_contactus) {
                    intent = new Intent(MainActivity.this, ContactUsActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_storelocation) {
                    leftNavigationView.getMenu().setGroupVisible(R.id.group, false);
                    leftNavigationView.getHeaderView(0).setVisibility(View.GONE);
                    manageStoreLocation();
                    return true;
                } else if (id == R.id.nav_wishlist) {
                    intent = new Intent(MainActivity.this, WishListActivity.class);
                    startActivity(intent);
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        if (session.getDeviceNumber().equals("")){
            session.setDeviceNumber(getDeviceNumber());
            db.createFirebase(new Firebase(getFirebaseToken(),getDeviceNumber()));
        }
        Log.e("SURYA","device number main "+session.getDeviceNumber()+" "+db.getFirebase().getFirebaseToken());

        if (session.isLoggedIn()) {
            rightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    // Handle Right navigation view item clicks here.
                    int id = item.getItemId();

                    Intent intent;

                    refreshMess();
                    drawer.closeDrawer(GravityCompat.END); /*Important Line*/
                    return true;
                }
            });
            setDataUser();
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, rightNavigationView);
        }

        setTabLayout();
        setDataToDb();
        manageLeftMenu();

        if(session.isLoggedIn()) {
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    refreshMess();
                }

            }, 0, 480000);
        }

        checkMessageAndVoucher();
        viewMes = LayoutInflater.from(this).inflate(R.layout.left_navigation_layout_message, null);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            checkMessageAndVoucher();
            ShortcutBadger.removeCount(getApplicationContext());

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Config.PUSH_NOTIFICATION);
            FirebaseBroadcastReceiver receiver = new FirebaseBroadcastReceiver();
            registerReceiver(receiver, intentFilter);

            // register GCM registration complete receiver
//            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                    new IntentFilter(Config.REGISTRATION_COMPLETE));
//
//            // register new push message receiver
//            // by doing this, the activity will be notified each time a new message arrives
//            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                    new IntentFilter(Config.PUSH_NOTIFICATION));

            // clear the notification area when the app is opened
            Notification.clearNotifications(getApplicationContext());

        }catch (Exception e){

        }

    }

    @Override
    protected void onPause() {
        try{
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
            super.onPause();
        }catch (Exception e){

        }
    }

    /**
     * managing wich menu is hidden if user is login
     */
    private void manageLeftMenu() {
        leftNavigationView.getMenu().findItem(R.id.version).setTitle("App version "+ BuildConfig.VERSION_NAME);
        if (session.isLoggedIn()) {
            leftNavigationView.getMenu().findItem(R.id.nav_wishlist).setVisible(true);
            leftNavigationView.getMenu().findItem(R.id.nav_message).setVisible(true);
        } else {
            leftNavigationView.getMenu().findItem(R.id.nav_wishlist).setVisible(false);
        }
    }

    /**
     * this method for synchronize left message text in left navigation drawer
     */
    public void manageLeftMenuMessage() {
        if (session.isLoggedIn()) {
            Messages messages = db.getAllMessages();
            MenuItem message = leftNavigationView.getMenu().findItem(R.id.nav_message);
            if (messages.getTotal() != null) {
                if (messages.getTotal() > 0) {
                    if (messages.getUnreadMessages() > 0) {
                        message.setTitle("Message (" + messages.getUnreadMessages() + ")");
                        SpannableString spannableString = new SpannableString(message.getTitle());
                        spannableString.setSpan(new TextAppearanceSpan(this, R.style.TextAppearanceAccent), 0, spannableString.length(), 0);
                        message.setTitle(spannableString);
                        message.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_message_green_outline_24_dp,null)).setTitle(spannableString);
                        countMessage.setText(messages.getUnreadMessages().toString());
                        countMessage.setVisibility(View.VISIBLE);

                    }
                }else{
                    message.setTitle("Message");
                    SpannableString spannableString = new SpannableString(message.getTitle());
                    spannableString.setSpan(new TextAppearanceSpan(this, R.style.TextAppearanceBlack), 0, spannableString.length(), 0);
                    message.setTitle(spannableString);
                    message.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_message_green_outline_24_dp,null)).setTitle(spannableString);
                    countMessage.setText(messages.getUnreadMessages().toString());
                    countMessage.setVisibility(View.GONE);
                }
                return;
            }
            SpannableString spannableString = new SpannableString(message.getTitle());
            spannableString.setSpan(new TextAppearanceSpan(this, R.style.TextAppearanceBlack), 0, spannableString.length(), 0);
            message.setTitle(spannableString);
            Drawable greyImg = ContextCompat.getDrawable(this, R.drawable.ic_message_outline_24_dp);
            message.setIcon(greyImg);
            countMessage.setVisibility(View.GONE);
        }
    }

    /**
     * managing side menu for choosing message type
     */
    private void manageMessageTypeMenu(){



        leftNavigationView.addView(viewMes);

        final ListView messageListView = (ListView) leftNavigationView.findViewById(R.id.messageTypeListView);
        final ImageView cityBackButton = (ImageView) leftNavigationView.findViewById(R.id.image_city_back);
        final TextView cityBackText = (TextView) leftNavigationView.findViewById(R.id.text_city_back);

        final List<String> lists = new ArrayList<>();
        if (session.getCountMessage()>0) {
            String vouchers = "News ("+session.getCountMessage()+")";
            lists.add(vouchers);
        }else{
            lists.add("News");
        }

        if (session.getCountVoucher()>0) {
            String vouchers = "Voucher ("+session.getCountVoucher()+")";
            lists.add(vouchers);
        }else{
            lists.add("Voucher");
        }

        final MessagetTypeAdapter adapter = new MessagetTypeAdapter(this, R.layout.left_navigation_item_message, lists);
        messageListView.setAdapter(adapter);
        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                // list outlet by city
                if(position == 0) {
                    Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                    intent.putExtra("news",session.getCountMessage());

                    leftNavigationView.removeView(viewMes);
                    leftNavigationView.getMenu().setGroupVisible(R.id.group, true);
                    leftNavigationView.getHeaderView(0).setVisibility(View.VISIBLE);

                    leftNavigationView.getMenu().setGroupVisible(R.id.group,false);
                    leftNavigationView.getHeaderView(0).setVisibility(View.GONE);
                    session.setCountMessage(0);
                    manageMessageTypeMenu();
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), VoucherActivity.class);
                    intent.putExtra("voucher",session.getCountVoucher());

                    leftNavigationView.removeView(viewMes);
                    leftNavigationView.getMenu().setGroupVisible(R.id.group, true);
                    leftNavigationView.getHeaderView(0).setVisibility(View.VISIBLE);

                    leftNavigationView.getMenu().setGroupVisible(R.id.group,false);
                    leftNavigationView.getHeaderView(0).setVisibility(View.GONE);
                    session.setCountVoucher(0);
                    manageMessageTypeMenu();
                    startActivity(intent);
                }
            }
        });

        // when city back click
        cityBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageTypeOpen = false;
                leftNavigationView.removeView(viewMes);
                leftNavigationView.getMenu().setGroupVisible(R.id.group, true);
                leftNavigationView.getHeaderView(0).setVisibility(View.VISIBLE);
                manageLeftMenu();
            }
        });
        cityBackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageTypeOpen = false;
                leftNavigationView.removeView(viewMes);
                leftNavigationView.getMenu().setGroupVisible(R.id.group, true);
                leftNavigationView.getHeaderView(0).setVisibility(View.VISIBLE);
                manageLeftMenu();
            }
        });
    }

    private void refreshMessage(){
        leftNavigationView.removeView(viewMes);
        leftNavigationView.getMenu().setGroupVisible(R.id.group,false);
        leftNavigationView.getHeaderView(0).setVisibility(View.GONE);
        messageTypeOpen = true;
        manageMessageTypeMenu();
    }

    /**
     * managing store location when user wan to choose location
     */
    private void manageStoreLocation() {

        final View view = LayoutInflater.from(this).inflate(R.layout.left_navigation_layout, null);
        leftNavigationView.addView(view);

        final ListView cityListView = (ListView) leftNavigationView.findViewById(R.id.cityListView);
        final ListView outletListView = (ListView) leftNavigationView.findViewById(R.id.outletListView);
        final ImageView cityBackButton = (ImageView) leftNavigationView.findViewById(R.id.image_city_back);
        final ImageView outletBackButton = (ImageView) leftNavigationView.findViewById(R.id.image_outlet_back);
        final TextView cityBackText = (TextView) leftNavigationView.findViewById(R.id.text_city_back);
        final TextView outletBackText = (TextView) leftNavigationView.findViewById(R.id.text_outlet_back);

        final List<OutletCity> lists = db.getAllOutletCities();
        final ArrayList<String> outletCitiesList = new ArrayList<String>();
        final ArrayList<String> outletList = new ArrayList<String>();
        outletCitiesList.clear();
        for (OutletCity outletCity : lists) {
            outletCitiesList.add(outletCity.getName());
        }
        // list city
        final StoreLocationAdapter adapter = new StoreLocationAdapter(this, R.layout.left_navigation_item, outletCitiesList);
        cityListView.setAdapter(adapter);
        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                // list outlet by city
                if(lists.get(position).getOutlets().size() == 1) {
                    Intent intent = new Intent(getApplicationContext(), OutletDetailActivity.class);
                    intent.putExtra("outlet", lists.get(position).getOutlets().get(0));
                    startActivity(intent);
                }else{
                    cityListView.setVisibility(View.GONE);
                    cityBackButton.setVisibility(View.INVISIBLE);
                    cityBackText.setVisibility(View.INVISIBLE);
                    outletBackButton.setVisibility(View.VISIBLE);
                    outletBackText.setVisibility(View.VISIBLE);
                    outletListView.setVisibility(View.VISIBLE);
                    outletList.clear();
                    for (Outlet outlet : lists.get(position).getOutlets()) {
                        outletList.add(outlet.getName());
                    }
                }

                StoreLocationAdapter outletAdapter = new StoreLocationAdapter(MainActivity.this, R.layout.left_navigation_item, outletList);
                outletListView.setAdapter(outletAdapter);
                outletListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                        Intent intent = new Intent(getApplicationContext(), OutletDetailActivity.class);
                        intent.putExtra("outlet", lists.get(position).getOutlets().get(i));
                        startActivity(intent);
                    }
                });

            }
        });

        // when city back click
        cityBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftNavigationView.removeView(view);
                leftNavigationView.getMenu().setGroupVisible(R.id.group, true);
                leftNavigationView.getHeaderView(0).setVisibility(View.VISIBLE);
                manageLeftMenu();
            }
        });
        cityBackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftNavigationView.removeView(view);
                leftNavigationView.getMenu().setGroupVisible(R.id.group, true);
                leftNavigationView.getHeaderView(0).setVisibility(View.VISIBLE);
                manageLeftMenu();
            }
        });

        // when outlet back click
        outletBackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityBackButton.setVisibility(View.VISIBLE);
                cityBackText.setVisibility(View.VISIBLE);
                outletBackButton.setVisibility(View.INVISIBLE);
                outletBackText.setVisibility(View.INVISIBLE);
                cityListView.setVisibility(View.VISIBLE);
                outletListView.setVisibility(View.GONE);
            }
        });
        outletBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityBackButton.setVisibility(View.VISIBLE);
                cityBackText.setVisibility(View.VISIBLE);
                outletBackButton.setVisibility(View.INVISIBLE);
                outletBackText.setVisibility(View.INVISIBLE);
                cityListView.setVisibility(View.VISIBLE);
                outletListView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * set data to db
     * - cities
     * - outlet cities
     * - outlets
     */
    private void setDataToDb() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(App.API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestApi service = retrofit.create(RequestApi.class);

        Call<Cities> call = service.cities();
        call.enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Call<Cities> call, Response<Cities> response) {
                if (response.raw().isSuccessful()) {
                    db.createCity(response.body());
                }
            }

            @Override
            public void onFailure(Call<Cities> call, Throwable t) {

            }
        });

        Call<Seats> seatsCall = service.seats();
        seatsCall.enqueue(new Callback<Seats>() {
            @Override
            public void onResponse(Call<Seats> call, Response<Seats> response) {

                if (response.raw().isSuccessful()) {
                    db.createSeat(response.body());
                }
            }

            @Override
            public void onFailure(Call<Seats> call, Throwable t) {

            }
        });

        OutletApi outletApi = retrofit.create(OutletApi.class);

        Call<OutletCities> outletCitiesCall = outletApi.outletCities();
        outletCitiesCall.enqueue(new Callback<OutletCities>() {
            @Override
            public void onResponse(Call<OutletCities> call, Response<OutletCities> response) {

                if (response.raw().isSuccessful()) {
                    db.createOutletCity(response.body());
                }
            }

            @Override
            public void onFailure(Call<OutletCities> call, Throwable t) {

            }
        });

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + session.getToken())
                        .addHeader("Content-Type", "application/json")
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();
        if (session.isLoggedIn()) {
            Retrofit retrofit1 = new Retrofit.Builder()
                    .baseUrl(App.API)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MessageApi messageApi = retrofit1.create(MessageApi.class);
            Call<Messages> messageCall = messageApi.messages(new MessageLoad(session.getLoginUpdate(),session.getDeviceNumber()));
            messageCall.enqueue(new Callback<Messages>() {
                @Override
                public void onResponse(Call<Messages> call, Response<Messages> response) {

                    if (response.raw().isSuccessful()) {
                        db.createMessage(response.body());
                    }
                    else if (response.raw().code() == 401) {
                        App.forceLogout(getApplicationContext());
                    }
                    else if (response.raw().code()  == 400) {
                        Log.e(App.LOG,"settodb "+response.message()+" "+response.code() );
//                        Toast.makeText(getApplicationContext(), response.raw().message()+" settodb code "+, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Messages> call, Throwable t) {

                }
            });

        }
    }

    /**
     * important method for refresh push message and trigering autologout
     */
    public void refreshMess(){
        if (session.isLoggedIn()) {

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + session.getToken())
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(newRequest);
                }
            }).readTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit1 = new Retrofit.Builder()
                    .baseUrl(App.API)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MessageApi messageApi = retrofit1.create(MessageApi.class);

            Call<Messages> messageCall = messageApi.messages(new MessageLoad(session.getLoginUpdate(),session.getDeviceNumber()));
            messageCall.enqueue(new Callback<Messages>() {
                @Override
                public void onResponse(Call<Messages> call, Response<Messages> response) {

                    Log.i("SURYA", "PESAN: "+response.raw().code());
                    Log.i("SURYA", "PESAN: "+response.raw().message()+
                            " \n"+session.getToken());

                    if (response.raw().isSuccessful()) {
                        db.createMessage(response.body());
//                        db.createMess(response.body().getMessages().get(1));
//                        manageLeftMenuMessage();
                        Log.i("aldieemaulana", "aldieemaulana tick tock "
                                + " and Status: " + response.body().getStatus()
                                + " and Total: " + response.body().getTotal());
                    }else if (response.raw().code() == 401) {
                        App.forceLogout(getApplicationContext());
                    }
                    else if (response.raw().code()  == 400) {
                        if (!response.raw().message().equals("Bad Request")){
//                            Toast.makeText(getApplicationContext(), response.raw().message()+" PESAN", Toast.LENGTH_SHORT).show();
                        }
//                        Toast.makeText(getApplicationContext(), response.raw().message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Messages> call, Throwable t) {
                    Log.e("Error Mess","Refresh Mess"+t.getMessage());
                }
            });
        }
    }

    /**
     *this method for logout
     * @param type 0 = fine logout; 1 = bad logout
     */
    private void attemptLogout(int type) {

        progressDialog = ProgressDialog.show(MainActivity.this, "Processing Logout", "Please wait ...", true);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
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

        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(App.API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi service = retrofit.create(UserApi.class);
        Log.e("SURYA","LOGOUT ID "+session.getDeviceNumber()+"\n"+getDeviceNumber());
        if (type==0){
            Call<LogoutResponse> call = service.logout();

            call.enqueue(new Callback<LogoutResponse>() {
                @Override
                public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {

                    if (response.raw().isSuccessful()) {
                        session.isLogOut();
                        db.deleteAlLWishes();
                        db.deleteAlLHistoryPoint();
                        db.deleteAlLVoucher();
                        db.deleteAlLMessage();
                        session.setCountVoucher(0);
                        session.setCountMessage(0);
                        Toast.makeText(MainActivity.this, "Your are now logged out", Toast.LENGTH_LONG).show();
//                        TastyToast.makeText(getApplicationContext(), "Your are now logged out", TastyToast.LENGTH_LONG, TastyToast.DEFAULT);
                    } else {
                        session.isLogOut();
                        db.deleteAlLWishes();
                        db.deleteAlLHistoryPoint();
                        db.deleteAlLVoucher();
                        db.deleteAlLMessage();
                        session.setCountVoucher(0);
                        session.setCountMessage(0);
                        Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_LONG).show();
                    }

                    DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                    databaseHelper.deleteAlLMessage();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<LogoutResponse> call, Throwable t) {
                    Log.i("cranium:save", "failed");
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Please check network",Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Call<LogoutResponse> call = service.logout();
            call.enqueue(new Callback<LogoutResponse>() {
                @Override
                public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {

                    session.isLogOut();
                    db.deleteAlLWishes();
                    db.deleteAlLHistoryPoint();

                    DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                    databaseHelper.deleteAlLMessage();

                    Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                    intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentMain);

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
//                    progressDialog.dismiss();

                }

                @Override
                public void onFailure(Call<LogoutResponse> call, Throwable t) {
                    Log.i("cranium:save", "failed");
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Please check network",Toast.LENGTH_SHORT).show();
                }
            });
            progressDialog.dismiss();
        }



    }

    /**
     * set user information for right navigation view
     */
    private void setDataUser() {
        //View headerView = rightNavigationView.inflateHeaderView(R.layout.nav_header_right);
        View headerView = rightNavigationView.getHeaderView(0);
        imageUpload = (CircularImageView) headerView.findViewById(R.id.image);
        imageUploadProgressBar = (ProgressBar) headerView.findViewById(R.id.image_progress_bar);
        ImageButton imageUploadButton = (ImageButton) headerView.findViewById(R.id.image_upload_button);
        AppCompatButton imageUploadButtonEd = (AppCompatButton) headerView.findViewById(R.id.image_upload_button_ed);
        TextView name = (TextView) headerView.findViewById(R.id.name);
        TextView email = (TextView) headerView.findViewById(R.id.email);
        TextView phone = (TextView) headerView.findViewById(R.id.phonenumber);
        TextView statusMember = (TextView) headerView.findViewById(R.id.statusmember);
        CircularImageView image = (CircularImageView) headerView.findViewById(R.id.image);
        LinearLayout editProfileButton = (LinearLayout) headerView.findViewById(R.id.edit_profile_button);
        AppCompatButton myCardButton = (AppCompatButton) headerView.findViewById(R.id.show_my_card_button);
        AppCompatButton logoutButton = (AppCompatButton) headerView.findViewById(R.id.logout_button);
        TextView point = (TextView) headerView.findViewById(R.id.point);
        TextView pointExpired = (TextView) headerView.findViewById(R.id.point_expired_date);
        ImageButton refreshButton = (ImageButton) headerView.findViewById(R.id.refresh);
        final TextView lastUpdatedPoint = (TextView) headerView.findViewById(R.id.last_updated_point);
        RelativeLayout myPointHistory = (RelativeLayout) headerView.findViewById(R.id.my_point_history);
        ImageView gotoHistory = (ImageView) headerView.findViewById(R.id.goto_history);
        RelativeLayout aboutSushitei = (RelativeLayout) headerView.findViewById(R.id.about_relative);
        RelativeLayout disclaimerSushitei = (RelativeLayout) headerView.findViewById(R.id.disclaimer_relative);
        RelativeLayout faqSushiTei = (RelativeLayout) headerView.findViewById(R.id.faq_relative);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        user = db.getUserByMemberCode(session.getMemberCode());
        name.setText(user.getFirstName() + " " + user.getLastName());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        statusMember.setText("Status: " + user.getStatusDescription());
        Picasso.with(getApplicationContext()).load(App.URL + "files/users/" + user.getImage()).error(R.drawable.ic_account_circle_green_50_dp).into(image);
        Log.i("aldieemaulana", "aldieemaulana " + App.URL + "files/users/" + user.getImage());
        Log.e("aldieemaulana", "compareTo: " + user.getDel());
        Log.e(App.LOG, "del " + user.getDel());
        Log.e(App.LOG, "del h14 " + user.getDelH14()+" user desc "+user.getStatusDescription()+" status "+user.getStatus()+" del "+user.getDel());
        if(user.getDelH14() <= 14 && user.getStatus().equals("1")) {
            if(user.getDelH14() == 14){
                showExpiredH14();
            }else if(user.getDelH14() <= 0 ){
                showResetDialog(0);
            }
            Log.e(App.LOG, "del dalam :  " + user.getDel());
        }else if(user.getDelH14() <= 0 ){
            showResetDialog(0);
        }else{
            if(user.getStatusDescription().equals("Expired")) {
                showResetDialog(0);
                Log.i("aldieemaulana", "compareToR: " + user.getDel());
            }
        }
        Log.e(App.LOG,user.getFormattedPointExpiredAtE());
        point.setText(user.getPoint());
        Log.e(App.LOG,"desk "+user.getStatusDescription()+" format "+user.getFormattedPointExpiredAtE()+" point "+user.getPoint()+" point expiring "+user.getPointExpiring()+" date expiry "+user.getFormattedDateOfExpiry());
        if(user.getStatusDescription().equals("Temporary")) {
            if(user.getFormattedPointExpiredAtE().equals("30 December 4000") ||user.getFormattedPointExpiredAtE().equals("30 Dec 4000") || user.getFormattedPointExpiredAtE().equals("30 December 3000") || user.getFormattedPointExpiredAtE().equals("30 Dec 3000")) {
                pointExpired.setText("Expired Point per -");
            }else{
                pointExpired.setText("Your Temporary member will be expired on " + user.getFormattedPointExpiredAtE());
            }
        }else{
            if (user.getPoint().equals("0") || user.getPointExpiring().equals("0")){
                pointExpired.setText("");
            }else{
                pointExpired.setText("Expired Point per " + user.getFormattedDateOfExpiry()+" is "+user.getPointExpiring());
            }
        }
        lastUpdatedPoint.setText("Last Updated: " + session.getKeyPointUpdated());

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                launchPointRefreshDialog();
                refreshDo();
                session.setKeyPointUpdated();
                lastUpdatedPoint.setText("Last Updated: " + session.getKeyPointUpdated());
            }
        });
        gotoHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getStatusDescription().equals("Expired")) {
                    showResetDialog(2);
                    hideRightDrawer(v);
                }else{
                    hideRightDrawer(v);
                    Intent intent = new Intent(MainActivity.this, PointHistoryActivity.class);
                    startActivity(intent);
                }
            }
        });
        aboutSushitei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRightDrawer(v);
                Intent intent = new Intent(MainActivity.this, AboutSushiteiActivity.class);
                startActivity(intent);
            }
        });
        disclaimerSushitei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRightDrawer(v);
                Intent intent = new Intent(MainActivity.this, DisclaimerSushiteiActivity.class);
                startActivity(intent);
            }
        });

        faqSushiTei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRightDrawer(v);
                Intent intent = new Intent(MainActivity.this, FaqSushiteiActivity.class);
                startActivity(intent);
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRightDrawer(v);
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        imageUploadButtonEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRightDrawer(v);
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
        myCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getStatusDescription().equals("Expired")) {
                    showResetDialog(1);
                    hideRightDrawer(v);

                }else{
                    hideRightDrawer(v);
                    Intent intent = new Intent(getApplicationContext(), MyCardActivity.class);
                    startActivity(intent);
                }

            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRightDrawer(v);
                attemptLogout(0);
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        imageUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRightDrawer(v);
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
        manageUploadImage();
    }

    /**
     * this will show the image selector for comments fragments
     * then handle the request
     */
    private void manageUploadImage() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1201);
        }

        mImageFileSelector = new ImageFileSelector(this);
        mImageFileSelector.setCallback(new ImageFileSelector.Callback() {
            @Override
            public void onError(ErrorResult errorResult) {
                switch (errorResult) {
                    case permissionDenied:
                    case canceled:
                    case error:
                        Log.i("cranium", "uploaderror");
                        imageUploadProgressBar.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onSuccess(String file) {
                Log.i("cranium", "result upload:" + file);

                imageUploadProgressBar.setVisibility(View.VISIBLE);

                final Bitmap bm = BitmapFactory.decodeFile(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 30, baos); // bm is the bitmap object
                byte[] b = baos.toByteArray();
                pathImage = Base64.encodeToString(b, Base64.DEFAULT);

                imageText = "data:image/jpeg;base64," + pathImage.replaceAll("\r*\n*", "");
                Log.i("cranium", "image text:" + imageText);

                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request newRequest  = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + session.getToken())
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

                ProfileImageRequest request = new ProfileImageRequest(imageText);
                Call<ProfileImageResponse> call = service.uploadProfileImage(session.getMemberCode(), request);
                call.enqueue(new Callback<ProfileImageResponse>() {
                    @Override
                    public void onResponse(Call<ProfileImageResponse> call, Response<ProfileImageResponse> response) {
                        if (response.raw().isSuccessful()) {
                            imageUpload.setImageBitmap(bm);
                            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                            db.createUser(response.body().getUser());
                            Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Upload Photo is failed", Toast.LENGTH_SHORT).show();
                        }

                        imageUploadProgressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<ProfileImageResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Upload Photo is failed, please try again", Toast.LENGTH_SHORT).show();
                        imageUploadProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }

    /**
     * simple setting for imagefileselector
     */
    private void captureImage() {
        mImageFileSelector.setOutPutImageSize(800,800);
        mImageFileSelector.setQuality(100);
        mImageFileSelector.selectImage(this, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mImageFileSelector.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (session.isLoggedIn()) {
            mImageFileSelector.onActivityResult(this, requestCode, resultCode, data);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (session.isLoggedIn()) {
            mImageFileSelector.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (session.isLoggedIn()) {
            mImageFileSelector.onRestoreInstanceState(savedInstanceState);
        }
    }

    /**
     * this for setting the tabs layout
     */
    private void setTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("HOT MENU"));
        tabLayout.addTab(tabLayout.newTab().setText("WAITING LIST"));
        tabLayout.addTab(tabLayout.newTab().setText("COMMENT"));
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_hot_menu_24_dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_waiting_outline_24_dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_comment_outline_24_dp);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(dark, black);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                LinearLayout tabLayoute = (LinearLayout)((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView tabTextView = (TextView) tabLayoute.getChildAt(1);
                tabTextView.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/latoRegular.ttf"), Typeface.NORMAL);
                switch (tab.getPosition()) {
                    case 0 :
                        tab.setIcon(R.drawable.ic_hot_menu_24_dp);
                        break;
                    case 1 :
                        tab.setIcon(R.drawable.ic_waiting_24_dp);
                        break;
                    case 2 :
                        tab.setIcon(R.drawable.ic_comment_24_dp);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0 :
                        tab.setIcon(R.drawable.ic_hot_menu_outline_24_dp);
                        break;
                    case 1 :
                        tab.setIcon(R.drawable.ic_waiting_outline_24_dp);
                        break;
                    case 2 :
                        tab.setIcon(R.drawable.ic_comment_outline_24_dp);
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        changeTabsFont();
    }

    /**
     * dialog expired H14
     * this will show before expire h14
     */
    private void showExpiredH14() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.remindertocollect_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView textExpired = (TextView) dialog.findViewById(R.id.description);

        textExpired.setText("Your temporary member will be expired in "+user.getFormattedPointExpiredAtE()+". Visit us and do more transaction to become Permanent Member.");

        ImageButton close = (ImageButton) dialog.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * dialog reset point if point is expire
     * @param type 0 : do nothing; 1 : to my card; 2 : to point history
     */
    private void showResetDialog(final int type) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.remindertoreset_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageButton close = (ImageButton) dialog.findViewById(R.id.close);
        AppCompatButton rese = (AppCompatButton) dialog.findViewById(R.id.register_button);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent;
                switch (type){
                    case 0 :
                        break;
                    case 1: intent = new Intent(getApplicationContext(), MyCardActivity.class);
                            startActivity(intent);
                        break;
                    case 2 : intent = new Intent(getApplicationContext(), PointHistoryActivity.class);
                            startActivity(intent);
                        break;
                }

            }
        });
        rese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPointRefreshDialog();
                resetDo();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    /**
     * dialog loading refresh point
     */
    public void launchPointRefreshDialog() {

        final ProgressDialog progressDialog = ProgressDialog.show(this, "Refresh Point", "Please wait ...", true);
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
     * this will change default tabs font
     */
    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getAssets(),"fonts/latoRegular.ttf"), Typeface.NORMAL);
                }
            }
        }
    }

    /**
     * this will hidden right navigation drawer
     * @param view
     */
    public void hideRightDrawer(View view) {
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else {
            drawer.openDrawer(Gravity.RIGHT);
        }
    }

    /**
     * this will hide left navigation drawer
     * @param view
     */
    public void hideLeftDrawer(View view) {
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
        } else {
            drawer.openDrawer(Gravity.RIGHT);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem itemRight = menu.findItem(R.id.action_openRight);
        if (session.isLoggedIn()) {
            itemRight.setIcon(getResources().getDrawable(R.drawable.ic_account_circle_green_24_dp));
        } else {
            itemRight.setIcon(getResources().getDrawable(R.drawable.ic_account_circle_white_24dp));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_openRight) {
            if (session.getToken() != null) {
                refreshMess();
                drawer.openDrawer(GravityCompat.END);
            }else{
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("force logout","");
                startActivity( intent);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        Intent intent;

        switch (itemId) {
            case R.id.nav_whatsnew :
                intent = new Intent(this, WhatsNewActivity.class);
                startActivity(intent);

                break;
            case R.id.nav_message :

                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.LEFT) ) {
            drawer.closeDrawer(Gravity.LEFT);
        }else if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else {

            if (doubleBackToExitPressedOnce) {

                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    /**
     * this will refresh profile
     */
    public void refreshDo() {
        progressDialog = ProgressDialog.show(MainActivity.this, "Loading", "Please wait ...", true);
        progressDialog.show();

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + session.getToken())
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

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        user = db.getUserByMemberCode(session.getMemberCode());

        EditProfileRequest request  = new EditProfileRequest(user.getFirstName(), user.getLastName(), user.getDob(), user.getEmail(), user.getPhone(), Integer.valueOf(user.getCityId()), "", "");
        Call<EditProfileResponse> call = service.editProfile(request, session.getMemberCode());
        call.enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response) {

                    if (response.raw().isSuccessful()) {
                        User user = response.body().getUser();
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                        db.createUser(user);

                        Log.i("aldieemaulana", "aldieemaulana db: " + response.body().getMessage());
                        setDataUser();


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
                        App.forceLogout(getApplicationContext());
                    }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Edit Profile is failed, please try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });


    }

    /**
     * this will reset profile points
     */
    public void resetDo() {
        progressDialog = ProgressDialog.show(this, "Reset", "Please wait ...", true);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + session.getToken())
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

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        user = db.getUserByMemberCode(session.getMemberCode());

        EditProfileRequest request  = new EditProfileRequest(user.getFirstName(), user.getLastName(), user.getDob(), user.getEmail(), user.getPhone(), Integer.valueOf(user.getCityId()), "", "");
        Call<EditProfileResponse> call = service.resetProfile(session.getMemberCode());
        call.enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response) {
                if (response.raw().isSuccessful()) {
                    User user = response.body().getUser();
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                    DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                    db.createUser(user);

                    Log.i("aldieemaulana", "aldieemaulana db: " + user.getPoint());
                    Log.i("aldieemaulana", "aldieemaulana db: " + user.getStatus());
                    setDataUser();

                    Toast.makeText(getApplicationContext(), "Resetting is success.", Toast.LENGTH_LONG).show();
                } else if (response.code() == 400) {
                    try {
                        Gson gson = new GsonBuilder().create();
                        EditProfileResponseError mError = gson.fromJson(response.errorBody().string(), EditProfileResponseError.class);
                        Toast.makeText(getApplicationContext(), mError.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        // handle failure to read error
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Reset Profile is failed", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Reset Profile is failed, please try again", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
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

    /**
     * this will return device id of android phone
     * @return id = devicenumber
     */
    private String getDeviceNumber(){
        String id = Settings.Secure.getString(MainActivity.this.getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return id;
    }

    private void checkMessageAndVoucher(){
        if (session.getCountMessage()>0 || session.getCountVoucher()>0){
            int count = session.getCountVoucher()+session.getCountMessage();
            countMessage.setText(""+count);
            countMessage.setVisibility(View.VISIBLE);

            MenuItem message = leftNavigationView.getMenu().findItem(R.id.nav_message);
            message.setTitle(Html.fromHtml("<font color='#026132'>Message ("+count+")</font>"));
            message.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_message_green_outline_24_dp,null));
        }else {
            MenuItem message = leftNavigationView.getMenu().findItem(R.id.nav_message);
            message.setTitle("Message");
            countMessage.setText("");
            countMessage.setVisibility(View.GONE);
        }
        countVoucher.setVisibility(View.GONE);
        Log.e(App.LOG,"HEI SUSHITEI JUMLAH BADGESS :  "+(session.getCountMessage()+session.getCountVoucher()));

    }

    private class FirebaseBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            int state = extras.getInt("isVoucher",0);
            int count = (session.getCountVoucher()+session.getCountMessage());
            if(messageTypeOpen)refreshMessage();
            checkMessageAndVoucher();
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            Log.e(App.LOG,"broadcast state from firebase "+state);
            Log.e(App.LOG,"broadcast firebase jumlah badge "+count);
        }
    }
}