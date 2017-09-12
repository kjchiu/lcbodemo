package com.kjchiu.lcbodemo.api.service;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetById<T> {
    private int status;
    private String message;
    private T item;

    public GetById(int status, String message, @JsonProperty("result") T item) {
        this.status = status;
        this.message = message;
        this.item = item;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getItem() {
        return item;
    }
}
