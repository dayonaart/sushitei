package com.cranium.sushiteiapps.api;

import com.cranium.sushiteiapps.model.request.AddWishlistMenuRequest;
import com.cranium.sushiteiapps.model.request.AddWishlistRequest;
import com.cranium.sushiteiapps.model.response.AddWishlistMenuResponse;
import com.cranium.sushiteiapps.model.response.WishlistDeleteMenu;
import com.cranium.sushiteiapps.model.response.Wishlists;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Dayona on 5/31/17.
 */

public interface WishlistApi {

    @GET("wishlists/all")
    @Headers({
            "Content-Type: application/json"
    })
    Call<Wishlists> wishlistAlls();

    @POST("wishlist/create")
    @Headers({
            "Content-Type: application/json"
    })
    Call<Wishlists> addWishlist(@Body AddWishlistRequest body);

    @PUT("wishlist/update/{id}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<Wishlists> updateWishlist(@Path("id") Integer id, @Body AddWishlistRequest body);

    @DELETE("wishlist-menu/delete-by-menu/{memberCode}/{menu}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<WishlistDeleteMenu> deleteWishlistMenu(@Path("memberCode") String memberCode, @Path("menu") Integer menu);

    @DELETE("wishlist/delete/{id}")
    @Headers({
            "Content-Type: application/json"
    })
    Call<WishlistDeleteMenu> deleteWishlist(@Path("id") String id);

    @POST("wishlist-menu/create")
    @Headers({
            "Content-Type: application/json"
    })
    Call<AddWishlistMenuResponse> addWishlistMenu(@Body AddWishlistMenuRequest body);


    @GET("wishlists")
    @Headers({
            "Content-Type: application/json"
    })
    Call<Wishlists> wishlists();
}
