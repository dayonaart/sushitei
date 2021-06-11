package com.cranium.sushiteiapps.activity;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.adapter.MenuListAdapter;
import com.cranium.sushiteiapps.api.MenuApi;
import com.cranium.sushiteiapps.api.WishlistApi;
import com.cranium.sushiteiapps.model.MenuCategory;
import com.cranium.sushiteiapps.model.Menu;
import com.cranium.sushiteiapps.model.MenuWishlist;
import com.cranium.sushiteiapps.model.Wishlist;
import com.cranium.sushiteiapps.model.response.HotMenusByCategory;
import com.cranium.sushiteiapps.model.response.Wishlists;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.SessionManager;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.github.florent37.fiftyshadesof.FiftyShadesOf;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import static java.security.AccessController.getContext;

/**
 * Created by Dayona on 6/01/17.
 */
public class MenuDetailListActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.title_bar) TextView titleBar;
    @BindView(R.id.menu_recycler_view) RecyclerView menuRecyclerView;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.search_edit) EditText searchEdit;
    @BindView(R.id.search_go_button) TextView searchGoButton;
    @BindView(R.id.parent_view) LinearLayout parentView;
    @BindView(R.id.image) ImageView imageView;

    private MenuCategory menuCategory;
    private MenuListAdapter menuListAdapter;
    private List<Menu> menuList;
    private List<MenuWishlist> menuWishlists;

    private DatabaseHelper db;

    public Retrofit retrofit;
    public SessionManager sessionManager;
    public List<Wishlist> wishlistList;
    public Integer selectedWishlist;
    public boolean isHotMenu;
    public String category;
    public int categoryId;

    @OnClick(R.id.image_back)
    public void doActionLeft(View view) {
        App.hideSoftKeyboard(MenuDetailListActivity.this);
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail_list);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        db = new DatabaseHelper(getBaseContext().getApplicationContext());

        menuCategory = (MenuCategory) getIntent().getSerializableExtra("menuCategory");
        isHotMenu = getIntent().getBooleanExtra("hotMenu", false);
        category = getIntent().getStringExtra("category");
        categoryId = getIntent().getIntExtra("categoryId",0);

        titleBar.setText(menuCategory.getShortName());
        String image = App.URL + "files/menu-categories/detail/" + menuCategory.getImage();
        Picasso.with(this).load(image).error(R.drawable.image_720x405).into(imageView);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                        .addHeader("Content-Type", "application/json")
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();
        if (sessionManager.isLoggedIn()) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(App.API)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            getWishlistAll(0);
        } else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(App.API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        menuList = new ArrayList<>();
        menuWishlists = new ArrayList<>();
        menuListAdapter = new MenuListAdapter(this, menuList,menuWishlists);

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
                getWishlistAll(1);
            }
        });

        getMenus(0);

        searchGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.hideSoftKeyboard(MenuDetailListActivity.this);
                menuList.clear();
                menuListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.VISIBLE);
                getMenus(2);
                getWishlistAll(1);
            }
        });

        parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.hideSoftKeyboard(MenuDetailListActivity.this);
            }
        });

        parentView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    App.hideSoftKeyboard(MenuDetailListActivity.this);
                }
            }
        });
    }

    public List<Wishlist> getWishlistList() {

        return wishlistList;
    }

    public void setWishlistList(List<Wishlist> list) {
        wishlistList = list;
    }

    private void getWishlistAll(int type) {
        wishlistList = new ArrayList<>();
        if (db.getAllWishlists().size()==0 || type==1){
            WishlistApi service = retrofit.create(WishlistApi.class);
            Call<Wishlists> call = service.wishlistAlls();
            call.enqueue(new Callback<Wishlists>() {
                @Override
                public void onResponse(Call<Wishlists> call, Response<Wishlists> response) {
                    if (response.raw().isSuccessful()) {
                        wishlistList = response.body().getWishlists();
                        db.createWishlists(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Wishlists> call, Throwable t) {

                }
            });
        }else{
            wishlistList.clear();
            wishlistList = db.getAllWishlists();
        }
    }

    private void getMenus(int type) {
        if (!isHotMenu){ //hot menu
            Log.e(App.LOG,"is hot Menu"+isHotMenu);
            if (db.getAllHotMenuDetailNew(categoryId).size()==0 || type==1){
                swipeContainer.setRefreshing(false);
                MenuApi service = retrofit.create(MenuApi.class);

                Call<HotMenusByCategory> call = service.menusByCategory(menuCategory.getId(), searchEdit.getText().toString());
                if(isHotMenu) {
                    call = service.hotMenusByCategory(menuCategory.getId(), searchEdit.getText().toString());
                }

                call.enqueue(new Callback<HotMenusByCategory>() {
                    @Override
                    public void onResponse(Call<HotMenusByCategory> call, Response<HotMenusByCategory> response) {

                        if (response.raw().isSuccessful()) {
                            swipeContainer.setRefreshing(false);
                            menuList.clear();
                            menuList.addAll(response.body().getMenus());
                            menuListAdapter.notifyDataSetChanged();
                            Log.e(App.LOG+" categori",categoryId+" ");

                            List<Menu> list = response.body().getMenus();
                            List<Menu> listNew = new ArrayList<>();

                            Log.e(App.LOG,"db : "+db.getAllHotMenuDetailNew(categoryId).size()+"  response : "+list.size());
                            
                            if (db.getAllHotMenuDetailNew(categoryId).size()<list.size()){ // take data for adding to local db
                                for (int i =0; i<list.size();i++){
                                    Menu menuRes = list.get(i);
                                    try{
                                        if (db.getAllHotMenuDetailNew(categoryId).get(i)==null || !db.getAllHotMenuDetailNew(categoryId).get(i).getName().equals(menuRes.getName())){
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

                            if (db.getAllHotMenuDetailNew(categoryId).size()>list.size()){ // take data for delete to local db
                                List<Menu> menuDb = db.getAllHotMenuDetailNew(categoryId);
                                for (int i =0; i<db.getAllHotMenuDetailNew(categoryId).size();i++){
                                    Menu menudb= menuDb.get(i);
                                    for (int j =0; j<list.size();j++){
                                        Menu menuRes= list.get(j);
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
                                if (db.getAllHotMenuDetailNew(categoryId).size()<list.size()){
                                    for (Menu m :listNew){
                                        Log.e(App.LOG,"tersimpan ke db : "+m.getName()+" ");
                                        db.createHotMenuDetailNew(m);
                                    }
                                }else{
                                    for (Menu m :listNew){
                                        Log.e(App.LOG,"terdelete di db : "+m.getName()+" ");
                                        db.deleteSingleHotMenuDetail(m);
                                    }
                                }

                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Menu is not found", Toast.LENGTH_LONG).show();
                        }

                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<HotMenusByCategory> call, Throwable t) {
                        swipeContainer.setRefreshing(false);
                        menuList.clear();
                        menuList.addAll(db.getAllHotMenuDetailNew(categoryId) );
                        menuListAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }else if (type==2){ // search menu
                swipeContainer.setRefreshing(false);
                MenuApi service = retrofit.create(MenuApi.class);

                Call<HotMenusByCategory> call = service.menusByCategory(menuCategory.getId(), searchEdit.getText().toString());
                if(isHotMenu) {
                    call = service.hotMenusByCategory(menuCategory.getId(), searchEdit.getText().toString());
                }

                call.enqueue(new Callback<HotMenusByCategory>() {
                    @Override
                    public void onResponse(Call<HotMenusByCategory> call, Response<HotMenusByCategory> response) {

                        if (response.raw().isSuccessful()) {
                            swipeContainer.setRefreshing(false);
                            menuList.clear();
                            menuList.addAll(response.body().getMenus());

                            Log.e(App.LOG,"masuk di sini !!!! hotmenu : "+isHotMenu+" size menulist"+menuList.size());
                            menuListAdapter.notifyDataSetChanged();
                            if (isHotMenu){
                                Log.e(App.LOG,"masuk di sini !!!! hotmenu : "+isHotMenu);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Menu is not found", Toast.LENGTH_LONG).show();
                        }

                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<HotMenusByCategory> call, Throwable t) {
                        swipeContainer.setRefreshing(false);
                        menuList.clear();
                        menuList.addAll(db.getAllMenuCategoryDetailNew(categoryId));
                        menuWishlists.addAll(db.getAllWishlistByCategory(category));
                        menuListAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }else{
                swipeContainer.setRefreshing(false);
                menuList.clear();
                menuList.addAll(db.getAllHotMenuDetailNew(categoryId) );
                menuListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        }else{
            Log.e(App.LOG,"is hot Menu"+isHotMenu);
            if (type==2){ // search menu
                swipeContainer.setRefreshing(false);
                MenuApi service = retrofit.create(MenuApi.class);

                Call<HotMenusByCategory> call = service.menusByCategory(menuCategory.getId(), searchEdit.getText().toString());
                if(isHotMenu) {
                    call = service.hotMenusByCategory(menuCategory.getId(), searchEdit.getText().toString());
                }

                call.enqueue(new Callback<HotMenusByCategory>() {
                    @Override
                    public void onResponse(Call<HotMenusByCategory> call, Response<HotMenusByCategory> response) {

                        if (response.raw().isSuccessful()) {
                            swipeContainer.setRefreshing(false);
                            menuList.clear();
                            menuList.addAll(response.body().getMenus());

                            Log.e(App.LOG,"masuk di sini !!!! hotmenu : "+isHotMenu+" size menulist"+menuList.size());
                            menuListAdapter.notifyDataSetChanged();
                            if (isHotMenu){
                                Log.e(App.LOG,"masuk di sini !!!! hotmenu : "+isHotMenu);

                            }


                        } else {
                            Toast.makeText(getApplicationContext(), "Menu is not found", Toast.LENGTH_LONG).show();
                        }

                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<HotMenusByCategory> call, Throwable t) {
                        swipeContainer.setRefreshing(false);
                        menuList.clear();
                        menuList.addAll(db.getAllMenuCategoryDetailNew(categoryId));
                        menuWishlists.addAll(db.getAllWishlistByCategory(category));
                        menuListAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }else if (db.getAllMenuCategoryDetailNew(categoryId).size()==0 || type==1){
                swipeContainer.setRefreshing(false);
                MenuApi service = retrofit.create(MenuApi.class);

                Call<HotMenusByCategory> call = service.menusByCategory(menuCategory.getId(), searchEdit.getText().toString());
                if(isHotMenu) {
                    call = service.hotMenusByCategory(menuCategory.getId(), searchEdit.getText().toString());
                }

                call.enqueue(new Callback<HotMenusByCategory>() {
                    @Override
                    public void onResponse(Call<HotMenusByCategory> call, Response<HotMenusByCategory> response) {

                        if (response.raw().isSuccessful()) {
                            swipeContainer.setRefreshing(false);
                            menuList.clear();
                            menuList.addAll(response.body().getMenus());

                            menuListAdapter.notifyDataSetChanged();

                            List<Menu> list = response.body().getMenus();
                            List<Menu> listNew = new ArrayList<>();

                            Log.e(App.LOG,"db : "+db.getAllMenuCategoryDetailNew(categoryId).size()+"  response : "+list.size());

                            if (db.getAllMenuCategoryDetailNew(categoryId).size()<list.size()){ // take data for adding to local db
                                for (int i =0; i<list.size();i++){
                                    Menu menuRes = list.get(i);
                                    try{
                                        if ( db.getAllMenuCategoryDetailNew(categoryId).get(i)==null || !db.getAllMenuCategoryDetailNew(categoryId).get(i).getName().equals(menuRes.getName())){
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

                            if (db.getAllMenuCategoryDetailNew(categoryId).size()>list.size()){ // take data for delete to local db
                                List<Menu> menuDb = db.getAllMenuCategoryDetailNew(categoryId);
                                for (int i =0; i<db.getAllMenuCategoryDetailNew(categoryId).size();i++){
                                    Menu menudb= menuDb.get(i);
                                    for (int j =0; j<list.size();j++){
                                        Menu menuRes= list.get(j);
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
                                if (db.getAllMenuCategoryDetailNew(categoryId).size()<list.size()){
                                    for (Menu m :listNew){
                                        Log.e(App.LOG,"tersimpan ke db : "+m.getName()+" ");
                                        db.createMenuCategoryDetailNew(m);
                                    }
                                }else{
                                    for (Menu m :listNew){
                                        Log.e(App.LOG,"terdelete di db : "+m.getName()+" ");
                                        db.deleteSingleMenuCategoryDetail(m);
                                    }
                                }
                            }

                            Log.e("categori",category);
                            for (Menu menu : menuList){
                                db.createWishlist(new MenuWishlist(menu.getMenuCategoryId(),menu.getId(),menu.getIsWishlist()));
                                Log.e("SURYA",menu.getId()+" "+menu.getIsWishlist());
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Menu is not found", Toast.LENGTH_LONG).show();
                        }

                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<HotMenusByCategory> call, Throwable t) {
                        swipeContainer.setRefreshing(false);
                        menuList.clear();
                        menuList.addAll(db.getAllMenuCategoryDetailNew(categoryId));
                        menuWishlists.addAll(db.getAllWishlistByCategory(category));
                        menuListAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
            else{
                swipeContainer.setRefreshing(false);
                menuList.clear();
                menuList.addAll(db.getAllMenuCategoryDetailNew(categoryId));
                menuWishlists.addAll(db.getAllWishlistByCategory(category));
                menuListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
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

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    protected void onResume() {
        getMenus(1);
        super.onResume();
        Log.e(App.LOG,"ONRESUME");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
