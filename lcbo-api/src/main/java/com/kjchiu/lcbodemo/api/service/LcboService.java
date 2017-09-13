package com.kjchiu.lcbodemo.api.service;

import com.kjchiu.lcbodemo.api.service.entity.Product;
import com.kjchiu.lcbodemo.api.service.entity.Store;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LcboService {

    @GET("stores/{id}")
    Call<GetById<Store>> getStore(@Path("id") int id);

    @GET("products/{id}")
    Call<GetById<Product>> getProduct(@Path("id") int id);

    @GET("products")
    Call<Paginated<Product>> findProducts(@Query("q") String query);

    @GET("products")
    Call<Paginated<Product>> findProducts(@Query("q") String query, @Query("page") int page, @Query("per_page") int pageSize);
}
