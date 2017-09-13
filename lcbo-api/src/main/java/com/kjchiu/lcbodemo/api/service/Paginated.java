package com.kjchiu.lcbodemo.api.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Paginated<T> {
    private int status;
    private String message;


    private PageInfo pageInfo;
    private List<T> items;

    @JsonCreator
    public Paginated(
            int status,
            String message,
            @JsonProperty("pager") PageInfo pageInfo,
            @JsonProperty("result") List<T> items) {
        this.status = status;
        this.message = message;
        this.pageInfo = pageInfo;
        this.items = items;
    }

    public Paginated(int status, String message) {
        this.items = new ArrayList<>();
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public List<T> getItems() {
        return items;
    }
}
