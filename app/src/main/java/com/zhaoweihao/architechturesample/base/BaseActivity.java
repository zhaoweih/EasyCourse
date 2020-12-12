package com.zhaoweihao.architechturesample.base;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhaoweihao.architechturesample.util.SharedPreferencesInterface_;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.sharedpreferences.Pref;

@EActivity
public class BaseActivity extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();

    @Pref
    public SharedPreferencesInterface_ sharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁用横屏
//        getSupportActionBar().hide();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //设置沉浸式效果
        QMUIStatusBarHelper.translucent(this);
        //设置状态栏黑色字体
        QMUIStatusBarHelper.setStatusBarLightMode(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        Log.d(TAG, "layoutResID = " + layoutResID);
    }



    protected void setMarginForRelativeLayout(View llTitle) {
//        Window window = getWindow();
//        //设置悬浮透明状态栏
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llTitle.getLayoutParams();
        layoutParams.setMargins(0, getStatusBarHeight(), 0, 0);//4个参数按顺序分别是左上右下etTop(statusBarHeight);
        llTitle.setLayoutParams(layoutParams); //mView是控件
    }

    protected void setMarginForFrameLayout(View llTitle) {
//        Window window = getWindow();
//        //设置悬浮透明状态栏
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) llTitle.getLayoutParams();
        layoutParams.setMargins(0, getStatusBarHeight(), 0, 0);//4个参数按顺序分别是左上右下etTop(statusBarHeight);
        llTitle.setLayoutParams(layoutParams); //mView是控件
    }

    protected void setMarginForLinearLayout(View llTitle) {
//        Window window = getWindow();
//        //设置悬浮透明状态栏
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llTitle.getLayoutParams();
        layoutParams.setMargins(0, getStatusBarHeight(), 0, 0);//4个参数按顺序分别是左上右下etTop(statusBarHeight);
        llTitle.setLayoutParams(layoutParams); //mView是控件

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private static final String STATUS_BAR_VIEW_TAG = "ghStatusBarView";

    protected void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarUpperAPI21(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setStatusBarUpperAPI19(color);
        }
    }

    /**
     * 思路:直接设置状态栏的颜色
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarUpperAPI21(int color) {
        Window window = getWindow();
        //取消设置悬浮透明状态栏,ContentView便不会进入状态栏的下方了
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //设置状态栏颜色
        window.setStatusBarColor(getResources().getColor(color));
    }

    /**
     * 思路:设置状态栏悬浮透明，然后制造一个和状态栏等尺寸的View设置好颜色填进去，就好像是状态栏着色了一样
     *
     * @param color
     */
    private void setStatusBarUpperAPI19(int color) {

        Window window = getWindow();
        //设置悬浮透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
        View statusBarView = decorViewGroup.findViewWithTag(STATUS_BAR_VIEW_TAG);
        if (statusBarView == null) {
            statusBarView = new View(this);

            statusBarView.setTag(STATUS_BAR_VIEW_TAG);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight());
            params.gravity = Gravity.TOP;
            statusBarView.setLayoutParams(params);
            decorViewGroup.addView(statusBarView);

        }
        statusBarView.setBackgroundColor(getResources().getColor(color));

        final ViewGroup contentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        final View childView = contentView.getChildAt(0);
        if (childView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
            childView.setFitsSystemWindows(true);
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = getResources().getDimensionPixelSize(resId);
        }
        return result;
    }

}
