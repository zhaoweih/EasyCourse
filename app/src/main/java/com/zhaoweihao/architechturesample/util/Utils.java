package com.zhaoweihao.architechturesample.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;


public class Utils {
    public static void log(Class c,String msg) {
        Log.d(c.getSimpleName(),msg);
    }
    public static ProgressDialog createDialog(Context context) {
       return null;
    }
}
