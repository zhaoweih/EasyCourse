package com.zhaoweihao.architechturesample.bean;
/**
*@description 点赞
*@author
 * {
 * 	"user_name": "TXK",
 * 	"user_id": 27,
 * 	"notebook_id": 38,
 * }
*@time 2019/3/1 16:50
*/
public class AddLike {
    private String user_name;
    private int user_id;
    private int notebook_id;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getNotebook_id() {
        return notebook_id;
    }

    public void setNotebook_id(int notebook_id) {
        this.notebook_id = notebook_id;
    }
}
