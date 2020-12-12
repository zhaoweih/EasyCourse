package com.zhaoweihao.architechturesample.bean;

import android.graphics.drawable.Drawable;
/**
*@description 用于笔记界面的ui
*@author tanxinkui
*@time 2019/1/18 15:53
*/
public class NoteUi {
    private Drawable userDrawable;
    private String title;
    private String author;
    private String date;
    private String contentTitle;
    private String content;
    private String type;
    private Drawable[] pic;
    private int comment;
    private int likes;

    public Drawable getUserDrawable() {
        return userDrawable;
    }

    public void setUserDrawable(Drawable userDrawable) {
        this.userDrawable = userDrawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Drawable[] getPic() {
        return pic;
    }

    public void setPic(Drawable[] pic) {
        this.pic = pic;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
