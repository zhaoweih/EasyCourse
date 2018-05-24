package com.zhaoweihao.architechturesample.data.course;

public class SendNoti {
    /**
     @courseId: 4,
     @content: "课后作业：查询潮州八景",
     @date: 2018-05-25,
     @tecId： 26
     */

    private int courseId;
    private String content;
    private String date;
    private String tecId;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTecId() {
        return tecId;
    }
    public void setTecId(String tecId) {
        this.tecId = tecId;
    }
}
