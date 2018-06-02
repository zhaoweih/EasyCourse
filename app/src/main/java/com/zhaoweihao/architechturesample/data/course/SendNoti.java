package com.zhaoweihao.architechturesample.data.course;

public class SendNoti {
    /**
     @courseId: 4,
     @content: "课后作业：查询潮州八景",
     @date: 2018-05-25,
     @tecId： 26
     @endDate:2018-05-25
     */

    private int courseId;
    private String content;
    private String date;
    private int tecId;
    private String endDate;

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
    public int getTecId() {
        return tecId;
    }
    public void setTecId(int tecId) {
        this.tecId = tecId;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
