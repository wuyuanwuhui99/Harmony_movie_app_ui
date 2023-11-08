package com.huawei.movie.http;

import com.huawei.movie.config.Api;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;

public  class  RequestUtils {
    private static RequestService instance;

    public static RequestService getInstance() {
        if(instance == null)
            instance =  new Retrofit.Builder()
                .baseUrl(Api.HOST)
                .client(new TokenHeaderInterceptor().getClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestService.class);
        return instance;
    }
}
