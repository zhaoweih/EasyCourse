package com.zhaoweihao.architechturesample;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhaoweihao.architechturesample.util.SharedPreferencesInterface_;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.litepal.LitePalApplication;

import java.lang.ref.WeakReference;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by Ken on 2017/8/1.
 */
@EApplication
public class EasyCourseApplication extends LitePalApplication {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static boolean status;

    @App
    static EasyCourseApplication context;

    private static EasyCourseApplication mInstance;


    public static ArrayList<WeakReference<Activity>> activityStack = new ArrayList<WeakReference<Activity>>();


    public static synchronized EasyCourseApplication getInstance() {
        if (null == mInstance) {
            mInstance = new EasyCourseApplication();
        }
        return mInstance;
    }

    public void addActivity(Activity activity) {
        WeakReference<Activity> act = new WeakReference<Activity>(activity);
        if (activityStack.indexOf(act) == -1) activityStack.add(act);
    }

    public void removeActivity(Activity activity) {
        WeakReference<Activity> act = new WeakReference<Activity>(activity);
        activityStack.remove(act);
    }


    public void exit() {
        for (WeakReference<Activity> activity : activityStack) {
            if (activity != null && activity.get() != null) activity.get().finish();
        }
        activityStack.clear();
    }

    public static void setContext(EasyCourseApplication context) {
        EasyCourseApplication.context = context;
    }

    public static Context getContext() {
        return context;
    }

    private Locale locale;

    private String language;

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }


    @Override
    public void onCreate() {
        super.onCreate();
//        StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder1.build());
//        builder1.detectFileUriExposure();
        mInstance = this;
        context = this;


    }

    public static String bu0(String str) {
        if (str.length() == 1) {
            str = "0" + str;
        }
        return str;
    }

    private static Gson gson;

    public static Gson getGson() {
        return gson;
    }

    static {
        gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.PROTECTED) //@protected 修饰的过滤，比如自动增长的id
                .addSerializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return "serialVersionUID".equals(f.getName());
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();
    }

    @Pref
    SharedPreferencesInterface_ sharedPreferencesInterface_;

    public static SharedPreferencesInterface_ getSharedPreference() {
        return context.sharedPreferencesInterface_;
    }

}
