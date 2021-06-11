package com.cranium.sushiteiapps.adapter;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.activity.MenuDetailActivity;
import com.cranium.sushiteiapps.activity.MenuDetailListActivity;
import com.cranium.sushiteiapps.api.WishlistApi;
import com.cranium.sushiteiapps.model.Menu;
import com.cranium.sushiteiapps.model.MenuCategory;
import com.cranium.sushiteiapps.model.MenuWishlist;
import com.cranium.sushiteiapps.model.Wishlist;
import com.cranium.sushiteiapps.model.request.AddWishlistMenuRequest;
import com.cranium.sushiteiapps.model.request.AddWishlistRequest;
import com.cranium.sushiteiapps.model.response.AddWishlistMenuResponse;
import com.cranium.sushiteiapps.model.response.WishlistDeleteMenu;
import com.cranium.sushiteiapps.model.response.Wishlists;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.SessionManager;
import com.github.florent37.fiftyshadesof.FiftyShadesOf;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

/**
 * Created by Dayona on 5/31/17.
 */

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MyViewHolder> {

    private MenuDetailListActivity mContext;
    private List<Menu> menuList;
    private List<MenuWishlist> menuWishlists;

    DatabaseHelper db;
    SessionManager sessionManager;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image, wishlist, wishlistOutline;
        public TextView name, price;

        public MyViewHolder(View view) {
            super(view);

            db = new DatabaseHelper(mContext);
            sessionManager = new SessionManager(mContext);

            image = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
            wishlist = (ImageView) view.findViewById(R.id.wishlist);
            wishlistOutline = (ImageView) view.findViewById(R.id.wishlist_outline);

            wishlistOutline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext.sessionManager.isLoggedIn()) {
                        setPickWishlistDialogBox(MyViewHolder.this, menuList.get(getAdapterPosition()).getId());
                    } else {
                        Toast.makeText(mContext, "You must login", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            wishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext.sessionManager.isLoggedIn()) {
                        deleteWishlistMenu(MyViewHolder.this, menuList.get(getAdapterPosition()).getId());
                    } else {
                        Toast.makeText(mContext, "You must login", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentToMenuDetail();
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentToMenuDetail();
                }
            });
        }

        private void intentToMenuDetail() {
            Intent intent;
            intent = new Intent(mContext, MenuDetailActivity.class);
            Menu menuCategory = menuList.get(getAdapterPosition());
            intent.putExtra("menu", menuCategory);
            mContext.startActivity(intent);
        }
    }

    public MenuListAdapter(MenuDetailListActivity mContext, List<Menu> menuList,List<MenuWishlist> menuWishlists) {
        this.mContext = mContext;
        this.menuList = menuList;
        this.menuWishlists = menuWishlists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Menu menu = menuList.get(position);

        holder.name.setText(menu.getShortName());
        holder.price.setText(menu.getFormattedPrice());
        String image = App.URL + "files/menu-images/" + menu.getImage();
        Picasso.with(mContext).load(image).error(R.drawable.image_315x315).into(holder.image);
        MenuWishlist menuWishlist =  db.getWishlist(menu.getId());
        if (menuWishlist != null){
            if (menuWishlist.getIsWishlist()!=null){
                if (menuWishlist.getIsWishlist()==0)
                    manageWishlisted(holder,false);
                else if (menuWishlist.getIsWishlist()==1)
                    manageWishlisted(holder,true);
            }else
                manageWishlisted(holder, menu.getIsWishlist().equals(1));
        }else{
            manageWishlisted(holder, menu.getIsWishlist().equals(1));
        }

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        menuList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Menu> menuList) {
        menuList.addAll(menuList);
        notifyDataSetChanged();
    }

    public void add(Menu menu) {
        menuList.add(menu);
        notifyDataSetChanged();
    }

    private void manageWishlisted(MyViewHolder holder, boolean trues) {
        if (trues) {
            holder.wishlistOutline.setVisibility(View.GONE);
            holder.wishlist.setVisibility(View.VISIBLE);
        } else {
            holder.wishlistOutline.setVisibility(View.VISIBLE);
            holder.wishlist.setVisibility(View.GONE);
        }
    }

    private void setPickWishlistDialogBox(final MyViewHolder holder, final Integer menuId) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pick_wishlist_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final Spinner wishlistSpinner = (Spinner) dialog.findViewById(R.id.wishlist);
        final AutoCompleteTextView wishlistTextView = (AutoCompleteTextView) dialog.findViewById(R.id.add_wishlist_text);
        AppCompatButton wishlistButton = (AppCompatButton) dialog.findViewById(R.id.wishlist_button);
        AppCompatButton addButton = (AppCompatButton) dialog.findViewById(R.id.add_button);

        loadWishlistSpinner(wishlistSpinner, mContext.getWishlistList());

        wishlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wishlistSpinner.getAdapter().getCount()==0) {
                    setMenuToWishlist(holder, mContext.selectedWishlist, menuId);
                    Toast.makeText(mContext,"Please select or add some wishlist first !",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }else if(wishlistSpinner.getAdapter().getCount()==0 && TextUtils.isEmpty(wishlistTextView.getText().toString()) ){
                    setMenuToWishlist(holder, mContext.selectedWishlist, menuId);
                    dialog.dismiss();
                }else{
                    setMenuToWishlist(holder, mContext.selectedWishlist, menuId);
                    dialog.dismiss();
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(wishlistTextView.getText().toString())) {
                    wishlistTextView.requestFocus();
                    return;
                } else {
                    setAddWishlist(holder, wishlistSpinner, wishlistTextView.getText().toString(), menuId);
                    wishlistTextView.setText(null);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void setAddWishlist(final MyViewHolder holder, final Spinner spinner, String wishlistText, final int menuID) {
        WishlistApi service = mContext.retrofit.create(WishlistApi.class);

        AddWishlistRequest request = new AddWishlistRequest(wishlistText);
        Call<Wishlists> call = service.addWishlist(request);

        call.enqueue(new Callback<Wishlists>() {
            @Override
            public void onResponse(Call<Wishlists> call, Response<Wishlists> response) {
                if (response.raw().isSuccessful()) {
                    mContext.setWishlistList(response.body().getWishlists());
                    loadWishlistSpinner(spinner, response.body().getWishlists());

                    WishlistApi service = mContext.retrofit.create(WishlistApi.class);
                    mContext.selectedWishlist = response.body().getWishlists().get(0).getId();

                    AddWishlistMenuRequest request = new AddWishlistMenuRequest(mContext.selectedWishlist, menuID);
                    Call<AddWishlistMenuResponse> calle = service.addWishlistMenu(request);

                    calle.enqueue(new Callback<AddWishlistMenuResponse>() {
                        @Override
                        public void onResponse(Call<AddWishlistMenuResponse> call, Response<AddWishlistMenuResponse> response) {
                            if (response.raw().isSuccessful()) {
                                Toast.makeText(mContext, "Wishlist is successfully saved", Toast.LENGTH_SHORT).show();
                                menuList.get(holder.getAdapterPosition()).setIsWishlist(1);
                                manageWishlisted(holder, true);
                            }else{
                                Log.e(App.LOG," wishlist dalam");
                            }
                        }

                        @Override
                        public void onFailure(Call<AddWishlistMenuResponse> call, Throwable t) {
                            Toast.makeText(mContext, "Wishlist is failed to save, \nplease check your network connection", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    Log.e(App.LOG," wishlist luar");
                }
            }

            @Override
            public void onFailure(Call<Wishlists> call, Throwable t) {
                Toast.makeText(mContext, "Wishlist is failed to save, \nplease check your network connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMenuToWishlist(final MyViewHolder holder, Integer wishlistId, final Integer menuId) {
        WishlistApi service = mContext.retrofit.create(WishlistApi.class);

        AddWishlistMenuRequest request = new AddWishlistMenuRequest(wishlistId, menuId);
        Call<AddWishlistMenuResponse> call = service.addWishlistMenu(request);

        call.enqueue(new Callback<AddWishlistMenuResponse>() {
            @Override
            public void onResponse(Call<AddWishlistMenuResponse> call, Response<AddWishlistMenuResponse> response) {
                if (response.raw().isSuccessful()) {
                    Toast.makeText(mContext, "Wishlist is successfully saved", Toast.LENGTH_SHORT).show();
                    menuList.get(holder.getAdapterPosition()).setIsWishlist(1);
                    manageWishlisted(holder, true);
                    db.updateWishlist(menuId,1);
                }
            }
            @Override
            public void onFailure(Call<AddWishlistMenuResponse> call, Throwable t) {
                Toast.makeText(mContext, "Wishlist is failed to save, \nplease check your network connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadWishlistSpinner(Spinner wishlistSpinner, final List<Wishlist> wishlists) {
        WishlistSpinnerAdapter wishlistSpinnerAdapter = new WishlistSpinnerAdapter(mContext, wishlists);
        wishlistSpinner.setAdapter(wishlistSpinnerAdapter);
        wishlistSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mContext.selectedWishlist = wishlists.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("cranium", "item nothing selected");
            }
        });
    }

    private void deleteWishlistMenu(final MyViewHolder holder,final Integer menu) {

        WishlistApi service = mContext.retrofit.create(WishlistApi.class);

        Call<WishlistDeleteMenu> call = service.deleteWishlistMenu(mContext.sessionManager.getMemberCode(), menu);

        call.enqueue(new Callback<WishlistDeleteMenu>() {
            @Override
            public void onResponse(Call<WishlistDeleteMenu> call, Response<WishlistDeleteMenu> response) {
                if (response.raw().isSuccessful()) {
                    menuList.get(holder.getAdapterPosition()).setIsWishlist(0);
                    manageWishlisted(holder, false);
                    db.updateWishlist(menu,0);
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WishlistDeleteMenu> call, Throwable t) {
                Toast.makeText(mContext, "Wishlist is failed to save, \nplease check your network connection", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
