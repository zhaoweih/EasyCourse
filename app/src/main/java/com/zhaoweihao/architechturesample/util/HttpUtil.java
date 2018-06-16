package com.zhaoweihao.architechturesample.util;

import java.io.File;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {

    /**
     * 网络工具类
     * 提供发送get请求和post请求的静态方法
     */

    private static final String prefix = "http://coffee.zhaoweihao.com:9001/api/";

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
    public static void sendPostRequestWithFile(String address, File file, okhttp3.Callback callback) {
        address = prefix + address;
        OkHttpClient client = new OkHttpClient();
        MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");
        MultipartBody mb = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_JPEG, file)).build();
        Request request = new Request.Builder()
                .url(address)
                .post(mb)
                .build();
        client.newCall(request).enqueue(callback);
    }

}
