package com.zhaoweihao.architechturesample.bean;




public class Chat  {

    private Integer id;

    /**
     * 发送者id
     */
    private Integer sender_id;

    /**
     * 接收者id
     */
    private Integer receiver_id;

    /**
     * 发送的内容
     */
    private String msg_content;

    /**
     * 发送的类型
     * 1 文字
     * 2 图片
     * 3 视频
     */
    private Integer send_type;

    /**
     * 发送的时间戳
     */
    private Long msg_time;

    /**
     * 发送者头像url（可选）
     */
    private String sender_avatar;


    /**
     * 接收者头像url(可选)
     */
    private String receiver_avatar;

    /**
     * 课程id
     * 用于群聊
     */
    private Integer course_id;

    /**
     * 班级id
     * 用于群聊
     */
    private Integer class_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSender_id() {
        return sender_id;
    }

    public void setSender_id(Integer sender_id) {
        this.sender_id = sender_id;
    }

    public Integer getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(Integer receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }

    public Integer getSend_type() {
        return send_type;
    }

    public void setSend_type(Integer send_type) {
        this.send_type = send_type;
    }

    public Long getMsg_time() {
        return msg_time;
    }

    public void setMsg_time(Long msg_time) {
        this.msg_time = msg_time;
    }

    public String getSender_avatar() {
        return sender_avatar;
    }

    public void setSender_avatar(String sender_avatar) {
        this.sender_avatar = sender_avatar;
    }

    public String getReceiver_avatar() {
        return receiver_avatar;
    }

    public void setReceiver_avatar(String receiver_avatar) {
        this.receiver_avatar = receiver_avatar;
    }

    public Integer getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Integer course_id) {
        this.course_id = course_id;
    }

    public Integer getClass_id() {
        return class_id;
    }

    public void setClass_id(Integer class_id) {
        this.class_id = class_id;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", sender_id=" + sender_id +
                ", receiver_id=" + receiver_id +
                ", msg_content='" + msg_content + '\'' +
                ", send_type=" + send_type +
                ", msg_time=" + msg_time +
                ", sender_avatar='" + sender_avatar + '\'' +
                ", receiver_avatar='" + receiver_avatar + '\'' +
                ", course_id=" + course_id +
                ", class_id=" + class_id +
                '}';
    }
}
