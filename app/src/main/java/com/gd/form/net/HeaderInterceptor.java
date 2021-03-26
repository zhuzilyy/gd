package com.gd.form.net;


import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>类描述：请求头拦截器
 * <p>创建人：wh
 * <p>创建时间：2019/6/20
 */
public class HeaderInterceptor implements Interceptor {

    public HeaderInterceptor() {
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder.method(request.method(), request.body());
        builder.url(request.url());

        builder.addHeader("X-AILNG-App-Type","Android");
       // builder.addHeader("Content-Type","application/json");
        builder.addHeader("Content-Type","x-www-form-urlencoded");
        builder.addHeader("User-Agent","Android");
        return chain.proceed(builder.build());
    }

}
