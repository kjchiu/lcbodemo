package com.kjchiu.lcbodemo.server.rest;

import com.kjchiu.lcbodemo.api.service.Paginated;
import com.kjchiu.lcbodemo.api.service.entity.Product;

import java.util.List;

/**
 * Augment generic paginated item list
 * to include product query history
 */
public class PaginatedProducts extends Paginated<Product> {

    /**
     * Product query history
     */
    private List<String> history;

    public PaginatedProducts(Paginated<Product> products, List<String> history) {
        super(
                products.getStatus(),
                products.getMessage(),
                products.getPageInfo(),
                products.getItems());
        this.history = history;

    }

    public List<String> getHistory() {
        return history;
    }
}
