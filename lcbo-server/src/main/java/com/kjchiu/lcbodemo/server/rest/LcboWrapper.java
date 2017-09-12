package com.kjchiu.lcbodemo.server.rest;

import com.kjchiu.lcbodemo.api.LcboClient;

public class LcboWrapper {

    protected final LcboClient client;

    public LcboWrapper(LcboClient client) {
        this.client= client;
    }
}
