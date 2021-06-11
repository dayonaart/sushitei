package com.cranium.sushiteiapps.adapter;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.activity.WishListActivity;
import com.cranium.sushiteiapps.activity.WishListEditActivity;
import com.cranium.sushiteiapps.api.WishlistApi;
import com.cranium.sushiteiapps.model.Wishlist;
import com.cranium.sushiteiapps.model.WishlistDetail;
import com.cranium.sushiteiapps.model.response.WishlistDeleteMenu;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dayona on 5/28/17.
 */

public class WishlistListAdapter extends RecyclerView.Adapter<WishlistListAdapter.MyViewHolder> {

    private WishListActivity mContext;
    private List<Wishlist> wishlistList;


    private WishlistListMenuAdapter menuListAdapter;
    private List<WishlistDetail> menuList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageButton editButton, deleteButton;
        public RecyclerView menuRecyclerView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title_text);
            editButton = (ImageButton) view.findViewById(R.id.leftAct);
            menuRecyclerView = (RecyclerView) view.findViewById(R.id.menu_recycler_view);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent intent;
                    intent = new Intent(mContext, WishListEditActivity.class);
                    Wishlist wishlist = wishlistList.get(getAdapterPosition());
                    intent.putExtra("wishlist", wishlist);
                    mContext.startActivity(intent);
                }
            });

            deleteButton = (ImageButton) view.findViewById(R.id.rightAct);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    deleteWishlist(MyViewHolder.this, wishlistList.get(getAdapterPosition()).getId());
                }
            });

        }
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = mContext.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public WishlistListAdapter(WishListActivity mContext, List<Wishlist> wishlistList) {
        this.mContext = mContext;
        this.wishlistList = wishlistList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wish_list_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Wishlist wishlist = wishlistList.get(position);
        holder.title.setText(wishlist.getName());

        menuList = new ArrayList<>();
        menuListAdapter = new WishlistListMenuAdapter(mContext, menuList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 2);
        holder.menuRecyclerView.setLayoutManager(mLayoutManager);
        holder.menuRecyclerView.setNestedScrollingEnabled(false);
        holder.menuRecyclerView.setHasFixedSize(true);
        holder.menuRecyclerView.setHorizontalScrollBarEnabled(true);
        holder.menuRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(15), true));
        holder.menuRecyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.menuRecyclerView.setAdapter(menuListAdapter);

        menuList.clear();
        menuList.addAll(wishlist.getWishlistDetails());
        menuListAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return wishlistList.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        wishlistList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Wishlist> list) {
        wishlistList.addAll(list);
        notifyDataSetChanged();
    }

    public void add(Wishlist wishlist) {
        wishlistList.add(wishlist);
        notifyDataSetChanged();
    }

    private void deleteWishlist(final MyViewHolder holder, final Integer menu) {

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

                Call<WishlistDeleteMenu> call = service.deleteWishlist(menu.toString());

                call.enqueue(new Callback<WishlistDeleteMenu>() {
                    @Override
                    public void onResponse(Call<WishlistDeleteMenu> call, Response<WishlistDeleteMenu> response) {
                        if (response.raw().isSuccessful()) {
                            Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
