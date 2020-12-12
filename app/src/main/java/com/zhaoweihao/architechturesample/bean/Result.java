package com.zhaoweihao.architechturesample.bean;

import java.util.List;

/**
 * @author Admin
 */
public class Result<T> {

    /**
     * payload : [{"id":2,"courseId":10,"stuId":20,"studentId":"2015191054","courseName":"大学语文","teacherName":"赵威豪","password":null},{"id":3,"courseId":10,"stuId":20,"studentId":"2015191054","courseName":"大学语文","teacherName":"赵威豪","password":null}]
     * success : true
     * msg : null
     * code : 200
     * timestamp : 1545304870
     */

    private boolean success;
    private String msg;
    private int code;
    private int timestamp;
    private T payload;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }


    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

}
