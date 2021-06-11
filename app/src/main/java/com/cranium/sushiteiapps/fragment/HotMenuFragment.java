package com.cranium.sushiteiapps.fragment;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.activity.MenuDetailActivity;
import com.cranium.sushiteiapps.adapter.HotMenuListAdapter;
import com.cranium.sushiteiapps.api.MenuApi;
import com.cranium.sushiteiapps.model.MenuCategory;
import com.cranium.sushiteiapps.model.response.HotMenus;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.github.florent37.fiftyshadesof.FiftyShadesOf;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dayona on 5/31/17.
 */

public class HotMenuFragment extends Fragment {

    @BindView(R.id.hot_menu_recycler_view) RecyclerView hotMenuRecyclerView;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    private List<MenuCategory> hotMenuList;
    private HotMenuListAdapter hotMenuListAdapter;

    private DatabaseHelper db;

    private Retrofit retrofit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_hot_menu, container, false);
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

        hotMenuList = new ArrayList<>();
        hotMenuListAdapter = new HotMenuListAdapter(getActivity(), hotMenuList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        hotMenuRecyclerView.setLayoutManager(mLayoutManager);
        hotMenuRecyclerView.setHorizontalScrollBarEnabled(true);
        hotMenuRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(15), true));
        hotMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());
        hotMenuRecyclerView.setAdapter(hotMenuListAdapter);

        swipeContainer.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHotMenus(1);
            }
        });

        getHotMenus(0);

        return view;
    }

    private void getHotMenus(int type) {
        swipeContainer.setRefreshing(true);
        if (db.getAllMenuCategory().size()==0 || type ==1){
            MenuApi service = retrofit.create(MenuApi.class);

            Call<HotMenus> call = service.hotMenus();

            call.enqueue(new Callback<HotMenus>() {
                @Override
                public void onResponse(Call<HotMenus> call, Response<HotMenus> response) {
                    if (response.raw().isSuccessful()) {
                        swipeContainer.setRefreshing(false);
                        hotMenuList.clear();
                        hotMenuList.addAll(response.body().getMenuCategories());
                        hotMenuListAdapter.notifyDataSetChanged();

                        db.deleteAlLHotMenu();
                        db.createHotMenu(response.body());
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<HotMenus> call, Throwable t) {
                    showDialog();
                    swipeContainer.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else{
            swipeContainer.setRefreshing(false);
            hotMenuList.clear();
            hotMenuList.addAll(db.getAllMenuCategory());
            hotMenuListAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
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
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void showDialog(){
        android.support.v7.app.AlertDialog.Builder mDialog = new android.support.v7.app.AlertDialog.Builder(getContext());
        mDialog.setMessage("Connection problem");
        mDialog.setPositiveButton("Retry",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                getHotMenus(1);
            }
        });
        mDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert = mDialog.create();
        alert.show();
    }

}
