package com.zhaoweihao.architechturesample.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.zhaoweihao.architechturesample.data.ZhihuDailyNewsQuestion;

@Database(entities = {
        ZhihuDailyNewsQuestion.class},
        version = 1)

public abstract class AppDatabase extends RoomDatabase{

    static final String DATABASE_NAME = "paper-plane_db";

    public abstract ZhihuDailyNewsDao zhihuDailyNewsDao();

}
