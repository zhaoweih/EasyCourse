package com.zhaoweihao.architechturesample.data.source.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zhaoweihao.architechturesample.data.ZhihuDailyNewsQuestion;
import com.zhaoweihao.architechturesample.data.source.datasource.ZhihuDailyNewsDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ZhihuDailyNewsRepository implements ZhihuDailyNewsDataSource {

    @Nullable
    private static ZhihuDailyNewsRepository INSTANCE = null;

    @NonNull
    private final ZhihuDailyNewsDataSource mLocalDataSource;

    @NonNull
    private final ZhihuDailyNewsDataSource mRemoteDataSource;

    private Map<Integer, ZhihuDailyNewsQuestion> mCachedItems;

    private ZhihuDailyNewsRepository(@NonNull ZhihuDailyNewsDataSource remoteDataSource,
                                     @NonNull ZhihuDailyNewsDataSource localDataSource) {
        this.mLocalDataSource = localDataSource;
        this.mRemoteDataSource = remoteDataSource;
    }

    public static ZhihuDailyNewsRepository getInstance(@NonNull ZhihuDailyNewsDataSource remoteDataSource,
                                                       @NonNull ZhihuDailyNewsDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ZhihuDailyNewsRepository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public void getZhihuDailyNews(boolean forceUpdate, boolean clearCache, long date, @NonNull LoadZhihuDailyNewsCallback callback) {

        if (mCachedItems != null && !forceUpdate) {
            callback.onNewsLoaded(new ArrayList<>(mCachedItems.values()));
            return;
        }

        //先从网络上获得数据
        mRemoteDataSource.getZhihuDailyNews(false, clearCache, date, new LoadZhihuDailyNewsCallback() {
            @Override
            public void onNewsLoaded(@NonNull List<ZhihuDailyNewsQuestion> list) {
                refreshCache(clearCache, list);
                callback.onNewsLoaded(new ArrayList<>(mCachedItems.values()));
                saveAll(list);
            }

            @Override
            public void onDataNotAvailable() {
                mLocalDataSource.getZhihuDailyNews(false, false, date, new LoadZhihuDailyNewsCallback() {
                    @Override
                    public void onNewsLoaded(@NonNull List<ZhihuDailyNewsQuestion> list) {
                        refreshCache(clearCache, list);
                        callback.onNewsLoaded(new ArrayList<>(mCachedItems.values()));
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
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
        mLocalDataSource.saveAll(list);
        mRemoteDataSource.saveAll(list);

        if (mCachedItems == null) {
            mCachedItems = new LinkedHashMap<>();
        }

        for (ZhihuDailyNewsQuestion item : list) {
            mCachedItems.put(item.getId(), item);
        }
    }

    private void refreshCache(boolean clearCache, List<ZhihuDailyNewsQuestion> list) {

        if (mCachedItems == null) {
            mCachedItems = new LinkedHashMap<>();
        }
        if (clearCache) {
            mCachedItems.clear();
        }
        for (ZhihuDailyNewsQuestion item : list) {
            mCachedItems.put(item.getId(), item);
        }
    }
}
