package com.zhaoweihao.architechturesample.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zhaoweihao.architechturesample.data.ZhihuDailyNewsQuestion;
import com.zhaoweihao.architechturesample.data.source.datasource.ZhihuDailyNewsDataSource;
import com.zhaoweihao.architechturesample.database.AppDatabase;

import java.util.List;

public class ZhihuDailyNewsLocalDataSource implements ZhihuDailyNewsDataSource{

    @Nullable
    private static ZhihuDailyNewsDataSource INSTANCE = null;

    @Nullable
    private AppDatabase mDb = null;

    private ZhihuDailyNewsLocalDataSource(@NonNull Context context) {
        DatabaseCreator
    }


    @Override
    public void getZhihuDailyNews(boolean forceUpdate, boolean clearCache, long date, @NonNull LoadZhihuDailyNewsCallback callback) {

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
