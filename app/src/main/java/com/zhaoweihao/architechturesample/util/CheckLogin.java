package com.zhaoweihao.architechturesample.util;

import com.zhaoweihao.architechturesample.database.User;

import org.litepal.crud.DataSupport;


public class CheckLogin {
    public static Boolean ifUserLogin(){
        return (DataSupport.findLast(User.class)!=null);
    }
}

