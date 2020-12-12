package com.zhaoweihao.architechturesample.bean;

public class RestResponse {
    /**
     * @payload 可能返回一个对象
     * @success true代表操作成功, false代表操作失败 boolean
     * @msg 返回的信息
     * @code 状态码 int 200代表成功,500代表失败
     * @timestamp 时间戳 long
     * @返回json示例: {"payload": null,
     * "success": false,
     * "msg": "用户名已存在",
     * "code": 500,
     * "timestamp": 1526019544}
     */

    private Object payload;
    private Boolean success;
    private String msg;
    private Integer code;
    private Long timestamp;

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
