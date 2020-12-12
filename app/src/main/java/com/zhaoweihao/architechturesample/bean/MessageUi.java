package com.zhaoweihao.architechturesample.bean;

import android.graphics.drawable.Drawable;

/**
*@description 用于消息界面的recycleview Adapter
*@author tanxinkui
*@time 2019/1/18 12:51
*/
public class MessageUi {
    private Drawable drawable;
    private String title;
    private String description;
    private String date;

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
