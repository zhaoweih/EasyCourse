package com.zhaoweihao.architechturesample.https;

import android.content.Context;
import android.util.Log;

import com.zhaoweihao.architechturesample.util.Constant;

import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitManager {
    private static final String TAG = "RetrofitManager";

    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpCliente;

    public static Retrofit retrofit(Context context) {
        if (okHttpCliente == null) {
            okHttpCliente = setHttpClient(context);
        }
        Log.d(TAG, "Constant.BASE_URL = " + Constant.getBaseUrl());
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpCliente)
                .build();
        return retrofit;
    }

    public static OkHttpClient setHttpClient(Context context) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        /**
         *设置缓存，代码略
         */

        /**
         *  公共参数，代码略
         */

        /**
         * 设置头，代码略
         */

        /**
         * 设置cookie，代码略
         */

        /**
         * 设置超时和重连，代码略
         */

        if (Constant.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //设置 Debug Log 模式
            okHttpClientBuilder.addInterceptor(loggingInterceptor);
//                okHttpClientBuilder.addInterceptor(new RetryInterceptor());
        }
        okHttpClientBuilder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                //todo:生产包需要注释 78、79 行
//                .sslSocketFactory(createSSLSocketFactory())
//                .hostnameVerifier(new TrustAllHostnameVerifier())
                .build();
        //支持https
//            okHttpClientBuilder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory(context, new int[]{R.raw.ormlite_config}));
//            okHttpClientBuilder.sslSocketFactory(SSLSocketClient.createSSLSocketFactory(), new SSLSocketClient.MyTrustManager());
//            okHttpClientBuilder.sslSocketFactory(HttpsUtils.getTrusClient(null), new SSLSocketClient.MyTrustManager());

//            HttpsUtils.getTrusClient(okHttpClientBuilder, context);
////            SSLSocketClient.getSSLSocketFactory(okHttpClientBuilder, context, new int[]{R.raw.keystore});
//        HttpsUtils.SSLParams sslParams = null;
//        try {
//            sslParams = HttpsUtils.getSslSocketFactory(new InputStream[]{context.getResources().getAssets().open("keystore.p12")}, null, null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        okHttpClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);

        //发布到应用商店和官网需要注释
//        okHttpClientBuilder.hostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        OkHttpClient okHttpClient = okHttpClientBuilder.build();
        return okHttpClient;
    }

    public static RemoteService remoteService(Context context) {
        return retrofit(context).create(RemoteService.class);
    }

//    private static javax.net.ssl.SSLSocketFactory createSSLSocketFactory() {
//        javax.net.ssl.SSLSocketFactory ssfFactory = null;
//
//        try {
//            SSLContext sc = SSLContext.getInstance("TLS");
//            sc.init(null,  new TrustManager[] { new TrustAllCerts() }, new SecureRandom());
//
//            ssfFactory = sc.getSocketFactory();
//        } catch (Exception e) {
//        }
//
//        return ssfFactory;
//    }

}
