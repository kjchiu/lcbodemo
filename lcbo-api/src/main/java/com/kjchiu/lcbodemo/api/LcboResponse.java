package com.kjchiu.lcbodemo.api;

public class LcboResponse<T> {

    int status;

    String message;

    T result;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getResult() {
        return result;
    }
}
