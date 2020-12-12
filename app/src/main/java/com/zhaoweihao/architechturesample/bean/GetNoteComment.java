package com.zhaoweihao.architechturesample.bean;

import java.io.Serializable;

/**
*@description 获取笔记评论
*@author
*@time 2019/3/21 16:21
 * "id": 1,
 * 	"content": "这个笔记不错",
 * 	"user_id": 13,
 * 	"owner_id": 14,
 * 	"time": 1548769514262,
 * 	"user_avatar": "https://google.com",
 * 	"user_name": "小镇",
 * 	"notebook_id": 5
*/
public class GetNoteComment implements Serializable {
    private int id;
    private String content;
    private int user_id;
    private int owner_id;
    private long time;
    private String user_avatar;
    private String user_name;
    private int notebook_id;

    public GetNoteComment() {
    }

    public GetNoteComment(int id, String content, int user_id, int owner_id, long time, String user_avatar, String user_name, int notebook_id) {
        this.id = id;
        this.content = content;
        this.user_id = user_id;
        this.owner_id = owner_id;
        this.time = time;
        this.user_avatar = user_avatar;
        this.user_name = user_name;
        this.notebook_id = notebook_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getNotebook_id() {
        return notebook_id;
    }

    public void setNotebook_id(int notebook_id) {
        this.notebook_id = notebook_id;
    }

    @Override
    public String toString() {
        return "GetNoteComment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", user_id=" + user_id +
                ", owner_id=" + owner_id +
                ", time=" + time +
                ", user_avatar='" + user_avatar + '\'' +
                ", user_name='" + user_name + '\'' +
                ", notebook_id=" + notebook_id +
                '}';
    }
}
