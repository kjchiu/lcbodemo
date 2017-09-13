package com.kjchiu.lcbodemo.api.service;

import com.fasterxml.jackson.annotation.JsonCreator;

public class PageInfo {

    private int recordsPerPage;
    private int totalRecordCount;
    private boolean isFirstPage;
    private boolean isFinalPage;
    private int page;

    @JsonCreator
    public PageInfo(int recordsPerPage, int totalRecordCount, boolean isFirstPage, boolean isFinalPage, int currentPage) {
        this.recordsPerPage = recordsPerPage;
        this.totalRecordCount = totalRecordCount;
        this.isFirstPage = isFirstPage;
        this.isFinalPage = isFinalPage;
        this.page = currentPage;
    }


    public int getRecordsPerPage() {
        return recordsPerPage;
    }

    public int getTotalRecordCount() {
        return totalRecordCount;
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public boolean isFinalPage() {
        return isFinalPage;
    }

    public int getPage() {
        return page;
    }
}
