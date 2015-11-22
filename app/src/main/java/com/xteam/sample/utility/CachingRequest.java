package com.xteam.sample.utility;

import android.content.Context;
import android.net.ConnectivityManager;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.xteam.sample.dagger.AppConf;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Amit on 10/21/15.
 */

/*
 This class will create a Cache file in user device.
 Span of cache is 1 hour (max_age=3600)
 Maximum amount of cache can be stored in device 30MB
 */
public class CachingRequest {

    private AppConf appConf;
    public static Context mContext = null;
    public static String baseAPIUrl = null;
    protected static RestAdapter mRestAdapter;
    public static String REQUEST_TYPE = "http";
    public static String CACHE = "Cache-Control";
    private static int RESPONSE_TIME = 30;
    private static long SIZE_OF_CACHE = 30 * 1024 * 1024; // 30 MB
    public static ConnectivityManager connectivityManager ;

    public CachingRequest(Context context, String baseAPIUrl, AppConf mAppConf){
        this.mContext = context;
        this.baseAPIUrl = baseAPIUrl;
        this.appConf = mAppConf;
    }

    public RestAdapter init(){

        Cache cache = null;
        cache = new Cache(new File(mContext.getCacheDir(), REQUEST_TYPE), SIZE_OF_CACHE);

        // Create OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setCache(cache);
        okHttpClient.setConnectTimeout(RESPONSE_TIME, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(RESPONSE_TIME, TimeUnit.SECONDS);

        // Add Cache-Control Interceptor
        okHttpClient.networkInterceptors().add(mCacheControlInterceptor);

        // Create Executor
        Executor executor = Executors.newCachedThreadPool();

        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(baseAPIUrl)
                .setExecutors(executor, executor)
                .setClient(new OkClient(okHttpClient))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new CustomConverter(mContext, this.appConf))
                .build();
        return mRestAdapter;
    }

    private static final Interceptor mCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            // Add Cache Control only for GET methods
            if (request.method().equals("GET")) {
                if (isNetworkAvaliable()) {
                    // 1 day
                    request.newBuilder()
                            .header(CACHE, "only-if-cached")
                            .build();
                } else {
                    request.newBuilder()
                            .header(CACHE, "public, max-stale=2419200")
                            .build();
                }
            }

            Response response = chain.proceed(request);

            // Re-write response CC header to force use of cache
            return response.newBuilder()
                    .header(CACHE, "public, max-age=3600") // 1 hour
                    .build();
        }
    };

    public static boolean isNetworkAvaliable(){
        connectivityManager =  (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }


}
