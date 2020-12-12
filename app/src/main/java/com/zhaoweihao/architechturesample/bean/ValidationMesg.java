package com.zhaoweihao.architechturesample.bean;

/**
 * @author
 * @description 用于验证消息，也可用于获取所有好友
 * @time 2019/3/9 15:59
 */
public class ValidationMesg {
    private int id;
    private String from_username;
    private String to_username;
    private int status;
    private int is_confirmed;
    private long time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom_username() {
        return from_username;
    }

    public void setFrom_username(String from_username) {
        this.from_username = from_username;
    }

    public String getTo_username() {
        return to_username;
    }

    public void setTo_username(String to_username) {
        this.to_username = to_username;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIs_confirmed() {
        return is_confirmed;
    }

    public void setIs_confirmed(int is_confirmed) {
        this.is_confirmed = is_confirmed;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
