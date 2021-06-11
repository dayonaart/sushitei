package com.cranium.sushiteiapps.api;

import com.cranium.sushiteiapps.model.response.HotMenus;
import com.cranium.sushiteiapps.model.response.HotMenusByCategory;
import com.cranium.sushiteiapps.model.response.MenuCategories;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Dayona on 5/31/17.
 */

public interface MenuApi {

    @GET("hot-menu-categories")
    @Headers({
            "Content-Type: application/json"
    })
    Call<HotMenus> hotMenus();

    @GET("hot-menus/{categoryId}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<HotMenusByCategory> hotMenusByCategory(@Path("categoryId") Integer category, @Query("search") String search);

    @GET("menus/{categoryId}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<HotMenusByCategory> menusByCategory(@Path("categoryId") Integer category, @Query("search") String search);

    @GET("menu-categories")
    @Headers({
            "Content-Type: application/json"
    })
    Call<MenuCategories> menuCategories();

    @GET("menu-categories/{createdAt}/{updatedAt}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<MenuCategories> menuCategoriesNew(@Path("createdAt") String createdAt, @Path("updatedAt") String updatedAt);

}
