package com.huawei.movie.http;

import com.huawei.movie.config.Api;
import poerty.jianjian.converter.gson.GsonConverterFactory;
import poetry.jianjia.JianJia;

public  class  RequestUtils {
    private static RequestService instance;

    public static RequestService getInstance() {
        if(instance == null){
            instance = new JianJia.Builder()
                    .baseUrl(Api.HOST)
                    .client(new TokenHeaderInterceptor().getClient().build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(RequestService.class);
        }
        return instance;
    }
}
