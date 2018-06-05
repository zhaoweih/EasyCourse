package com.zhaoweihao.architechturesample.data.course;

import java.util.Date;

public class SendTopic {
    /**
     @"courseId": 4,int
     @"content": String,
     @"tecId": null,
     @"teacherId": "20151120",String
     @"startDate": "2018-05-27 04:10:09.0",date
     @"endDate": null,date
     @"status": 1,int
     */

    private int courseId;
    private String content;
    private String teacherId;
    private int tecId;
    private int status;
    private String startDate;
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
    public String getTeacherId() {
        return teacherId;
    }
    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public int getTecId() {
        return tecId;
    }
    public void setTecId(int tecId) {
        this.tecId = tecId;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
}
