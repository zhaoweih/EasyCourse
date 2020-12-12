package com.zhaoweihao.architechturesample.bean;

import java.util.ArrayList;

/**
*@description 添加笔记
 * {“title”: ”测试笔记”,
 * 	”content”: ”测试笔记内容”,
 * 	”resoucrs”: ”www.google.com”,
 * 	”user_id”: 10,
 * 	”tag”: ”草稿”,
 * 	”whereValues”: [],
 * 	”saveOrUpdateProperties”: []
 * }
*@author
*@time 2019/2/16 20:14
*/
public class AddNote {
    private String title;
    private String content;
    private String resoucrs;
    private int user_id;
    private String is_shared;
    private String tag;
    private long time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResoucrs() {
        return resoucrs;
    }

    public String getIs_shared() {
        return is_shared;
    }

    public void setIs_shared(String is_shared) {
        this.is_shared = is_shared;
    }

    public void setResoucrs(String resoucrs) {
        this.resoucrs = resoucrs;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
