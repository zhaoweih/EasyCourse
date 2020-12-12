package com.zhaoweihao.architechturesample.bean;

import com.alibaba.fastjson.JSON;

public class UserDataAll {
    private String token;
    private JSON user;
    private String expiredDate;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public JSON getUser() {
        return user;
    }

    public void setUser(JSON user) {
        this.user = user;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }
}
