package com.cranium.sushiteiapps.activity;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.adapter.MenuCategoryListAdapter;
import com.cranium.sushiteiapps.api.MenuApi;
import com.cranium.sushiteiapps.api.PromoBannerApi;
import com.cranium.sushiteiapps.model.Menu;
import com.cranium.sushiteiapps.model.MenuCategory;
import com.cranium.sushiteiapps.model.PromoBanner;
import com.cranium.sushiteiapps.model.response.MenuCategories;
import com.cranium.sushiteiapps.model.response.PromoBanners;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dayona on 6/01/17.
 */
public class MenuCategoryListActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.title_bar) TextView titleBar;
    @BindView(R.id.promo_banner_slider) SliderLayout promoBannerSlider;
    @BindView(R.id.promo_banner_slider_indicator) PagerIndicator promoBannerSliderIndicator;
    @BindView(R.id.menu_category_recycler_view) RecyclerView menuRecyclerView;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    private Retrofit retrofit;
    private MenuCategory menuCategory;
    private MenuCategoryListAdapter menuListAdapter;
    private List<MenuCategory> menuList;
    private DatabaseHelper db;

    @OnClick(R.id.image_back)
    public void doActionLeft(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_category_list);
        ButterKnife.bind(this);

        retrofit = new Retrofit.Builder()
                .baseUrl(App.API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        db = new DatabaseHelper(getApplicationContext());

        menuList = new ArrayList<>();
        menuListAdapter = new MenuCategoryListAdapter(this, menuList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        menuRecyclerView.setLayoutManager(mLayoutManager);
        menuRecyclerView.setNestedScrollingEnabled(false);
        menuRecyclerView.setHasFixedSize(true);
        menuRecyclerView.setHorizontalScrollBarEnabled(true);
        menuRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(15), true));
        menuRecyclerView.setItemAnimator(new DefaultItemAnimator());
        menuRecyclerView.setAdapter(menuListAdapter);

        swipeContainer.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getMenus(1);
                getPromoSliders(1);
            }
        });

        getPromoSliders(0);
        getMenus(0);

    }

    /**
     * load menus from API or local db
     * @param type , is load type for get data (
     *             0 = load menus from local db,
     *             1 = load menus from API)
    * */
    private void getMenus(int type) {

        swipeContainer.setRefreshing(false);
        if (db.getAllMenuCategoryNew().size()==0 || type == 1){
            MenuApi service = retrofit.create(MenuApi.class);

            Call<MenuCategories> call = service.menuCategories();

            call.enqueue(new Callback<MenuCategories>() {
                @Override
                public void onResponse(Call<MenuCategories> call, Response<MenuCategories> response) {
                    if (response.raw().isSuccessful()) {
                        swipeContainer.setRefreshing(false);
                        menuList.clear();
                        menuList.addAll(response.body().getMenuCategories());
                        menuListAdapter.notifyDataSetChanged();
                        List<MenuCategory> listDb = db.getAllMenuCategoryNew();
                        List<MenuCategory> list = response.body().getMenuCategories();
                        List<MenuCategory> listNew = new ArrayList<>();

                        Log.e(App.LOG,"db : "+db.getAllMenuCategoryNew().size()+"  response : "+list.size());

                        if (db.getAllMenuCategoryNew().size()<list.size()){ // take data for adding to local db
                            for (int i =0; i<list.size();i++){
                                MenuCategory menuRes = list.get(i);
                                try{
                                    if ( db.getAllMenuCategoryNew().get(i)==null || !db.getAllMenuCategoryNew().get(i).getName().equals(menuRes.getName())){
                                        Log.e(App.LOG,"datesss menuss nda mirip dalam : created "+menuRes.getCreatedAt()
                                                +" updated "+menuRes.getUpdatedAt()+" name "+menuRes.getName());
                                        listNew.add(menuRes);
                                    }
                                }catch(IndexOutOfBoundsException e){
                                    e.printStackTrace();
                                    Log.e(App.LOG,"datesss menuss nda mirip catch : created "+menuRes.getCreatedAt()
                                            +" updated "+menuRes.getUpdatedAt()+" name "+menuRes.getName());
                                    listNew.add(menuRes);
                                }
                            }
                        }

                        if (db.getAllMenuCategoryNew().size()>list.size()){ // take data for delete to local db
                            List<MenuCategory> menuDb = db.getAllMenuCategoryNew();
                            for (int i =0; i<db.getAllMenuCategoryNew().size();i++){
                                MenuCategory menudb= menuDb.get(i);
                                for (int j =0; j<list.size();j++){
                                    MenuCategory menuRes= list.get(j);
                                    if (!menudb.getName().equals(menuRes.getName())){
                                        if (j==list.size()-1){
                                            listNew.add(menudb);
                                        }
                                    }else{
                                        Log.e(App.LOG,menudb+" "+menuRes);
                                        break;
                                    }
                                }
                            }
                        }

                        if (listNew.size() > 0) {
                            if (db.getAllMenuCategoryNew().size()<list.size()){
                                for (MenuCategory m :listNew){
                                    Log.e(App.LOG,"tersimpan ke db : "+m.getName()+" ");
                                    db.createMenuCategoryNew(m);
                                }
                            }else{
                                for (MenuCategory m :listNew){
                                    Log.e(App.LOG,"terdelete di db : "+m.getName()+" ");
                                    db.deleteSingleMenuCategory(m);
                                }
                            }

                        }
                    }

                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<MenuCategories> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else{
            swipeContainer.setRefreshing(false);
            menuList.clear();
            menuList.addAll(db.getAllMenuCategoryNew());
            menuListAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }
    }

    /**
     * load data promo slider from API or local db
     * @param type , is load type for get data (
     *             0 = load promo data from local db,
     *             1 = load promo data from API)
     * */
    private void getPromoSliders(int type) {
        if (db.getAllPromoBanners().size()==0 || type==1){
            PromoBannerApi service = retrofit.create(PromoBannerApi.class);

            Call<PromoBanners> call = service.promoBanners();

            call.enqueue(new Callback<PromoBanners>() {
                @Override
                public void onResponse(Call<PromoBanners> call, Response<PromoBanners> response) {

                    if (response.raw().isSuccessful()) {

                        if (response.body().getPromoBanners().equals(new ArrayList<PromoBanner>())) {
                            sliderException();
                        } else if (response.body().getPromoBanners().size() != db.getAllPromoBanners().size()) {

                            for (PromoBanner promoBanner : response.body().getPromoBanners()) {

                                DefaultSliderView textSliderView = new DefaultSliderView(MenuCategoryListActivity.this);

                                if (promoBanner.getImage() != null) {

                                    String image = App.URL + "/files/promo-banners/" + promoBanner.getImage();
                                    textSliderView
                                            .image(image)
                                            .setScaleType(BaseSliderView.ScaleType.Fit)
                                            .setOnSliderClickListener(MenuCategoryListActivity.this);

                                    promoBannerSlider.addSlider(textSliderView);
                                }

                            }
                            db.deleteAlLBanner();
                            db.createPromoBanner(response.body());
                        }

                        promoBannerSlider.setDuration(4000);
                        promoBannerSlider.setPresetTransformer(SliderLayout.Transformer.Stack);
                        promoBannerSlider.setCustomIndicator(promoBannerSliderIndicator);

                    } else {
                        sliderException();
                    }
                }

                @Override
                public void onFailure(Call<PromoBanners> call, Throwable t) {
                    sliderException();
                }
            });
        }else{
            if (db.getAllPromoBanners().equals(new ArrayList<PromoBanner>())) {
                sliderException();
            } else {

                for (PromoBanner promoBanner : db.getAllPromoBanners()) {

                    DefaultSliderView textSliderView = new DefaultSliderView(MenuCategoryListActivity.this);

                    if (promoBanner.getImage() != null) {

                        String image = App.URL + "/files/promo-banners/" + promoBanner.getImage();
                        textSliderView
                                .image(image)
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(MenuCategoryListActivity.this);

                        promoBannerSlider.addSlider(textSliderView);
                    }
                }
            }

            promoBannerSlider.setDuration(4000);
            promoBannerSlider.setPresetTransformer(SliderLayout.Transformer.Stack);
            promoBannerSlider.setCustomIndicator(promoBannerSliderIndicator);
        }


    }

    /**
     * exception for handling empty data and image promo slider
    * */
    private void sliderException() {
        DefaultSliderView textSliderView = new DefaultSliderView(MenuCategoryListActivity.this);

        textSliderView
                .image(R.drawable.image_720x405)
                .setScaleType(BaseSliderView.ScaleType.Fit)
                .setOnSliderClickListener(MenuCategoryListActivity.this);

        promoBannerSlider.addSlider(textSliderView);
        promoBannerSlider.setDuration(4000);
        promoBannerSlider.setPresetTransformer(SliderLayout.Transformer.Stack);
        promoBannerSlider.setCustomIndicator(promoBannerSliderIndicator);
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

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
