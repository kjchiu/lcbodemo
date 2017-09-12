package com.kjchiu.lcbodemo.api.service;

import com.kjchiu.lcbodemo.api.service.entity.Product;
import com.kjchiu.lcbodemo.api.service.entity.Store;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LcboService {

    @GET("stores/{id}")
    Call<GetById<Store>> getStore(@Path("id") int id);

    @GET("products/{id}")
    Call<GetById<Product>> getProduct(@Path("id") int id);
}
