package com.kjchiu.lcbodemo.api.service;

import com.kjchiu.lcbodemo.api.service.Paginated;
import retrofit2.Call;
import retrofit2.http.Query;

@FunctionalInterface
public interface PaginatedQuery<T> {

    Call<Paginated<T>> query(@Query("q") String query, @Query("page") int page, @Query("per_page") int pageSize);
}
