package com.zhaoweihao.architechturesample.bean;

import java.io.Serializable;

public class SubmitAndShowResource implements Serializable {
    private int id;
    private String teacher_id;
    private String res_url;
    private String res_name;
    private int res_size;
    private int class_id;
    private String res_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getRes_url() {
        return res_url;
    }

    public void setRes_url(String res_url) {
        this.res_url = res_url;
    }

    public String getRes_name() {
        return res_name;
    }

    public void setRes_name(String res_name) {
        this.res_name = res_name;
    }

    public int getRes_size() {
        return res_size;
    }

    public void setRes_size(int res_size) {
        this.res_size = res_size;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getRes_time() {
        return res_time;
    }

    public void setRes_time(String res_time) {
        this.res_time = res_time;
    }
}
