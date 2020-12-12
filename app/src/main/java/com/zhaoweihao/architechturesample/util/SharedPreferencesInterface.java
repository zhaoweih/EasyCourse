package com.zhaoweihao.architechturesample.util;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * SharedPref 有权限管理机制
 * ACTIVITY.使用MyActivity_MyPrefs命名共享preference
 * ACTIVITY_DEFAULT，默认权限MyActivity进行命名，Activity级别权限(可以通过activity.getPreferences()进行获取)
 * APPLICATION_DEFAULT,应用级别默认SharedPreference或者UNIQUE，使用MyPrefs命名。
 * 如果不增加value = SharedPref.Scope.UNIQUE  则其他activity不能共享数据
 */
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface SharedPreferencesInterface {

    @DefaultString("")
    String username();

    @DefaultString("")
    String password();


    int user_id();



}
