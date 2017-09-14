package com.kjchiu.lcbodemo.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjchiu.lcbodemo.api.service.LcboService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Create instances of LcboClient
 */
public class LcboClientFactory {

    /**
     * Create new lcbo service client
     * @param key lcboapi.com auth token
     * @param mapper jackson object mapper
     * @return
     */
    public static LcboClient create(String key, ObjectMapper mapper) {

        if (null == mapper ){
            mapper = new ObjectMapper();
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new AuthInterceptor(key))
                .build();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://lcboapi.com/")
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();

        LcboService service = retrofit.create(LcboService.class);
        return new LcboClient(service);

    }
}
