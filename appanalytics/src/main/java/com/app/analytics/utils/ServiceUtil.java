package com.app.analytics.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceUtil {

    public Retrofit retrofitProductionHTTP(Context context) {

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient(context))
                .baseUrl(ApiConstants.BASE_PRODUCTION_URL_HTTP)
                .build();


    }

    public Retrofit retrofitFAS(Context context) {

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient(context))
                .baseUrl(ApiConstants.BASE_FABRIC_FAS_BASE_URL)
                .build();

    }


    public HttpLoggingInterceptor httpLoggingInterceptor() {
        /*HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.i(message);
            }
        });*/

        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }


    public Cache cache(File cacheFile) {
        return new Cache(cacheFile, AnalyticsAppConstants.cache_size);
    }

    public File cacheFile(Context context) {
        return new File(context.getCacheDir(), AnalyticsAppConstants.ok_http_cache);
    }

    public OkHttpClient.Builder okHttpClientBuilder(Context context) {

        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor())
                .cache(cache(cacheFile(context)))
                .connectTimeout(AnalyticsAppConstants.ok_http_timeout, TimeUnit.SECONDS)
                .writeTimeout(AnalyticsAppConstants.ok_http_timeout, TimeUnit.SECONDS)
                .readTimeout(AnalyticsAppConstants.ok_http_timeout, TimeUnit.SECONDS);
    }

    public OkHttpClient okHttpClient(Context context) {
        return okHttpClientBuilder(context).build();
    }

    public Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient().registerTypeAdapter(DateTime.class, new DateTimeConverter());
        return gsonBuilder.create();
    }

}
