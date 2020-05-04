package com.initsysctrl.omnidemo.utils;

import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * Created by quanlun on 2020/4/19.
 */
public class OkHttpUtils {
    public static OkHttpClient client = null;
    public static OkHttpClient getInstance(){
        synchronized (OkHttpUtils.class){
            if(client==null){
                init();
            }
        }

        return client;
    }
    public static void init() {
        client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .build();

    }
}
