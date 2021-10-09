package com.kobe.light.api;

import com.google.gson.GsonBuilder;
import com.kobe.light.LightApplication;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <p>Class: com.medbit.android.product.dsp.app.api.RetrofitServiceManager</p>
 * <p>Description: </p>
 * <pre>
 *
 *  </pre>
 *
 * @author lujunjie
 * @date 2018/8/7/20:29.
 */
public class RetrofitServiceManager {

    private static final int DEFAULT_TIME_OUT = 120;//超时时间 5s
    private static final int DEFAULT_READ_TIME_OUT = 120;
    private static final int DEFAULT_WRITE_TIME_OUT = 120;
    private final Retrofit mRetrofit;

    private RetrofitServiceManager() {

        Authenticator authenticator = new Authenticator() {//当服务器返回的状态码为401时，会自动执行里面的代码，也就实现了自动刷新token
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
//                instance.getUploadToken()
                String token = LightApplication.getToken();
                return response.request().newBuilder()
                        .header("token", token)
                        .build();
            }
        };

//        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
//        //新建log拦截器
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//            @Override
//            public void log(String message) {
//                Log.d("ApiUrl", "--->" + message);
//            }
//        });
//        loggingInterceptor.setLevel(level);

        // 创建 OKHttpClient
//
//        Interceptor tokenInterceptor = new Interceptor() {//全局拦截器，往请求头部添加 token 字段，实现了全局添加 token
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request original = chain.request();
//                HttpUrl originalHttpUrl = original.url();
//                String token = LightApplication.getToken();
//                HttpUrl url = originalHttpUrl.newBuilder()
//                        .addQueryParameter("token", token)
//                        .build();
//
//                // Request customization: add request headers
//                Request.Builder requestBuilder = original.newBuilder()
//                        .header
//                        .url(url);
//
//                Request request = requestBuilder.build();
//                return chain.proceed(request);
//            }
//        };

        // init cookie manager
        CookieHandler cookieHandler = new CookieManager();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .header("Authorization", LightApplication.getToken())
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);

                    }
                })
//                .authenticator(authenticator)
//                .cookieJar(new JavaNetCookieJar(cookieHandler))
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIME_OUT, TimeUnit.SECONDS)


//                .addNetworkInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        final Request originalRequest = chain.request();
//                        Request newRequest;
//
//                        newRequest = originalRequest.newBuilder()
//                                .header("Content-Type","application/json")
//                                .build();
//                        return chain.proceed(newRequest);
//                    }
//                })
                .readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
                .build();


        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()
                ))
                .baseUrl(LightApi.FINAL_URL)
                .build();
    }

    private static class SingletonHolder {
        private static final RetrofitServiceManager INSTANCE = new RetrofitServiceManager();
    }

    /**
     * 获取RetrofitServiceManager
     *
     * @return
     */
    public static RetrofitServiceManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取对应的Service
     *
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }

}
