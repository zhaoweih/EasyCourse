package com.zhaoweihao.architechturesample.database;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;
/**
*@description 添加待办事项
*@author
*@time 2019/3/14 13:39
*/
public class Memorandum extends DataSupport implements Serializable {
    private int id;
    private int UserId;
    private String title;
    private String tag;
    private Date date;
    private String content;
    private Boolean isNotify;

    public Memorandum(int userId, String title, String tag, Date date, String content, Boolean isNotify) {
        UserId = userId;
        this.title = title;
        this.tag = tag;
        this.date = date;
        this.content = content;
        this.isNotify = isNotify;
    }

    public Memorandum() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getNotify() {
        return isNotify;
    }

    public void setNotify(Boolean notify) {
        isNotify = notify;
    }

    @Override
    public String toString() {
        return "Memorandum{" +
                "id=" + id +
                ", UserId=" + UserId +
                ", title='" + title + '\'' +
                ", tag='" + tag + '\'' +
                ", date=" + date +
                ", content='" + content + '\'' +
                ", isNotify=" + isNotify +
                '}';
    }
}
