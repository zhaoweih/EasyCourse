package com.zhaoweihao.architechturesample.util;

import android.content.Context;
import android.util.Log;

public class Utils {
    public static void log(Class c,String msg) {
        Log.d(c.getSimpleName(),msg);
    }
}
