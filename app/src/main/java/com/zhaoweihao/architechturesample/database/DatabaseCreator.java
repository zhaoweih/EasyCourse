package com.zhaoweihao.architechturesample.database;

import android.arch.persistence.room.Database;
import android.support.annotation.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class DatabaseCreator {

    @Nullable
    private static DatabaseCreator INSTANCE = null;

    private AppDatabase mDb;

    private final AtomicBoolean mInitiallizing
}
