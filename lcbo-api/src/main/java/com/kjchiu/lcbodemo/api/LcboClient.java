package com.kjchiu.lcbodemo.api;

import com.kjchiu.lcbodemo.api.service.GetById;
import com.kjchiu.lcbodemo.api.service.LcboService;
import com.kjchiu.lcbodemo.api.service.entity.Product;
import com.kjchiu.lcbodemo.api.service.entity.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class LcboClient {
    
    private static final Logger logger = LoggerFactory.getLogger(LcboClient.class);

    LcboService service;
    public LcboClient(LcboService service) {
        this.service = service;
    }

    /**
     * Get store by id
     * @param id store id
     * @return maybe store if exists
     */
    public Optional<Store> getStore(int id) {
        return findById(id, service::getStore);
    }

    /**
     * Get product by id
     * @param id product id
     * @return maybe product if exists
     */
    public Optional<Product> getProduct(int id) {
        return findById(id, service::getProduct);
    }

    public List<Product> findProduct(String query) {
    }

    private <T> Optional<T> findById(int id, Function<Integer, Call<GetById<T>>> provider) {
        Call<GetById<T>> call = provider.apply(id);
        try {
            Response<GetById<T>> response = call.execute();
            if (! response.isSuccessful()) {
                logger.error("Failed to get store", response.errorBody().string());
                return Optional.empty();
            }

            GetById<T> body = response.body();
            return Optional.of(body.getItem());
        } catch (IOException e) {
            logger.error("Failed to find store", e);
            return Optional.empty();
        }
    }

    private <T> List<T> findByQuery(String query, Function<String, Call<Find<T>>> provider) {

    }

}
