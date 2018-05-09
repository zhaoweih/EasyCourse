package com.zhaoweihao.architechturesample.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.data.ZhihuDailyNews;
import com.zhaoweihao.architechturesample.data.ZhihuDailyNewsQuestion;
import com.zhaoweihao.architechturesample.data.source.datasource.ZhihuDailyNewsDataSource;
import com.zhaoweihao.architechturesample.retrofit.RetrofitService;
import com.zhaoweihao.architechturesample.util.DateFormatUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ZhihuDailyNewsRemoteDataSource implements ZhihuDailyNewsDataSource {

    @Nullable
    private static ZhihuDailyNewsRemoteDataSource INSTANCE = null;

    private ZhihuDailyNewsRemoteDataSource() {

    }

    public static ZhihuDailyNewsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ZhihuDailyNewsRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getZhihuDailyNews(boolean forceUpdate, boolean clearCache, long date, @NonNull LoadZhihuDailyNewsCallback callback) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.ZHIHU_DAILY_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService.ZhihuDailyService service = retrofit.create(RetrofitService.ZhihuDailyService.class);

        service.getZhihuList(DateFormatUtil.formatZhihuDailyDateLongToString(date))
                .enqueue(new Callback<ZhihuDailyNews>() {
                    @Override
                    public void onResponse(Call<ZhihuDailyNews> call, Response<ZhihuDailyNews> response) {
                        long timestamp = DateFormatUtil.formatZhihuDailyDateStringToLong(response.body().getDate());

                        Log.d("TAG", "onResponse: timestamp " + timestamp);

                        for (ZhihuDailyNewsQuestion item : response.body().getStories()) {
                            item.setTimestamp(timestamp);
                        }
                        callback.onNewsLoaded(response.body().getStories());
                    }

                    @Override
                    public void onFailure(Call<ZhihuDailyNews> call, Throwable t) {
                        callback.onDataNotAvailable();
                    }
                });
    }

    @Override
    public void getFavorites(@NonNull LoadZhihuDailyNewsCallback callback) {

    }

    @Override
    public void getItem(int itemId, @NonNull GetNewsItemCallback callback) {

    }

    @Override
    public void favoriteItem(int itemId, boolean favorite) {

    }

    @Override
    public void saveAll(@NonNull List<ZhihuDailyNewsQuestion> list) {

    }
}
