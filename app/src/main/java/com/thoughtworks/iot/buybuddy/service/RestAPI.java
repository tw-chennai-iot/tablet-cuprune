package com.thoughtworks.iot.buybuddy.service;

import com.thoughtworks.iot.buybuddy.model.Cart;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestAPI{

    @POST("/cart")
    Call<Cart> createCart();

    @POST("/cart/{cart-id}")
    Call<Cart> addItemToCart(@Path("cart-id")String cartId, @Body() Map<String,String> tagDetails);

    @DELETE("/cart/{cart-id}/tag/{tag-id}")
    Call<Cart> deleteItemFromCart(@Path("cart-id")String cartId,@Path("tag-id")String tagId);

    @POST("/cart/{cart-id}/pay")
    Call<Cart> pay(@Path("cart-id")String cartId);

}
