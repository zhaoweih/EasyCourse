package com.zhaoweihao.architechturesample.bean;


import java.io.Serializable;

/**
*@description 用于笔记界面的所有笔记的展示
*@author
*@time 2019/2/16 22:24
*/
public class ShowNote implements Serializable {
    private int id;
    private String title;
    private String content;
    private String resoucrs;
    private int user_id;
    private Boolean is_shared;
    private String tag;
    private long time;
    private int like_num;
    private Boolean isSelected;
    private Boolean isAddedLike;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public void setResoucrs(String resoucrs) {
        this.resoucrs = resoucrs;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Boolean getIs_shared() {
        return is_shared;
    }

    public void setIs_shared(Boolean is_shared) {
        this.is_shared = is_shared;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public Boolean getAddedLike() {
        return isAddedLike;
    }

    public void setAddedLike(Boolean addedLike) {
        isAddedLike = addedLike;
    }

    @Override
    public String toString() {
        return "ShowNote{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", resoucrs='" + resoucrs + '\'' +
                ", user_id=" + user_id +
                ", is_shared=" + is_shared +
                ", tag='" + tag + '\'' +
                ", time=" + time +
                ", like_num=" + like_num +
                ", isSelected=" + isSelected +
                ", isAddedLike=" + isAddedLike +
                '}';
    }
}
