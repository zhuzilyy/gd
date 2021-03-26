package com.gd.form.net;




import com.gd.form.app.App;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <p>类描述：动态修改baseUrl
 * <p>创建人：wh
 * <p>创建时间：2019/6/20
 */
public class ServiceGenerator {

    private static String BASE_URL = UrlConstant.getInstance().getServerUrl();


    public static <T> T createServiceFrom(Class<T> serviceClass) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .baseUrl(BASE_URL).build().create(serviceClass);
    }

    /**
     * OkHttpClient
     */
    private static OkHttpClient getClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(App.getInstance())));
        builder.addInterceptor(new HeaderInterceptor());
        return builder.build();
    }
}