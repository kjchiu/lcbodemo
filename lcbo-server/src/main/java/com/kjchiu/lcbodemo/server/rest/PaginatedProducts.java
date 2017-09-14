package com.kjchiu.lcbodemo.server.rest;

import com.kjchiu.lcbodemo.api.service.Paginated;
import com.kjchiu.lcbodemo.api.service.entity.Product;

import java.util.List;

public class PaginatedProducts extends Paginated<Product> {

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
