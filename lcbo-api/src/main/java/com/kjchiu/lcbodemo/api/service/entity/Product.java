package com.kjchiu.lcbodemo.api.service.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Product {
    private int id;
    private String name;
    private String tags;
    private boolean isDiscontinued;
    private int priceInCents;
    private int regularPriceInCents;
    private String imageUrl;
    private String imageThumbUrl;

    @JsonCreator
    public Product(int id, String name, String tags, boolean isDiscontinued, int priceInCents, int regularPriceInCents, String imageUrl, String imageThumbUrl) {
        this.id = id;
        this.name = name;
        this.tags = tags;
        this.isDiscontinued = isDiscontinued;
        this.priceInCents = priceInCents;
        this.regularPriceInCents = regularPriceInCents;
        this.imageUrl = imageUrl;
        this.imageThumbUrl = imageThumbUrl;
    }

}
