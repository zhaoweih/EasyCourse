package com.zhaoweihao.architechturesample.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Ken on 2017/8/9.
 */

public class ToastUtil {
    public static Toast mToast;
    public static Context context;
    public static void showToast(Context context,String text) {
        if(mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    public static void showShort(Context context,int resId){
        Toast.makeText(context,resId,Toast.LENGTH_SHORT).show();
    }


    public static void showLong(Context context,int resId){
        Toast.makeText(context,resId,Toast.LENGTH_LONG).show();
    }

    public static void showShort(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }


    public static void showLong(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_LONG).show();
    }
}
