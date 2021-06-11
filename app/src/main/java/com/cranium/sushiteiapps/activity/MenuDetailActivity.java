package com.cranium.sushiteiapps.activity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.adapter.WishlistSpinnerAdapter;
import com.cranium.sushiteiapps.api.WishlistApi;
import com.cranium.sushiteiapps.model.Menu;
import com.cranium.sushiteiapps.model.Wishlist;
import com.cranium.sushiteiapps.model.request.AddWishlistMenuRequest;
import com.cranium.sushiteiapps.model.request.AddWishlistRequest;
import com.cranium.sushiteiapps.model.response.AddWishlistMenuResponse;
import com.cranium.sushiteiapps.model.response.WishlistDeleteMenu;
import com.cranium.sushiteiapps.model.response.Wishlists;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.SessionManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

public class MenuDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.title_bar) TextView titleBar;

    @BindView(R.id.image) ImageView image;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.price) TextView price;
    @BindView(R.id.wishlist) ImageButton wisheslist;
    @BindView(R.id.wishlist_outline) ImageButton wishlist;

    @OnClick(R.id.image_back)
    public void doActionLeft(View view) {
        onBackPressed();
    }

    private Menu menu;
    public Retrofit retrofit;
    public SessionManager sessionManager;
    private DatabaseHelper db;
    public List<Wishlist> wishlistList;
    public Integer selectedWishlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);
        db = new DatabaseHelper(MenuDetailActivity.this);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                        .addHeader("Content-Type", "application/json")
                        .build();
                return chain.proceed(newRequest);
            }
        }).readTimeout(App.TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(App.TIMEOUT, TimeUnit.SECONDS).build();
        if (sessionManager.isLoggedIn()) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(App.API)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            getWishlistAll();
        } else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(App.API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        menu = (Menu) getIntent().getSerializableExtra("menu");

        titleBar.setText(menu.getShortName());
        Picasso.with(this).load(App.URL + "files/menu-images/" + menu.getImage()).into(image);
        name.setText(menu.getName());
        description.setText(menu.getDescription());
        price.setText(menu.getFormattedPrice());

        manageWishlisted();

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLoggedIn()) {
                    setPickWishlistDialogBox(menu.getId());
                } else {
                    Toast.makeText(MenuDetailActivity.this, "You must login", Toast.LENGTH_SHORT).show();
                }
            }
        });
        wisheslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLoggedIn()) {
                    deleteWishlistMenu(menu.getId());
                } else {
                    Toast.makeText(MenuDetailActivity.this, "You must login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
    * managing wishlist for toggle love icon
    * */
    private void manageWishlisted() {
        if (menu.getIsWishlist().equals(1)) {
            wishlist.setVisibility(View.GONE);
            wisheslist.setVisibility(View.VISIBLE);
        } else {
            wishlist.setVisibility(View.VISIBLE);
            wisheslist.setVisibility(View.GONE);
        }
    }

    /**
     * load wishlist data from API based on db resource
    * */
    private void getWishlistAll() {
        wishlistList = new ArrayList<>();
//        if (db.getAllWishlists().size()==0){
            WishlistApi service = retrofit.create(WishlistApi.class);
            Call<Wishlists> call = service.wishlistAlls();
            call.enqueue(new Callback<Wishlists>() {
                @Override
                public void onResponse(Call<Wishlists> call, Response<Wishlists> response) {
                    if (response.raw().isSuccessful()) {
                        wishlistList = response.body().getWishlists();
                        db.createWishlists(response.body());
                        Log.e("ERROR WISHLIST ",""+response.body().getStatus());
                    }
                }

                @Override
                public void onFailure(Call<Wishlists> call, Throwable t) {

                }
            });
//        }else{
//            wishlistList.clear();
//            wishlistList.addAll(db.getAllWishlists());
//        }
    }

    /**
     * showing dialog for pick wishlist, add and choose wishlist
     * @param menuId is selected menu id
    * */
    private void setPickWishlistDialogBox(final Integer menuId) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pick_wishlist_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final Spinner wishlistSpinner = (Spinner) dialog.findViewById(R.id.wishlist);
        final AutoCompleteTextView wishlistTextView = (AutoCompleteTextView) dialog.findViewById(R.id.add_wishlist_text);
        AppCompatButton wishlistButton = (AppCompatButton) dialog.findViewById(R.id.wishlist_button);
        AppCompatButton addButton = (AppCompatButton) dialog.findViewById(R.id.add_button);

        loadWishlistSpinner(wishlistSpinner, wishlistList, 0);

        wishlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (wishlistSpinner.getAdapter().getCount()==0) {
                    setMenuToWishlist(selectedWishlist, menuId);
                    Toast.makeText(getApplicationContext(),"Please select or add some wishlist first !",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }else if(wishlistSpinner.getAdapter().getCount()==0 && TextUtils.isEmpty(wishlistTextView.getText().toString()) ){
                    setMenuToWishlist(selectedWishlist, menuId);
                    dialog.dismiss();
                }else{
                    setMenuToWishlist(selectedWishlist, menuId);
                    dialog.dismiss();
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(wishlistTextView.getText().toString()) ) {
                    wishlistTextView.requestFocus();
                    return;
                } else {
                    setAddWishlist(wishlistSpinner, wishlistTextView.getText().toString(), menuId);
                    dialog.dismiss();
                    wishlistTextView.setText(null);
                }
            }
        });

        dialog.show();
    }

    /**
     * handlle response wishlist from user and store it to APi
    * */
    private void setAddWishlist(final Spinner spinner, String wishlistText, final int menuID) {
        WishlistApi service = retrofit.create(WishlistApi.class);

        AddWishlistRequest request = new AddWishlistRequest(wishlistText);
        Call<Wishlists> call = service.addWishlist(request);

        call.enqueue(new Callback<Wishlists>() {
            @Override
            public void onResponse(Call<Wishlists> call, Response<Wishlists> response) {
                if (response.raw().isSuccessful()) {
                    Toast.makeText(MenuDetailActivity.this, "Wishlist is successfully saved ", Toast.LENGTH_SHORT).show();
                    wishlistList = response.body().getWishlists();
                    loadWishlistSpinner(spinner, wishlistList, menuID);
                    setMenuToWishlist(selectedWishlist, menuID);
                    db.updateWishlist(menuID,1);
                }
            }

            @Override
            public void onFailure(Call<Wishlists> call, Throwable t) {
                Toast.makeText(MenuDetailActivity.this, "Wishlist is failed to save, \nplease check your network connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * this method is for set chosen menu to wishlist and store it to API
     * @param menuId is selected menu id
     * @param wishlistId is selected wishlist id
    * */
    private void setMenuToWishlist(Integer wishlistId, final Integer menuId) {
        WishlistApi service = retrofit.create(WishlistApi.class);

        AddWishlistMenuRequest request = new AddWishlistMenuRequest(wishlistId, menuId);
        Call<AddWishlistMenuResponse> call = service.addWishlistMenu(request);

        call.enqueue(new Callback<AddWishlistMenuResponse>() {
            @Override
            public void onResponse(Call<AddWishlistMenuResponse> call, Response<AddWishlistMenuResponse> response) {
                if (response.raw().isSuccessful()) {
                    menu.setIsWishlist(1);
                    db.updateWishlist(menuId,1);
                    manageWishlisted();
                    Toast.makeText(MenuDetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddWishlistMenuResponse> call, Throwable t) {
                Toast.makeText(MenuDetailActivity.this, "Wishlist is failed to save, \nplease check your network connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * this method is for load data from adapter spinner
     * @param menuID is selected menu id
     * @param wishlists is wishlist listdata
     * @param wishlistSpinner is spinner view
    * */
    private void loadWishlistSpinner(Spinner wishlistSpinner, final List<Wishlist> wishlists, final int menuID) {
        WishlistSpinnerAdapter wishlistSpinnerAdapter = new WishlistSpinnerAdapter(this, wishlists);
        wishlistSpinner.setAdapter(wishlistSpinnerAdapter);
        wishlistSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedWishlist = wishlists.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("cranium", "item nothing selected");
            }
        });
    }

    /**
     * this method is for deleting wishlist menu based on selected menu
     * @param menuId is selected menu id
    * */
    private void deleteWishlistMenu(final Integer menuId) {
        WishlistApi service = retrofit.create(WishlistApi.class);

        Call<WishlistDeleteMenu> call = service.deleteWishlistMenu(sessionManager.getMemberCode(), menuId);

        call.enqueue(new Callback<WishlistDeleteMenu>() {
            @Override
            public void onResponse(Call<WishlistDeleteMenu> call, Response<WishlistDeleteMenu> response) {
                if (response.raw().isSuccessful()) {
                    db.updateWishlist(menuId,0);
                    menu.setIsWishlist(0);
                    manageWishlisted();
                    Toast.makeText(MenuDetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WishlistDeleteMenu> call, Throwable t) {
                Toast.makeText(MenuDetailActivity.this, "Wishlist is failed to save, \nplease check your network connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
