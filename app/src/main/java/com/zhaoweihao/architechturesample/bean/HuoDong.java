package com.zhaoweihao.architechturesample.bean;

import android.graphics.drawable.Drawable;

/**
*@description
 * @Drawable 用于活动首页的图片icon
 * @Tag 用于活动的标签
 * @Title 用于活动的标题
 * @Duration 用于描述活动的持续时间
*@author tanxinkui
*@time 2019/1/22 16:35
*/
public class HuoDong {
    private Drawable drawable;
    private String tag;
    private String Title;
    private String Duration;

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }
}
