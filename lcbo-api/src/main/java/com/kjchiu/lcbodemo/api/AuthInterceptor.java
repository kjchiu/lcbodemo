package com.kjchiu.lcbodemo.api;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Inject lcbo-api authorization token
 */
public class AuthInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    String authorization;

    public AuthInterceptor(String key) {
        this.authorization = "Token " + key;
    }

    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request
                .newBuilder()
                .addHeader("Authorization", this.authorization)
                .build();

        return chain.proceed(request);
    }
}
