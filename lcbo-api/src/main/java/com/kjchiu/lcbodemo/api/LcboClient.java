package com.kjchiu.lcbodemo.api;

import com.kjchiu.lcbodemo.api.service.GetById;
import com.kjchiu.lcbodemo.api.service.LcboService;
import com.kjchiu.lcbodemo.api.service.Paginated;
import com.kjchiu.lcbodemo.api.service.PaginatedQuery;
import com.kjchiu.lcbodemo.api.service.entity.Product;
import com.kjchiu.lcbodemo.api.service.entity.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class LcboClient {

    private static final Logger logger = LoggerFactory.getLogger(LcboClient.class);
    public static final int ITEMS_PER_PAGE = 8;

    LcboService service;

    public LcboClient(LcboService service) {
        this.service = service;
    }

    /**
     * Get store by id
     *
     * @param id store id
     * @return maybe store if exists
     */
    public Optional<Store> getStore(int id) {
        return findById(id, service::getStore);
    }

    /**
     * Get product by id
     *
     * @param id product id
     * @return maybe product if exists
     */
    public Optional<Product> getProduct(int id) {
        return findById(id, service::getProduct);
    }

    public Paginated<Product> findProducts(String query) {
        return findProducts(query, 1);
    }

    public Paginated<Product> findProducts(String query, int page) {
        return findByQuery(query, page, ITEMS_PER_PAGE, service::findProducts);
    }

    private <T> Optional<T> findById(int id, Function<Integer, Call<GetById<T>>> provider) {
        Call<GetById<T>> call = provider.apply(id);
        try {
            Response<GetById<T>> response = call.execute();
            if (!response.isSuccessful()) {
                logger.error("Failed to find entity {}: {}", id, response.errorBody().string());
                return Optional.empty();
            }

            GetById<T> body = response.body();
            return Optional.of(body.getItem());
        } catch (IOException e) {
            logger.error("Failed to find entity", e);
            return Optional.empty();
        }
    }

    /**
     * Find any items that match query string
     * Assumes results are paginated
     *
     * @param query    query string
     * @param page     page to return
     * @param perPage  items per page
     * @param provider lcbo service impl function
     * @param <T>      type of item being queried
     * @return list of matching items with pagination info
     */
    private <T> Paginated<T> findByQuery(String query, int page, int perPage, PaginatedQuery<T> provider) {
        Call<Paginated<T>> call = provider.query(query, page, perPage);
        try {
            Response<Paginated<T>> response = call.execute();
            if (!response.isSuccessful()) {
                logger.error("Failed to query {}: {}", query, response.errorBody().string());
                return new Paginated<>(response.code(), response.message());
            }

            return response.body();
        } catch (IOException e) {
            logger.error("Failed to query entities", e);
            return new Paginated(0, e.toString());
        }

    }

}
