package com.huawei.movie.http;

import com.huawei.movie.MyApplication;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TokenHeaderInterceptor  implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        return null;
    }

    public OkHttpClient.Builder getClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.addInterceptor(chain -> {
            Request build = chain.request().newBuilder()
                    .addHeader("Authorization", MyApplication.getInstance().getToken())
                    .addHeader("Content-type","application/json;charset=UTF-8")
                    .build();
            return chain.proceed(build);
        });
        return builder;
    }
}
