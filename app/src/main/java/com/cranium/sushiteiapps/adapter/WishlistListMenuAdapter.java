package com.cranium.sushiteiapps.adapter;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.activity.WishListActivity;
import com.cranium.sushiteiapps.api.WishlistApi;
import com.cranium.sushiteiapps.model.WishlistDetail;
import com.cranium.sushiteiapps.model.response.WishlistDeleteMenu;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dayona on 5/31/17.
 */

public class WishlistListMenuAdapter extends RecyclerView.Adapter<WishlistListMenuAdapter.MyViewHolder> {

    private WishListActivity mContext;
    private List<WishlistDetail> menuList;
    DatabaseHelper db;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price;
        public ImageView image;
        public Button deleteButton;

        public MyViewHolder(View view) {
            super(view);

            db = new DatabaseHelper(mContext);

            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
            image = (ImageView) view.findViewById(R.id.image);

            deleteButton = (Button) view.findViewById(R.id.delete);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    deleteWishlist(MyViewHolder.this, menuList.get(getAdapterPosition()).getMenuId());
                }
            });
        }
    }

    public WishlistListMenuAdapter(WishListActivity mContext, List<WishlistDetail> menuList) {
        this.mContext = mContext;
        this.menuList = menuList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_wl_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        WishlistDetail menu = menuList.get(position);

        holder.name.setText(menu.getMenu().getName());
        holder.price.setText(menu.getMenu().getFormattedPrice());
        if (menu.getMenu().getImage() != null) {
            String image = App.URL + "files/menu-images/" + menu.getMenu().getImage();
            Picasso.with(mContext).load(image).into(holder.image);
        } else {
            Picasso.with(mContext).load(R.drawable.image_315x315).into(holder.image);
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
    public void addAll(List<WishlistDetail> menuList) {
        menuList.addAll(menuList);
        notifyDataSetChanged();
    }

    public void add(WishlistDetail menu) {
        menuList.add(menu);
        notifyDataSetChanged();
    }

    private void deleteWishlist(final MyViewHolder holder, final String menu) {

        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_wishlist_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageButton wishlistButton = (ImageButton) dialog.findViewById(R.id.close);
        wishlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        AppCompatButton wishlistButtonC = (AppCompatButton) dialog.findViewById(R.id.cancel_button);
        wishlistButtonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        AppCompatButton wishlistButtonY = (AppCompatButton) dialog.findViewById(R.id.delete_button);
        wishlistButtonY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WishlistApi service = mContext.retrofit.create(WishlistApi.class);

                Call<WishlistDeleteMenu> call = service.deleteWishlistMenu(mContext.sessionManager.getMemberCode(), (Integer.valueOf(menu)));

                call.enqueue(new Callback<WishlistDeleteMenu>() {
                    @Override
                    public void onResponse(Call<WishlistDeleteMenu> call, Response<WishlistDeleteMenu> response) {
                        if (response.raw().isSuccessful()) {
                            db.updateWishlist((Integer.valueOf(menu)),0);
                            Toast.makeText(mContext, response.body().getMessage()+" "+menu, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            mContext.recreate();
                        }
                    }

                    @Override
                    public void onFailure(Call<WishlistDeleteMenu> call, Throwable t) {

                    }
                });
            }
        });


        dialog.show();

    }


}
