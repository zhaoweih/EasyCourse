package com.zhaoweihao.architechturesample.util;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {

    /**
     * 网络工具类
     * 提供发送get请求和post请求的静态方法
     */

    private static final String prefix = "http://zhaoweihao.com:9001/api/";

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void sendGetRequest(String address,okhttp3.Callback callback) {
        address = prefix + address;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void sendPostRequest(String address, String json, okhttp3.Callback callback) {
        address = prefix + address;
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

}
